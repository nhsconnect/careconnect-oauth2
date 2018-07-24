package org.smartplatforms.oauth2;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.PlainJWT;
import com.nimbusds.jwt.SignedJWT;
import org.hspconsortium.platform.authentication.launch.LaunchContextAuthenticationInterceptor;
import org.mitre.jwt.signer.service.JWTSigningAndValidationService;
import org.mitre.jwt.signer.service.impl.JWKSetCacheService;
import org.mitre.jwt.signer.service.impl.SymmetricKeyJWTValidatorCacheService;
import org.mitre.oauth2.model.ClientDetailsEntity;
import org.mitre.oauth2.model.OAuth2AccessTokenEntity;
import org.mitre.oauth2.service.ClientDetailsEntityService;
import org.mitre.oauth2.service.SystemScopeService;
import org.mitre.openid.connect.config.ConfigurationPropertiesBean;
import org.mitre.openid.connect.model.UserInfo;
import org.mitre.openid.connect.service.ApprovedSiteService;
import org.mitre.openid.connect.service.UserInfoService;
import org.mitre.openid.connect.token.ConnectTokenEnhancer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.Map.Entry;

@Service
public class SmartLaunchTokenEnhancer implements TokenEnhancer {

    /**
     * Logger for this class
     */
    private static final Logger logger = LoggerFactory.getLogger(ConnectTokenEnhancer.class);

    private Gson gson = new Gson();

    @Autowired
    private ConfigurationPropertiesBean configBean;

    @Autowired
    private JWTSigningAndValidationService jwtService;

    @Autowired
    private ClientDetailsEntityService clientService;

    @Autowired
    private ApprovedSiteService approvedSiteService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private OIDCTokenEnhancerService connectTokenService;

    @Autowired
    private JWKSetCacheService encryptors;

    @Autowired
    private SymmetricKeyJWTValidatorCacheService symmetricCacheService;

    Function<LaunchContextEntity, String> key = new Function<LaunchContextEntity, String>() {
        @Override
        public String apply(LaunchContextEntity input) {
            return input.getName();
        }
    };


    Function<Entry<String,String>, LaunchContextEntity> toLaunchContextEntity = new Function<Entry<String,String>, LaunchContextEntity>() {
        @Override
        public LaunchContextEntity apply(Entry<String, String> input) {
            LaunchContextEntity e = new LaunchContextEntity();
            e.setName(input.getKey());
            e.setValue(input.getValue());
            return e;
        }
    };

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken,	OAuth2Authentication authentication)  {
        OAuth2AccessTokenEntity token = (OAuth2AccessTokenEntity) accessToken;
        OAuth2Request originalAuthRequest = authentication.getOAuth2Request();

        String clientId = originalAuthRequest.getClientId();
        ClientDetailsEntity client = clientService.loadClientByClientId(clientId);

        JWTClaimsSet.Builder claimsBuilder = new JWTClaimsSet.Builder();

        claimsBuilder.audience(Lists.newArrayList(clientId));

        claimsBuilder.issuer(configBean.getIssuer());

        claimsBuilder.issueTime(new Date());

        claimsBuilder.expirationTime(token.getExpiration());

        claimsBuilder.jwtID(UUID.randomUUID().toString()); // set a random NONCE in the middle of it

        claimsBuilder.claim("scope", Joiner.on(" ").join(accessToken.getScope()));

        JWSAlgorithm signingAlg = jwtService.getDefaultSigningAlgorithm();

        SignedJWT signed = new SignedJWT(new JWSHeader(signingAlg), claimsBuilder.build());

        jwtService.signJwt(signed);

        token.setJwt(signed);

        /**
         * Authorization request scope MUST include "openid" in OIDC, but access token request
         * may or may not include the scope parameter. As long as the AuthorizationRequest
         * has the proper scope, we can consider this a valid OpenID Connect request. Otherwise,
         * we consider it to be a vanilla OAuth2 request.
         *
         * Also, there must be a user authentication involved in the request for it to be considered
         * OIDC and not OAuth, so we check for that as well.
         */
        if (originalAuthRequest.getScope().contains(SystemScopeService.OPENID_SCOPE)
                && !authentication.isClientOnly()) {

            String username = authentication.getName();
            UserInfo userInfo = userInfoService.getByUsernameAndClientId(username, clientId);

            if (userInfo != null) {

                Map<String, Object> customClaims = new HashMap<>();
//                customClaims.put("profile", originalAuthRequest.getRequestParameters().get("aud") + "/Practitioner/demo");
                customClaims.put("profile", userInfo.getProfile());
                customClaims.put("sub", userInfo.getPreferredUsername());
                customClaims.put("displayName", userInfo.getName());
                customClaims.put("organizationName", userInfo.getWebsite());
                customClaims.put("email", userInfo.getEmail());

                OAuth2AccessTokenEntity idTokenEntity = connectTokenService.createIdToken(client,
                        originalAuthRequest, claimsBuilder.build().getIssueTime(),
                        userInfo.getSub(), token, customClaims);

                // attach the id token to the parent access token
                token.setIdToken(idTokenEntity.getJwt());
            } else {
                // can't create an id token if we can't find the user
                logger.warn("Request for ID token when no user is present.");
            }
        }

        @SuppressWarnings("unchecked")
        String extensions = (String) authentication.getOAuth2Request().getExtensions().get("launch_context");

        if (extensions == null) {
            return token;
        }

        @SuppressWarnings("unchecked")
        Map<String, String> contextMap = (HashMap<String,String>)gson.fromJson(extensions, HashMap.class);

        // remove our authentication launch parameter (if it exists), the application doesn't need it...nor should it have it.
        contextMap.remove(LaunchContextAuthenticationInterceptor.USER_EMAIL_LAUNCH_CONTEXT_PARAM);

        Set<LaunchContextEntity> context = FluentIterable
                .from(contextMap.entrySet())
                .transform(toLaunchContextEntity)
                .toSet();

        token.setLaunchContext(new HashSet<LaunchContextEntity>(context));
        return token;
    }

}
