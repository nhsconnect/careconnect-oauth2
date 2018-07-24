package org.hspconsortium.platform.authorization.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class FirebaseJwtLoginUrlAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {
    public FirebaseJwtLoginUrlAuthenticationEntryPoint(String loginFormUrl, String baseUrl) {
        super(loginFormUrl);
        this.baseUrl = baseUrl;
    }

    private String baseUrl;

    @Override
    protected String buildRedirectUrlToLoginPage(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
        StringBuilder loginFormBuilder = new StringBuilder(determineUrlToUseForThisRequest(request, response, authException));
        try {
            loginFormBuilder.append("?afterAuth=");
            loginFormBuilder.append(URLEncoder.encode(this.baseUrl, "UTF-8"));
            String servletPath = getServletPath(request);
            loginFormBuilder.append(servletPath.equals("") ? "" : URLEncoder.encode(servletPath, "UTF-8"));
            if (request.getQueryString() != null) {
                loginFormBuilder.append(URLEncoder.encode("?" + request.getQueryString(), "UTF-8"));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return loginFormBuilder.toString();
    }

    private String getServletPath(HttpServletRequest request) {
        String servletPath = request.getServletPath();

        if (servletPath.startsWith("/")) {
            return servletPath.substring(1);
        }

        return servletPath;
    }
}