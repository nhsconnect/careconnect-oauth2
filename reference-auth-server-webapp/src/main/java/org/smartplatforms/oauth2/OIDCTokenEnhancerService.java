package org.smartplatforms.oauth2;

import org.mitre.oauth2.model.ClientDetailsEntity;
import org.mitre.oauth2.model.OAuth2AccessTokenEntity;
import org.springframework.security.oauth2.provider.OAuth2Request;

import java.util.Date;
import java.util.Map;

/**
 */
public interface OIDCTokenEnhancerService {

    public OAuth2AccessTokenEntity createIdToken(ClientDetailsEntity client,
                                                 OAuth2Request request,
                                                 Date issueTime,
                                                 String sub,
                                                 OAuth2AccessTokenEntity accessToken,
                                                 Map<String, Object> customClaims);

    /**
     * Create an id token with the information provided.
     *
     * @param client
     * @param request
     * @param issueTime
     * @param sub
     * @param signingAlg
     * @param accessToken
     * @return
     */
    public OAuth2AccessTokenEntity createIdToken(
            ClientDetailsEntity client, OAuth2Request request, Date issueTime,
            String sub, OAuth2AccessTokenEntity accessToken);

    /**
     * Create a registration access token for the given client.
     *
     * @param client
     * @return
     */
    public OAuth2AccessTokenEntity createRegistrationAccessToken(ClientDetailsEntity client);

    /**
     * Create a resource access token for the given client (protected resource).
     *
     * @param client
     * @return
     */
    public OAuth2AccessTokenEntity createResourceAccessToken(ClientDetailsEntity client);

    /**
     * Rotate the registration or resource token for a client
     * @param client
     * @return
     */
    public OAuth2AccessTokenEntity rotateRegistrationAccessTokenForClient(ClientDetailsEntity client);

}
