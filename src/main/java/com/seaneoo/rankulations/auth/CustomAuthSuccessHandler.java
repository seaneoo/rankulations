package com.seaneoo.rankulations.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seaneoo.rankulations.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;

@Component
public class CustomAuthSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtService jwtService;

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomAuthSuccessHandler.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) {
        var oAuth2User = (OAuth2User) authentication.getPrincipal();
        var user = customUserDetailsService.loadUserByUsername(oAuth2User.getName());
        var token = jwtService.generateToken(user);

        var responseBody = new HashMap<String, String>();
        responseBody.put("token", token);
        response.setContentType("application/json");

        try {
            response.getWriter()
                    .write(new ObjectMapper().writeValueAsString(responseBody));
        } catch (IOException e) {
            LOGGER.error("Error handling authentication success for user \"{}\": {}", user.getUsername(),
                    e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
