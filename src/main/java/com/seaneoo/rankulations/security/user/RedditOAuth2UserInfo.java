package com.seaneoo.rankulations.security.user;

import java.util.Map;

public class RedditOAuth2UserInfo extends OAuth2UserInfo {

    public RedditOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return attributes.get("id")
                .toString();
    }

    @Override
    public String getUsername() {
        return attributes.get("name")
                .toString();
    }

    @Override
    public String getProfilePic() {
        return attributes.get("icon_img")
                .toString();
    }
}
