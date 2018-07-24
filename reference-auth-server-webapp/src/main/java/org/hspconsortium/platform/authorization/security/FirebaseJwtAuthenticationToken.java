package org.hspconsortium.platform.authorization.security;

import com.google.firebase.auth.FirebaseToken;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;

public class FirebaseJwtAuthenticationToken extends AbstractAuthenticationToken {

    private String jwt;
    private FirebaseToken firebaseToken;

    public FirebaseJwtAuthenticationToken(){
        super(new ArrayList<GrantedAuthority>());
    }

    public FirebaseJwtAuthenticationToken(FirebaseToken firebaseToken, Collection<GrantedAuthority> authorities) {
        super(authorities);
        this.firebaseToken = firebaseToken;
        this.setAuthenticated(true);
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public String getJwt() {
        return jwt;
    }

    public FirebaseToken getFirebaseToken() {
        return firebaseToken;
    }

    public void setFirebaseToken(FirebaseToken firebaseToken) {
        this.firebaseToken = firebaseToken;
    }

    @Override
    public Object getCredentials() {
        return jwt;
    }

    @Override
    public Object getPrincipal() {
        if (firebaseToken == null) {
            return null;
        }

        return firebaseToken.getEmail();
    }
}
