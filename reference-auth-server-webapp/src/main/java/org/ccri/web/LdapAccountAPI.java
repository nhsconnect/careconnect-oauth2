package org.ccri.web;


import com.google.gson.*;
import org.ccri.model.LdapAccount;
import org.mitre.openid.connect.view.HttpCodeView;
import org.mitre.openid.connect.view.JsonEntityView;
import org.mitre.openid.connect.view.JsonErrorView;
import org.mitre.openid.connect.web.RootController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/" + LdapAccountAPI.URL)
public class LdapAccountAPI {

    private JsonParser parser = new JsonParser();

    private Gson gson = new GsonBuilder()
            .serializeNulls()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
            .create();

    public static final String URL = RootController.API_URL + "/account";

    /**
     * Logger for this class
     */
    private static final Logger logger = LoggerFactory.getLogger(LdapAccountAPI.class);

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String apiLdapAccount(@RequestBody String jsonString, Model m, Authentication auth) {

        JsonObject json = null;
        LdapAccount ldapAccount = null;

        try {
            json = parser.parse(jsonString).getAsJsonObject();
            ldapAccount = gson.fromJson(json, LdapAccount.class);
        }
        catch (JsonSyntaxException e) {
            logger.error("apiAddLdapAccount failed due to JsonSyntaxException", e);
            m.addAttribute(HttpCodeView.CODE, HttpStatus.BAD_REQUEST);
            m.addAttribute(JsonErrorView.ERROR_MESSAGE, "Could not save new account. The server encountered a JSON syntax exception. Contact a system administrator for assistance.");
            return JsonErrorView.VIEWNAME;
        } catch (IllegalStateException e) {
            logger.error("apiAddLdapAccount failed due to IllegalStateException", e);
            m.addAttribute(HttpCodeView.CODE, HttpStatus.BAD_REQUEST);
            m.addAttribute(JsonErrorView.ERROR_MESSAGE, "Could not save new account. The server encountered an IllegalStateException. Refresh and try again - if the problem persists, contact a system administrator for assistance.");
            return JsonErrorView.VIEWNAME;
        }

        return JsonEntityView.VIEWNAME;
    }



}
