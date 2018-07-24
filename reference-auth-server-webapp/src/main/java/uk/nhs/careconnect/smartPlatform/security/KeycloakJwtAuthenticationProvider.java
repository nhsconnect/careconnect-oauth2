package uk.nhs.careconnect.smartPlatform.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hspconsortium.platform.authorization.security.FirebaseJwtAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class KeycloakJwtAuthenticationProvider implements AuthenticationProvider {

    private Log log = LogFactory.getLog(KeycloakJwtAuthenticationProvider.class);

    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("In keycloak authentication  provider");
        KeycloakJwtAuthenticationToken keycloakJwtAuthenticationToken = (KeycloakJwtAuthenticationToken) authentication;

        if (keycloakJwtAuthenticationToken == null || !keycloakJwtAuthenticationToken.isAuthenticated()) {
            // the token is invalid
            return null;
        }

        return keycloakJwtAuthenticationToken;
    }

    public boolean supports(Class<?> authentication) {
        return authentication.equals(FirebaseJwtAuthenticationToken.class);
    }
}