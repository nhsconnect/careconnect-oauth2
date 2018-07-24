/*
 * #%L
 * hspc-security
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

package org.hspconsortium.platform.security.oauth.mitre;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.commons.lang.StringUtils;
import org.mitre.oauth2.introspectingfilter.service.IntrospectionAuthorityGranter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScopeBasedIntrospectionAuthorityGranter implements IntrospectionAuthorityGranter {

    private static final Map<String, String> SCOPE_TO_AUTHORITY_MAP = new HashMap<String, String>(){{
        put("patient/*.read", "SCOPE_PATIENT_DATA_READ");
        put("launch", "SCOPE_LAUNCH");
    }};

    private Logger log = LoggerFactory.getLogger(this.getClass());

    public List<GrantedAuthority> getAuthorities(JsonObject introspectionResponse) {
        JsonElement scopeElement = introspectionResponse.get("scope");
        ArrayList<String> roles = new ArrayList<String>(){{
            add("DUMMY_SCOPE");
        }};
        //Scopes are a white-space separated list
//        String[] scopes = scopeElement.getAsString().split(" ");
//        for(String scope : scopes){
//            String role = roleFromScope(scope);
//            if(StringUtils.isNotBlank(role)){
//                roles.add(roleFromScope(scope));
//            }
//        }
        return AuthorityUtils.createAuthorityList(roles.toArray(new String[roles.size()]));
    }

    private String roleFromScope(String scope){
        String role = SCOPE_TO_AUTHORITY_MAP.get(scope);
        if(StringUtils.isBlank(role)){
            log.error(String.format("No ROLE mapping for SCOPE: '%s'", scope));
        }
        return role;
    }
}
