package org.smartplatforms.oauth2.mock;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

@Configuration
@Conditional(MockEnabledCondition.class)
@Controller
public class MockAuthorizationEndpoint {

	private static Logger logger = LoggerFactory.getLogger(MockAuthorizationEndpoint.class);

	@Value("${mock.endpoints.enabled}")
	private boolean mockEnabled;

	@RequestMapping(value = "/mock/authorize", method = RequestMethod.GET)
	public void authorize(HttpServletResponse response,
						  @RequestParam Map<String, String> parameters) throws IOException, ParseException, JOSEException, InvalidKeySpecException, NoSuchAlgorithmException {

		if (!mockEnabled) {
			throw new EndpointDisabledException();
		}

		String fileName = this.getClass().getClassLoader().getResource("openid-connect-jwks/mock.only.keystore.jwks").getFile();

		JWKSet jwks = JWKSet.load(new File(fileName));
		RSAKey rsaKey = (RSAKey) jwks.getKeys().get(0);
		// Create RSA-signer with the private key
		JWSSigner signer = new RSASSASigner(rsaKey.toRSAPrivateKey());

		String launchContext = "";
		if (parameters.containsKey("launch")) {
			launchContext = java.net.URLDecoder.decode(parameters.get("launch"), StandardCharsets.UTF_8.name());
		}

		// Prepare JWT with claims set
		JWTClaimsSet.Builder claimsSetBuilder = new JWTClaimsSet.Builder();
		claimsSetBuilder.claim("context", launchContext);
		claimsSetBuilder.claim("client_id", parameters.get("client_id"));
		claimsSetBuilder.claim("scope", parameters.get("scope"));
		claimsSetBuilder.expirationTime(new Date(new Date().getTime() + 5 * 60 * 1000)); //5 min

		SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.RS256), claimsSetBuilder.build());

		// Compute the RSA signature
		signedJWT.sign(signer);

		// build the response redirect
		response.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
		response.setHeader("Location", parameters.get("redirect_uri") +
				"?code=" + signedJWT.serialize() +
				"&state=" + parameters.get("state")
		);
	}

	@ExceptionHandler(EndpointDisabledException.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public void handleAuthorizationException(HttpServletResponse response, Exception e) throws IOException {
		response.getWriter().write(e.getMessage());
	}

}
