package org.hspconsortium.platform.web;

import org.mitre.openid.connect.model.DefaultUserInfo;
import org.mitre.openid.connect.model.UserInfo;
import org.mitre.util.jpa.JpaUtil;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

import static org.mitre.util.jpa.JpaUtil.getSingleResult;

@Repository
@Transactional(value = "defaultTransactionManager")
public class ExtendedUserInfoService {

    @PersistenceContext(unitName = "defaultPersistenceUnit")
    private EntityManager manager;

    @SuppressWarnings("JpaQlInspection")
    public List<DefaultUserInfo> getAllUserInfo() {
        TypedQuery<DefaultUserInfo> query = manager.createQuery("select u from DefaultUserInfo u", DefaultUserInfo.class);
        return query.getResultList();
    }

    public DefaultUserInfo getUserInfoByEmail(String email) {
        TypedQuery<DefaultUserInfo> query = manager.createNamedQuery(DefaultUserInfo.QUERY_BY_EMAIL, DefaultUserInfo.class);
        query.setParameter(DefaultUserInfo.PARAM_EMAIL, email);

        return getSingleResult(query.getResultList());
    }

    public DefaultUserInfo addUserInfo(DefaultUserInfo defaultUserInfo) {
        return JpaUtil.saveOrUpdate(defaultUserInfo.getId(), manager, defaultUserInfo);
    }
}
