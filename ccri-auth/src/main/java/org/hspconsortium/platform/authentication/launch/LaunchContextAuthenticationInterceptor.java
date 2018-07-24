package org.hspconsortium.platform.authentication.launch;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hspconsortium.platform.authorization.launchcontext.LaunchContextHolder;
import org.hspconsortium.platform.security.LaunchContext;
import org.smartplatforms.oauth2.LaunchOrchestrationReceiveEndpoint;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.UriComponentsBuilder;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

@Component
public class LaunchContextAuthenticationInterceptor extends HandlerInterceptorAdapter {

    private Log log = LogFactory.getLog(LaunchContextAuthenticationInterceptor.class);

    public static final String USER_EMAIL_LAUNCH_CONTEXT_PARAM = "4ae23017813e417d937e3ba21974581d";
    @Inject
    private LaunchContextHolder launchContextHolder;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {
        if (httpServletRequest.getServletPath().startsWith("/authorize")) {
            authenticateByLaunchContext(httpServletRequest);
        }

        return true;
    }

    private void authenticateByLaunchContext(HttpServletRequest httpServletRequest) {

        String launchId = null;
        if (httpServletRequest.getMethod().equals(HttpMethod.GET.name())) {
            launchId = httpServletRequest.getParameter("launch");
        } else if (httpServletRequest.getMethod().equals(HttpMethod.POST.name())) {
            String referer = httpServletRequest.getHeader("Referer");
            log.info("** Referrer = "+referer);
            MultiValueMap<String, String> parameters = null;
            try {
                parameters = UriComponentsBuilder.fromUri(new URI(referer)).build().getQueryParams();
            } catch (URISyntaxException e) {
                return;
            }
            if (parameters.get("launch") != null) {
                launchId = parameters.get("launch").get(0);
            }
        } else {
            return;
        }
        log.info("GET Launch Id = " + launchId);

        // if there is no launch reqeust, proceed as normal
        if (StringUtils.isEmpty(launchId)) {
            log.info("Empty Lauch Id");
            return;
        }

        // retrieve launch context
        LaunchContext launchContext = launchContextHolder.getLaunchContext(launchId);


        if (launchContext.getParam(USER_EMAIL_LAUNCH_CONTEXT_PARAM) == null) {
            return;
        }

        LaunchContextAuthenticationToken launchContextAuthenticationToken =
                generateLaunchContextAuthentication(launchContext);


        SecurityContext launchContextSecurityContext = SecurityContextHolder.createEmptyContext();

        launchContextSecurityContext.setAuthentication(launchContextAuthenticationToken);

        SecurityContextHolder.setContext(launchContextSecurityContext);
    }

    private LaunchContextAuthenticationToken generateLaunchContextAuthentication(LaunchContext launchContext) {

        String username = String.valueOf(launchContext.getParam(USER_EMAIL_LAUNCH_CONTEXT_PARAM));

        List<SimpleGrantedAuthority> launchContextAuthorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));

        User launchContextUser = new User(username, "password", launchContextAuthorities);

        return new LaunchContextAuthenticationToken(launchContextUser, null, launchContextAuthorities);
    }
}
