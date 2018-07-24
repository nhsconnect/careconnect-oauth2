/*
 * #%L
 * hspc-auth
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

package org.smartplatforms.oauth2;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hspconsortium.platform.authorization.launchcontext.LaunchContextHolder;
import org.hspconsortium.platform.security.LaunchContext;
import org.hspconsortium.platform.service.FirebaseTokenService;
import org.mitre.openid.connect.web.CorsFilter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.common.util.RandomValueStringGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class LaunchOrchestrationReceiveEndpoint {

    @Inject
    private LaunchContextHolder launchContextHolder;

    private Log log = LogFactory.getLog(LaunchOrchestrationReceiveEndpoint.class);

    @RequestMapping(value = "/Launch", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public LaunchContext findLaunchContext(String launchContextId) {

        log.debug("KGM In findLaunchContext");
        return launchContextHolder.getLaunchContext(launchContextId);
    }

    @RequestMapping(value = "/Launch", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public void handleLaunchRequest(HttpServletRequest request, HttpServletResponse response, @RequestBody String jsonString) {
        log.info("KGM In handleLaunchRequest");
        HttpSession sessionObj = request.getSession();
        SecurityContext securityContext = (SecurityContext) sessionObj.getAttribute("SPRING_SECURITY_CONTEXT");
        Map<String, Object> jsonMap = createLaunchContext(jsonString, securityContext);

        response.setHeader("Content-Type", "application/json;charset=utf-8");
        try {
             response.getWriter().write(new Gson().toJson(jsonMap));
        } catch (IOException io_ex) {
            throw new RuntimeException(io_ex);
        }
    }

    private LaunchContext createLaunchContext(String launchId, Map<String, Object> launchContextParams) {
        LaunchContext launchContext = null;
        if (StringUtils.isNotBlank(launchId)) {
            launchContext = launchContextHolder.getLaunchContext(launchId);
        }
        if (launchContext == null) {
            launchContext = new LaunchContext();
            RandomValueStringGenerator randomValueStringGenerator = new RandomValueStringGenerator();
            launchId = randomValueStringGenerator.generate();
            launchContext.setLaunchId(launchId);
            log.info("SET lauch_id = "+launchId);
            launchContext.setLaunchContextParams(launchContextParams);
            launchContextHolder.putLaunchContext(launchId, launchContext);
        }
        return launchContext;
    }

    private Map<String, Object> buildLaunchContextParamsMap(JsonObject jsonParams) {
        Map<String, Object> launchContextParams = new HashMap<>();
        for(Map.Entry entry : jsonParams.entrySet() ) {
            if (!entry.getKey().equals("launch_id")) {

                launchContextParams.put((String)entry.getKey(), ((JsonElement)entry.getValue()).getAsString());
            }
        }
        return launchContextParams;
    }

    Map<String, Object> createLaunchContext(String jsonString, SecurityContext securityContext){
        Map<String, Object> jsonMap = new HashMap<>();
        JsonObject json = new JsonParser().parse(jsonString).getAsJsonObject();
        JsonObject jsonParams = json.get("parameters").getAsJsonObject();

        JsonElement jsonLaunchId = jsonParams.get("launch_id");
        String launchId = null;
        if (jsonLaunchId != null) {
            launchId = jsonLaunchId.getAsString();
        }

        Map<String, Object> launchContextParams = buildLaunchContextParamsMap(jsonParams);

        LaunchContext launchContext = createLaunchContext(launchId, launchContextParams);

        if (securityContext != null) {
            log.info("** Security Context wasn't null");
            Authentication authentication = securityContext.getAuthentication();
            //User user = (User) authentication.getPrincipal();
            jsonMap.put("username", authentication.getPrincipal());
        } else {  //TODO this shouldn't happen when we turn authentication back on
            log.info("** Security Context IS null");
            jsonMap.put("username", "none");
        }

        //TODO: get actual values
        jsonMap.put("created_by", "hspc_platform");
        jsonMap.put("launch_id", launchContext.getLaunchId());
        jsonMap.put("created_at", new Date().toString());
        Map<String, Object> retMap = new Gson().fromJson(json.get("parameters"), new TypeToken<HashMap<String, Object>>() {
        }.getType());
        jsonMap.put("parameters", retMap);
        return jsonMap;
    }
}
