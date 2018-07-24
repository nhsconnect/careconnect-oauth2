package org.hspconsortium.platform.config;

import org.mitre.openid.connect.config.ConfigurationPropertiesBean;

public class HspcConfigurationPropertiesBean extends ConfigurationPropertiesBean {
    private String newUserUrl;
    private String forgotPasswordUrl;
    private String personaAuthUrl;

    public String getNewUserUrl() {
        return newUserUrl;
    }

    public void setNewUserUrl(String newUserUrl) {
        this.newUserUrl = newUserUrl;
    }

    public String getForgotPasswordUrl() {
        return forgotPasswordUrl;
    }

    public void setForgotPasswordUrl(String forgotPasswordUrl) {
        this.forgotPasswordUrl = forgotPasswordUrl;
    }

    public String getPersonaAuthUrl() {
        return personaAuthUrl;
    }

    public void setPersonaAuthUrl(String personaAuthUrl) {
        this.personaAuthUrl = personaAuthUrl;
    }
}
