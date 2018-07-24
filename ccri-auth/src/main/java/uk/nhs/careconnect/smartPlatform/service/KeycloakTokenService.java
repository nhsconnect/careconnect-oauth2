package uk.nhs.careconnect.smartPlatform.service;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.hspconsortium.platform.authorization.repository.impl.FirebaseUserProfileDto;
import org.keycloak.OAuth2Constants;
import org.keycloak.RSATokenVerifier;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import uk.nhs.careconnect.smartPlatform.KeycloakToken;
import uk.nhs.careconnect.smartPlatform.impl.KeycloakUserProfileDto;

import javax.annotation.PostConstruct;

import java.io.InputStream;



public class KeycloakTokenService {

    private Log log = LogFactory.getLog(KeycloakTokenService.class);

    Keycloak keycloak;

    private KeycloakUserProfileDto userProfileDto = null;

    @Autowired
    private ResourceLoader resourceLoader;

    @PostConstruct
    private void initKeycloak() {

        log.info("initKeycloak()");



        InputStream configStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("keycloak.json");

        if (configStream == null) {
            throw new RuntimeException("Could not find any keycloak.json file in classpath.");
        }

        keycloak = KeycloakBuilder.builder() //
                .serverUrl("http://localhost:8080/auth") //
                .realm("fhir") //
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS) //
                .clientId("ccri-auth") //
                .clientSecret("f39fd5b4-9bef-4574-a38c-5d460cf2c6a9") //
                .build();
        /*
        try {
            authzClient = AuthzClient.create(JsonSerialization.readValue(configStream, Configuration.class));
        } catch (IOException e) {
            throw new RuntimeException("Could not parse configuration.", e);

        }
        */

    }


    public KeycloakToken validateToken(String jwtToken) {
        try {
            log.info("validateToken: "+jwtToken);

            if (keycloak== null) {
                log.info("keycloak null");
            }


            RSATokenVerifier verifier = RSATokenVerifier.create(jwtToken);

            log.info(verifier.getToken().getType());
            log.info("Token email :" + verifier.getToken().getEmail());

            /*
            PublicKey publicKey = getRealmPublicKey( verifier.getHeader());

            JwtParser jwtParser =Jwts.parser().setSigningKey(getKey(""));

            Jwt parsedJwt = jwtParser.parse(jwt);

            // String scope = claims.getBody().get("scope").toString();
            log.info(parsedJwt.getBody().toString());
*/

            KeycloakToken keycloakToken = new KeycloakToken();
            if (verifier.getToken().getEmail()!=null) {
                keycloakToken.setEmail(verifier.getToken().getEmail());
            } else {
                keycloakToken.setEmail(verifier.getToken().getName());
            }



            return keycloakToken;
        } catch (Throwable ex) {

            ex.printStackTrace();
            log.info("Expired token value: " + jwtToken);
            return null;
        }
    }

    public KeycloakUserProfileDto getUserProfileInfo(String email) {
        log.trace("KeycloakTokenService.getUserProfileInfo "+email);
        userProfileDto = new KeycloakUserProfileDto();
        userProfileDto.setEmail(email);

        return userProfileDto;
    }

/*
    private PublicKey getRealmPublicKey(Keycloak keycloak, JWSHeader jwsHeader) {

// Variant 1: use openid-connect /certs endpoint
        return retrievePublicKeyFromCertsEndpoint(jwsHeader);

// Variant 2: use the Public Key referenced by the "kid" in the JWSHeader
// in order to access realm public key we need at least realm role... e.g. view-realm
//      return retrieveActivePublicKeyFromKeysEndpoint(keycloak, jwsHeader);

// Variant 3: use the active RSA Public Key exported by the PublicRealmResource representation
//      return retrieveActivePublicKeyFromPublicRealmEndpoint();
    }
    */
/*
    private PublicKey retrievePublicKeyFromCertsEndpoint(JWSHeader jwsHeader) {
        try {
            ObjectMapper om = new ObjectMapper();
            @SuppressWarnings("unchecked")
            Map<String, Object> certInfos = om.readValue(new URL(getRealmCertsUrl()).openStream(), Map.class);

            List<Map<String, Object>> keys = (List<Map<String, Object>>) certInfos.get("keys");

            Map<String, Object> keyInfo = null;
            for (Map<String, Object> key : keys) {
                String kid = (String) key.get("kid");

                if (jwsHeader.getKeyId().equals(kid)) {
                    keyInfo = key;
                    break;
                }
            }

            if (keyInfo == null) {
                return null;
            }

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            String modulusBase64 = (String) keyInfo.get("n");
            String exponentBase64 = (String) keyInfo.get("e");

            // see org.keycloak.jose.jwk.JWKBuilder#rs256
            Decoder urlDecoder = Base64.getUrlDecoder();
            BigInteger modulus = new BigInteger(1, urlDecoder.decode(modulusBase64));
            BigInteger publicExponent = new BigInteger(1, urlDecoder.decode(exponentBase64));

            return keyFactory.generatePublic(new RSAPublicKeySpec(modulus, publicExponent));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    */
}
