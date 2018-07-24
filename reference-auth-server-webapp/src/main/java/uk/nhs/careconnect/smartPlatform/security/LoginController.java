package uk.nhs.careconnect.smartPlatform.security;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Controller
@Profile("users-keycloak")
public class LoginController {

    @Value("${hspc.platform.accountLoginPage}")
    private String loginUrl;

    @Value("${hspc.platform.accountLogoutPage}")
    private String logoutUrl;

    @Value("${oidc.issuer}")
    private String oidcIssuer;

    @RequestMapping({"login", "login/"})
    public String doLoginRedirect(@RequestParam(required = false) String hspcRedirectUrl) {
        hspcRedirectUrl = hspcRedirectUrl != null ? hspcRedirectUrl : oidcIssuer;
        return "redirect:" + loginUrl + buildRedirectUrlParam("afterAuth", hspcRedirectUrl);
    }

    @RequestMapping({"logout", "logout/"})
    public String doLogoutRedirect(@RequestParam(required = false) String hspcRedirectUrl) {
        hspcRedirectUrl = hspcRedirectUrl != null ? hspcRedirectUrl : oidcIssuer;
        return "redirect:" + logoutUrl + buildRedirectUrlParam("afterLogout", hspcRedirectUrl);
    }

    private String buildRedirectUrlParam(String paramName, String paramValue) {
        StringBuilder builder = new StringBuilder();
        builder.append("?");
        builder.append(paramName);
        builder.append("=");
        try {
            builder.append(URLEncoder.encode(paramValue, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
        return builder.toString();
    }
}