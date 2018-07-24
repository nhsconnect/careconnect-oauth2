package org.hspconsortium.platform.authorization.security;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;

/**
 * Used as a place holder on profiles that don't require custom security.
 */
@Component("customApiSecurityFilter")
@Profile("!users-keycloak")
public class CustomApiSecurityFilterPlaceholder implements CustomApiSecurityFilter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
