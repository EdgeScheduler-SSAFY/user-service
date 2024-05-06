package com.ssafy.userservice.security.oauth2;

import java.util.Map;
import lombok.ToString;

@ToString
public class AzureOAuth2UserInfo implements Oauth2UserInfo{
    private Map<String, Object> attributes;

    public AzureOAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProviderId() {
        return (String) attributes.get("sub");
    }

    @Override
    public String getProvider() {
        return "azure";
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }
}
