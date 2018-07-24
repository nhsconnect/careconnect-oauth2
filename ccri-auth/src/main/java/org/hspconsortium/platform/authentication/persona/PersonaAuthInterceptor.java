package org.hspconsortium.platform.authentication.persona;

import org.hspconsortium.platform.service.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;

public class PersonaAuthInterceptor extends HandlerInterceptorAdapter {

    @Value("${hspc.platform.persona.cookieName}")
    private String personaCookieName;

    @Value("${hspc.platform.persona.cookieDomain}")
    private String personaCookieDomain;

    @Inject
    private JwtService jwtService;


    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {
        if (httpServletRequest.getServletPath().startsWith("/authorize")) {
            authenticatePersonaUser(httpServletRequest);
        } else if (httpServletRequest.getServletPath().startsWith("/token")) {
            removePersonaCookie(httpServletRequest, httpServletResponse);
        }

        return true;
    }

    private void removePersonaCookie(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

        if (httpServletRequest.getCookies() == null)
            return;

        for (Cookie cookie : httpServletRequest.getCookies()) {
            if (cookie.getName().equals(personaCookieName)) {
                cookie.setPath("/");
                cookie.setMaxAge(0);
                httpServletResponse.addCookie(cookie);
            }
        }
    }

    private void authenticatePersonaUser(HttpServletRequest httpServletRequest) {
        Cookie[] cookies = httpServletRequest.getCookies();

        if (cookies == null)
            return;

        Cookie hspcPersonaTokenCookie = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(personaCookieName)) {
                hspcPersonaTokenCookie = cookie;
            }
        }

        // if there is no persona cookie, proceed as normal
        if (hspcPersonaTokenCookie == null) {
            return;
        }

        PersonaAuthenticationToken personaAuthentication = generatePersonaAuthentication(hspcPersonaTokenCookie.getValue());

        SecurityContext personaSecurityContext = SecurityContextHolder.createEmptyContext();
        personaSecurityContext.setAuthentication(personaAuthentication);
        SecurityContextHolder.setContext(personaSecurityContext);
    }

    private PersonaAuthenticationToken generatePersonaAuthentication(String personaJwtString) {

        String username = jwtService.usernameFromJwt(personaJwtString);

        if (username == null) {
            throw new SecurityException("Invalid JWT while trying to authenticate persona user.");
        }

        List<SimpleGrantedAuthority> personaAuthorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        User personaUser = new User(username, "password", personaAuthorities);
        return new PersonaAuthenticationToken(personaUser, null, personaAuthorities);
    }
}
