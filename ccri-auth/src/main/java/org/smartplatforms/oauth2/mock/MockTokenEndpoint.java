package org.smartplatforms.oauth2.mock;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import net.minidev.json.JSONObject;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Conditional(MockEnabledCondition.class)
@Controller
public class MockTokenEndpoint {

	private static Logger logger = LoggerFactory.getLogger(MockTokenEndpoint.class);

	@Value("${mock.endpoints.enabled}")
	private boolean mockEnabled;

	@RequestMapping(value = "/mock/token", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = "application/json")
	public @ResponseBody String authorize(@RequestParam Map<String, String> parameters) throws ParseException, JOSEException, IOException, InvalidKeySpecException, NoSuchAlgorithmException {

		if (!mockEnabled) {
			throw new EndpointDisabledException();
		}

		String grantType = parameters.get("grant_type");
		String codeRaw = null;

		if (grantType.equalsIgnoreCase("authorization_code")) {
			codeRaw = parameters.get("code");
		} else if (grantType.equalsIgnoreCase("refresh_token")) {
			codeRaw = parameters.get("refresh_token");
		}

		if (codeRaw != null) {

			String fileName = this.getClass().getClassLoader().getResource("openid-connect-jwks/mock.only.keystore.jwks").getFile();

			JWKSet jwks = JWKSet.load(new File(fileName));
			RSAKey rsaKey = (RSAKey) jwks.getKeys().get(0);
			JWSVerifier verifier = new RSASSAVerifier(rsaKey.toRSAPublicKey());

			SignedJWT incomingSignedJWT = SignedJWT.parse(codeRaw);
			incomingSignedJWT.verify(verifier);

			JWTClaimsSet jwtClaimsSet = incomingSignedJWT.getJWTClaimsSet();

			String scope = (String)jwtClaimsSet.getClaim("scope");
			String context = (String)jwtClaimsSet.getClaim("context");

			Map<String, String> token = new HashMap<>();
			token.put("token_type", "Bearer");
			token.put("expires_in", "3600");
			token.put("scope", scope);
			token.put("client_id", parameters.get("client_id"));

			if (!context.isEmpty()) {
				PlainObject po = PlainObject.parse(context);
				Payload payload = po.getPayload();
				JSONObject jsonObject = payload.toJSONObject();
				for (Map.Entry entry : jsonObject.entrySet()) {
					token.put((String)entry.getKey(), (String)entry.getValue());
				}
			}

			// Create RSA-signer with the private key
			JWSSigner signer = new RSASSASigner(rsaKey.toRSAPrivateKey());

			// Prepare JWT with claims set
			JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
					.claim("token_type", parameters.get("bearer"))
					.claim("client_id", parameters.get("client_id"))
					.expirationTime(new Date(new Date().getTime() + 60 * 60 * 1000)).build(); //5 min

			SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.RS256), claimsSet);

			// Compute the RSA signature
			signedJWT.sign(signer);
			token.put("access_token", signedJWT.serialize());

			if (scope.contains("offline_access")) {
				token.put("'refresh_token'", incomingSignedJWT.serialize());
			}

			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			return ow.writeValueAsString(token);
		}

		return null;
	}

	@ExceptionHandler(EndpointDisabledException.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public void handleAuthorizationException(HttpServletResponse response, Exception e) throws IOException {
		response.getWriter().write(e.getMessage());
	}

}
