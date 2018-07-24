package org.ccri.repository;

import org.ccri.model.LdapAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Component;

import javax.naming.Name;

@Component
public class LdapAccountRepository{

    @Autowired
    private final LdapTemplate ldapTemplate;

    public LdapAccountRepository(LdapTemplate ldapTemplate) {
        this.ldapTemplate = ldapTemplate;
    }

    public void createAccount(LdapAccount account){

        // cn=abowes,ou=users,dc=ccri,dc=com
        // NB - LDAP Server is connected to dc=ccri,dc=com and so elements should be relative to this base.

        Name dn = LdapNameBuilder
                .newInstance()
                .add("ou", "users")
                .add("cn", account.getUserId())
                .build();
        DirContextAdapter context = new DirContextAdapter(dn);

        context.setAttributeValues("objectclass",
                new String[]{ "top", "person", "organizationalPerson", "inetOrgPerson", "simpleSecurityObject"});
        context.setAttributeValue("cn", account.getUserId());
        context.setAttributeValue("uid", account.getUserId());
        context.setAttributeValue("givenName", account.getFirstName());
        context.setAttributeValue("sn", account.getSurname());
        context.setAttributeValue("userPassword", account.getPassword());
        context.setAttributeValue("displayName", account.getDisplayName());
        context.setAttributeValue("mail", account.getEmailAddress());

        ldapTemplate.bind(context);
    }

}
