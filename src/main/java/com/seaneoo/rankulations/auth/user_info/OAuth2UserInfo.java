package com.seaneoo.rankulations.auth.user_info;

import lombok.Getter;

import java.util.Map;

@Getter
public abstract class OAuth2UserInfo {

    Map<String, Object> attributes;

    String id;

    String username;

    String profilePic;

    public OAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
}
