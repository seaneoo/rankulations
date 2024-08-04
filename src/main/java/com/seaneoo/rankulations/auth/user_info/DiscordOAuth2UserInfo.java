package com.seaneoo.rankulations.auth.user_info;

import org.springframework.web.util.UriComponentsBuilder;

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
        var avatarId = attributes.get("avatar");
        if (avatarId == null) return null;
        return UriComponentsBuilder.fromUriString("https://cdn.discordapp.com/avatars")
                .pathSegment(getId(), avatarId.toString())
                .queryParam("size", "512")
                .build()
                .toUriString();
    }
}
