package org.hspconsortium.platform.web;

import com.google.gson.*;
import org.mitre.openid.connect.model.DefaultUserInfo;
import org.mitre.openid.connect.view.HttpCodeView;
import org.mitre.openid.connect.view.JsonEntityView;
import org.mitre.openid.connect.view.JsonErrorView;
import org.mitre.openid.connect.web.ClientAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequestMapping("/api/user-info")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class UserInfoApi {

    private static final Logger logger = LoggerFactory.getLogger(ClientAPI.class);

    @Autowired
    private ExtendedUserInfoService extendedUserInfoService;

    private Gson gson = new GsonBuilder()
            .serializeNulls()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
            .create();

    /**
     * Get a list of all user-info
     */
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getAllUserInfo(ModelMap m) {

        List<DefaultUserInfo> userInfo = extendedUserInfoService.getAllUserInfo();

        m.put(JsonEntityView.ENTITY, userInfo);

        return "jsonEntityView";
    }

    /**
     * Add one user's info
     */
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String addUserInfo(@RequestBody String jsonString, ModelMap m) {

        JsonParser parser = new JsonParser();
        JsonObject json = null;
        DefaultUserInfo defaultUserInfo = null;

        try {
            json = parser.parse(jsonString).getAsJsonObject();
            defaultUserInfo = gson.fromJson(json, DefaultUserInfo.class);
        } catch (JsonSyntaxException e) {
            logger.error("apiAddClient failed due to JsonSyntaxException", e);
            m.addAttribute(HttpCodeView.CODE, HttpStatus.BAD_REQUEST);
            m.addAttribute(JsonErrorView.ERROR_MESSAGE, "Could not save user info. The server encountered a JSON syntax exception. Contact a system administrator for assistance.");
            return JsonErrorView.VIEWNAME;
        } catch (IllegalStateException e) {
            logger.error("apiAddClient failed due to IllegalStateException", e);
            m.addAttribute(HttpCodeView.CODE, HttpStatus.BAD_REQUEST);
            m.addAttribute(JsonErrorView.ERROR_MESSAGE, "Could not save user info. The server encountered an IllegalStateException. Refresh and try again - if the problem persists, contact a system administrator for assistance.");
            return JsonErrorView.VIEWNAME;
        }

        // check if user already exists
        DefaultUserInfo existingUserInfo = extendedUserInfoService.getUserInfoByEmail(defaultUserInfo.getEmail());

        // if it does exist, update that record
        if (existingUserInfo != null) {
            defaultUserInfo.setId(existingUserInfo.getId());
        }

        extendedUserInfoService.addUserInfo(defaultUserInfo);

        m.put(JsonEntityView.ENTITY, defaultUserInfo);
        return "jsonEntityView";
    }
}
