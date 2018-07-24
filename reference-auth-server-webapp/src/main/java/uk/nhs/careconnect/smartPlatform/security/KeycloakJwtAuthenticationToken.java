package uk.nhs.careconnect.smartPlatform.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import uk.nhs.careconnect.smartPlatform.KeycloakToken;

import java.util.ArrayList;
import java.util.Collection;

public class KeycloakJwtAuthenticationToken extends AbstractAuthenticationToken {

    private String jwt;
    private KeycloakToken keycloakToken;

    public KeycloakJwtAuthenticationToken(){
        super(new ArrayList<GrantedAuthority>());
    }

    public KeycloakJwtAuthenticationToken(KeycloakToken keycloakToken, Collection<GrantedAuthority> authorities) {
        super(authorities);
        this.keycloakToken = keycloakToken;
        this.setAuthenticated(true);
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public String getJwt() {
        return jwt;
    }

    public KeycloakToken getKeycloakToken() {
        return keycloakToken;
    }

    public void setKeycloakToken(KeycloakToken keycloakToken) {
        this.keycloakToken = keycloakToken;
    }

    @Override
    public Object getCredentials() {
        return jwt;
    }

    @Override
    public Object getPrincipal() {
        if (keycloakToken == null) {
            return null;
        }

        return keycloakToken.getEmail();
    }
}
