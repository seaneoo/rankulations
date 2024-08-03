package com.seaneoo.rankulations.auth.user_info;

import com.seaneoo.rankulations.auth.AuthProvider;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo get(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        var registrationId = userRequest.getClientRegistration()
                .getRegistrationId();
        var authProvider = AuthProvider.valueOf(registrationId.toUpperCase());

        return switch (authProvider) {
            case DISCORD -> new DiscordOAuth2UserInfo(oAuth2User.getAttributes());
        };
    }
}
