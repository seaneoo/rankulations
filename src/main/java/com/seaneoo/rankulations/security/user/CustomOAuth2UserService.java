package com.seaneoo.rankulations.security.user;

import com.seaneoo.rankulations.user.AuthProvider;
import com.seaneoo.rankulations.user.User;
import com.seaneoo.rankulations.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomOAuth2UserService.class);

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        var oAuth2User = super.loadUser(userRequest);
        var userInfo = new RedditOAuth2UserInfo(oAuth2User.getAttributes());

        var userOptional = userRepository.findByProviderId(userInfo.getId());
        if (userOptional.isPresent()) {
            var user = updateUser(userOptional.get(), userInfo);
            LOGGER.info("Updated existing user \"{}\" (provider={})", user.getUsername(), user.getAuthProvider()
                    .name());
        } else {
            var user = registerUser(userRequest, userInfo);
            LOGGER.info("Registered new user \"{}\" (provider={})", user.getUsername(), user.getAuthProvider()
                    .name());
        }

        return oAuth2User;
    }

    private User registerUser(OAuth2UserRequest userRequest, OAuth2UserInfo userInfo) {
        var newUser = User.builder()
                .username(userInfo.getUsername())
                .profilePic(userInfo.getProfilePic())
                .authProvider(AuthProvider.valueOf(userRequest.getClientRegistration()
                        .getRegistrationId()
                        .toUpperCase()))
                .providerId(userInfo.getId())
                .build();
        return userRepository.save(newUser);
    }

    private User updateUser(User existingUser, OAuth2UserInfo userInfo) {
        existingUser.setUsername(userInfo.getUsername());
        existingUser.setProfilePic(userInfo.getProfilePic());
        return userRepository.save(existingUser);
    }
}
