package org.hspconsortium.platform.authentication.persona;

public class UserPersonaDto {
    private String username;
    private String name;
    private String resourceUrl;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResourceUrl() {
        return resourceUrl;
    }

    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }

    @Override
    public String toString() {
        return "UserPersonaDto{" +
                "username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", resourceUrl='" + resourceUrl + '\'' +
                '}';
    }
}
