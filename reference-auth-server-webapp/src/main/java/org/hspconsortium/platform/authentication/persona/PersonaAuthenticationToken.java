package org.hspconsortium.platform.authentication.persona;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Functionality this is no different than the UsernamePasswordAuthentiationToken, however this allows us to distinguish
 * a persona login in the oic.saved_user_auth table.
 */
public class PersonaAuthenticationToken extends UsernamePasswordAuthenticationToken {
    public PersonaAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public PersonaAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }
}
