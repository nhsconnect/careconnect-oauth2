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
package org.hspconsortium.platform.authorization.userdetails;

import com.google.common.collect.ImmutableList;
import org.hspconsortium.platform.authorization.rowmapper.HSPCAuthResultSetExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Mapps all users to ROLE_USER, additionally adds ROLE_ADMIN to anybody whose username
 * is in the configured admins property.
 *
 *
 */
public class MappedLdapAuthoritiesPopulator implements LdapAuthoritiesPopulator {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Collection<? extends GrantedAuthority> getGrantedAuthorities(DirContextOperations userData, String username) {
        List<? extends GrantedAuthority> authorities = null;
        String quString = "SELECT * from authorities where username = ?";
        authorities = jdbcTemplate.query(quString, new String[] { username },
                new HSPCAuthResultSetExtractor());
        if (authorities == null || authorities.isEmpty()) {
            List<GrantedAuthority> auths = new ArrayList<GrantedAuthority>();
            ;
            GrantedAuthority a = new SimpleGrantedAuthority("ROLE_USER");
            auths.add(a);
            authorities = auths;

        }
        return ImmutableList.copyOf(authorities);

    }

}
