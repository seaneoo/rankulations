package com.seaneoo.rankulations.auth.user_info;

import java.util.Map;

public class DiscordOAuth2UserInfo extends OAuth2UserInfo {

    public DiscordOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return attributes.get("id")
                .toString();
    }

    @Override
    public String getUsername() {
        return attributes.get("username")
                .toString();
    }

    @Override
    public String getProfilePic() {
        var avatar = attributes.get("avatar");
        return avatar != null ? avatar.toString() : null;
    }
}
