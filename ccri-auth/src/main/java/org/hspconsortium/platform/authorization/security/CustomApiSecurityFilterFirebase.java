package org.hspconsortium.platform.authorization.security;

import com.google.common.collect.ImmutableList;
import com.google.firebase.auth.FirebaseToken;
import org.hspconsortium.platform.service.FirebaseTokenService;
import org.mitre.openid.connect.model.UserInfo;
import org.mitre.openid.connect.repository.impl.JpaUserInfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component("customApiSecurityFilter")
@Profile("users-firebase")
public class CustomApiSecurityFilterFirebase extends GenericFilterBean implements CustomApiSecurityFilter {

    private final Logger log = LoggerFactory.getLogger(CustomApiSecurityFilterFirebase.class);

    @Value("${hspc.platform.auth.cookieName}")
    private String cookieName;

    @Inject
    private FirebaseTokenService firebaseTokenService;

    @PersistenceContext
    private EntityManager manager;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JpaUserInfoRepository jpaUserInfoRepository;

    public CustomApiSecurityFilterFirebase() {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        FirebaseJwtAuthenticationToken token = getAuthIfCookieExists((HttpServletRequest) servletRequest);
        if (token != null) {
            SecurityContextHolder.getContext().setAuthentication(token);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private FirebaseJwtAuthenticationToken getAuthIfCookieExists(HttpServletRequest httpServletRequest) {


        String jwt = resolveToken(httpServletRequest);
        if (jwt != null) {
            log.trace("jwt not null. Validating jwt");
            FirebaseToken token = firebaseTokenService.validateToken(jwt);

            if (token != null) {
                log.trace(token.getEmail());
                return new FirebaseJwtAuthenticationToken(token, retrieveAuthorities(token.getEmail()));
            }
            return new FirebaseJwtAuthenticationToken();
        } else {
            log.trace("jwt not containing dough ="+cookieName);
        }
        return null;
    }

    private Collection<GrantedAuthority> retrieveAuthorities(String email) {

        UserInfo userInfo = jpaUserInfoRepository.getByEmailAddress(email);
        log.trace("Checking permissions for "+email);
        if (userInfo == null)
            return defaultAuths();
        String username = userInfo.getPreferredUsername();
        log.trace("Checking permissions for PreferredUsername = "+username);
        log.trace("Checking permissions in database "+dataSource);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        List<? extends GrantedAuthority> authorities = null;
        String quString = "SELECT * from authorities where username = ?";
        authorities = jdbcTemplate.query(quString, new String[]{username},
                (RowMapper<GrantedAuthority>) (rs, rowNum) -> {
                    SimpleGrantedAuthority authority = new SimpleGrantedAuthority(
                            rs.getString("authority"));
                    return authority;
                });
        if (authorities == null || authorities.isEmpty()) {
            authorities = defaultAuths();

        }
        return ImmutableList.copyOf(authorities);
    }

    private List<GrantedAuthority> defaultAuths() {
        List<GrantedAuthority> auths = new ArrayList<GrantedAuthority>();
        GrantedAuthority a = new SimpleGrantedAuthority("ROLE_USER");
        auths.add(a);
        return auths;
    }

    private String resolveToken(HttpServletRequest httpServletRequest) {
        Cookie[] cookies = httpServletRequest.getCookies();
        if (cookies == null)
            return null;

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)) {
                return cookie.getValue();
            }
        }

        return null;
    }
}
