package org.hspconsortium.platform.authorization.launchcontext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mitre.oauth2.model.OAuth2AccessTokenEntity;
import org.mitre.oauth2.repository.OAuth2TokenRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
public class LaunchContextIntrospectionInterceptor extends HandlerInterceptorAdapter {

    private Log log = LogFactory.getLog(LaunchContextIntrospectionInterceptor.class);

    public static final String ACCESS_TOKEN_PARAM_KEY = "token";
    public static final String ENTITY_PARAM_KEY = "entity";
    public static final String LAUNCH_CONTEXT_PARAM_KEY = "launch_context";

    private final OAuth2TokenRepository oAuth2TokenRepository;

    @Inject
    public LaunchContextIntrospectionInterceptor(OAuth2TokenRepository oAuth2TokenRepository) {
        this.oAuth2TokenRepository = oAuth2TokenRepository;
    }


    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) throws Exception {

        String authTokenString = request.getParameter(ACCESS_TOKEN_PARAM_KEY);

        // if for some reason we've gotten here, but there is no authorization token with the request, then we abort
        // attempting to add the launch_context
        if (authTokenString == null || authTokenString.isEmpty()) {
            log.warn("Introspection endpoint reached, but no access token found.");
            return;
        }

        // check if a launch context is associated with this auth token
        OAuth2AccessTokenEntity accessTokenEntity = oAuth2TokenRepository.getAccessTokenByValue(authTokenString);

        // no additional information, just return
        if(accessTokenEntity == null)
            return;

        Map<String, Object> launchContextParamMap = accessTokenEntity.getAdditionalInformation();

        // the 'getAdditionalInformation()' method adds launch context params AND the access token to this map, since
        // we are only looking for launch context then let's remove the access token if it exists
        launchContextParamMap.remove(OAuth2AccessTokenEntity.ID_TOKEN_FIELD_NAME);

        // add launch context to response if any exists for given access token
        if (launchContextParamMap != null && !launchContextParamMap.isEmpty()) {

            @SuppressWarnings("unchecked")
            Map<String, Object> entity = (Map<String, Object>) modelAndView.getModelMap().get(ENTITY_PARAM_KEY);
            entity.put(LAUNCH_CONTEXT_PARAM_KEY, generateLaunchContextValue(launchContextParamMap));
        }
    }

    /**
     *  From a map of launch context key/values, create a CSV list
     * @param launchContextParamMap
     * @return All launch context in the format:
     *
     *   key1=value1,key2=value2,...
     *
     */
    private String generateLaunchContextValue(Map<String, Object> launchContextParamMap) {
        StringBuilder stringBuilder = new StringBuilder();
        Object[] keys = launchContextParamMap.keySet().toArray();

        for (int i = 0; i < keys.length; i++) {
            String key = (String) keys[i];
            String value = String.valueOf(launchContextParamMap.get(key));
            stringBuilder.append(key + "=" + value);
            if (i != keys.length - 1) {
                stringBuilder.append(",");
            }
        }

        return stringBuilder.toString();
    }
}
