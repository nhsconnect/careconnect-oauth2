package org.hspconsortium.platform.authorization.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class FirebaseJwtAuthenticationProvider implements AuthenticationProvider {

    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        FirebaseJwtAuthenticationToken firebaseJwtAuthenticationToken = (FirebaseJwtAuthenticationToken) authentication;

        if (firebaseJwtAuthenticationToken == null || !firebaseJwtAuthenticationToken.isAuthenticated()) {
            // the token is invalid
            return null;
        }

        return firebaseJwtAuthenticationToken;
    }

    public boolean supports(Class<?> authentication) {
        return authentication.equals(FirebaseJwtAuthenticationToken.class);
    }
}