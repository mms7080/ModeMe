package com.example.Modeme.Config;

import java.util.Map;

public class OAuth2UserInfo {
    private final Map<String, Object> attributes;
    private final String provider;

    public OAuth2UserInfo(Map<String, Object> attributes, String provider) {
        this.attributes = attributes;
        this.provider = provider;
    }

    public String getId() {
        return (String) attributes.get("id");
    }

    public String getName() {
        return (String) attributes.get("name");
    }

    public String getEmail() {
        return (String) attributes.get("email");
    }

    public String getProvider() {
        return provider;
    }
}
