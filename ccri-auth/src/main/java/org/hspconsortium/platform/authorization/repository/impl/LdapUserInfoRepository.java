/*
 * #%L
 * Health Services Platform Consortium - HSPC Reference Impl - LDAP Auth
 * %%
 * Copyright (C) 2014 - 2015 Healthcare Services Platform Consortium
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package org.hspconsortium.platform.authorization.repository.impl;

import org.hspconsortium.platform.authentication.persona.PersonaUserInfoRepository;
import org.mitre.openid.connect.model.DefaultUserInfo;
import org.mitre.openid.connect.model.UserInfo;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.Filter;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import java.util.List;

/**
 * Looks up the user information from an LDAP template and maps the results
 * into a UserInfo object. This object is then cached.
 *
 * @author jricher
 */
public class LdapUserInfoRepository extends PersonaUserInfoRepository {

    private LdapTemplate ldapTemplate;

    public LdapTemplate getLdapTemplate() {
        return ldapTemplate;
    }

    public void setLdapTemplate(LdapTemplate ldapTemplate) {
        this.ldapTemplate = ldapTemplate;
    }

    @Override
    public UserInfo getRealUserByUsername(String username) {
        Filter find = new EqualsFilter("uid", username);
        List res = ldapTemplate.search("", find.encode(), attributesMapper);

        if (res.isEmpty()) {
            // user not found
            return null;
        } else if (res.size() == 1) {
            // exactly one user found, return them
            return (UserInfo) res.get(0);
        } else {
            // more than one user found, error
            throw new IllegalArgumentException("User not found: " + username);
        }
    }

    private AttributesMapper attributesMapper = new AttributesMapper() {
        @Override
        public Object mapFromAttributes(Attributes attr) throws NamingException {

            if (attr.get("uid") == null) {
                return null; // we can't go on if there's no UID to look up
            }

            UserInfo ui = new DefaultUserInfo();

            // save the UID as the preferred username
            ui.setPreferredUsername(attr.get("uid").get().toString());

            // for now we use the UID as the subject as well (this should probably be different)
            ui.setSub(attr.get("uid").get().toString());


            // add in the optional fields

            // email address
            if (attr.get("mail") != null) {
                ui.setEmail(attr.get("mail").get().toString());
                // if this domain also provisions email addresses, this should be set to true
                ui.setEmailVerified(false);
            }

            // phone number
            if (attr.get("telephoneNumber") != null) {
                ui.setPhoneNumber(attr.get("telephoneNumber").get().toString());
                // if this domain also provisions phone numbers, this should be set to true
                ui.setPhoneNumberVerified(false);
            }

            // name structure
            if (attr.get("displayName") != null) {
                ui.setName(attr.get("displayName").get().toString());
            }

            if (attr.get("givenName") != null) {
                ui.setGivenName(attr.get("givenName").get().toString());
            }

            if (attr.get("sn") != null) {
                ui.setFamilyName(attr.get("sn").get().toString());
            }

            if (attr.get("initials") != null) {
                ui.setMiddleName(attr.get("initials").get().toString());
            }

            if (attr.get("labeledURI") != null) {
                ui.setProfile(attr.get("labeledURI").get().toString());
            }

            if (attr.get("organizationName") != null) {
                ui.setWebsite(attr.get("organizationName").get().toString());
            }

            return ui;
        }
    };
}
