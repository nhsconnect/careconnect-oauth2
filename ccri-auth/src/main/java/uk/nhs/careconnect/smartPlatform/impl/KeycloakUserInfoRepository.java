package uk.nhs.careconnect.smartPlatform.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hspconsortium.platform.authentication.persona.PersonaUserInfoRepository;
import uk.nhs.careconnect.smartPlatform.service.KeycloakTokenService;
import org.mitre.openid.connect.model.DefaultUserInfo;
import org.mitre.openid.connect.model.UserInfo;

import javax.inject.Inject;

public class KeycloakUserInfoRepository extends PersonaUserInfoRepository {
    @Inject
    private KeycloakTokenService keycloakTokenService;

    private Log log = LogFactory.getLog(KeycloakUserInfoRepository.class);

    @Override
    public UserInfo getRealUserByUsername(String username) {
        log.trace("FirebaseUserInfoRepository.getRealUserByUsername = " + username);
        // validate username against keycloak
        KeycloakUserProfileDto keycloakUserProfileDto = keycloakTokenService.getUserProfileInfo(username);
        if(keycloakUserProfileDto == null)
            return null;

        UserInfo userInfo = new DefaultUserInfo();

        userInfo.setSub(keycloakUserProfileDto.getUid());
        userInfo.setPreferredUsername(keycloakUserProfileDto.getEmail());
        userInfo.setEmail(keycloakUserProfileDto.getEmail());
        userInfo.setName(keycloakUserProfileDto.getDisplayName());

        return userInfo;
    }
}
