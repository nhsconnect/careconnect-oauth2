package org.hspconsortium.platform.authorization.repository.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hspconsortium.platform.authentication.persona.PersonaUserInfoRepository;
import org.hspconsortium.platform.service.FirebaseTokenService;
import org.mitre.openid.connect.model.DefaultUserInfo;
import org.mitre.openid.connect.model.UserInfo;

import javax.inject.Inject;

public class FirebaseUserInfoRepository extends PersonaUserInfoRepository {
    @Inject
    private FirebaseTokenService firebaseTokenService;

    private Log log = LogFactory.getLog(FirebaseUserInfoRepository.class);

    @Override
    public UserInfo getRealUserByUsername(String username) {
        log.trace("FirebaseUserInfoRepository.getRealUserByUsername = " + username);
        // validate username against Firebase
        FirebaseUserProfileDto firebaseUserProfileDto = firebaseTokenService.getUserProfileInfo(username);
        if(firebaseUserProfileDto == null)
            return null;

        UserInfo userInfo = new DefaultUserInfo();

        userInfo.setSub(firebaseUserProfileDto.getUid());
        userInfo.setPreferredUsername(firebaseUserProfileDto.getEmail());
        userInfo.setEmail(firebaseUserProfileDto.getEmail());
        userInfo.setName(firebaseUserProfileDto.getDisplayName());

        return userInfo;
    }
}
