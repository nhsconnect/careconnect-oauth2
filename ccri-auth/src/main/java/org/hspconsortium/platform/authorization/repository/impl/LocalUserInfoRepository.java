package org.hspconsortium.platform.authorization.repository.impl;


import org.hspconsortium.platform.authentication.persona.PersonaUserInfoRepository;
import org.mitre.openid.connect.model.UserInfo;
import org.mitre.openid.connect.repository.impl.JpaUserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class LocalUserInfoRepository extends PersonaUserInfoRepository {

    @Autowired
    private JpaUserInfoRepository jpaUserInfoRepository;

    @Override
    public UserInfo getRealUserByUsername(String username) {
        UserInfo userInfo = jpaUserInfoRepository.getByUsername(username);
        return userInfo;
    }
}