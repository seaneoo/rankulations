package com.seaneoo.rankulations.auth;

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
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

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

        LOGGER.info("Handling authentication for user \"{}\"", user.getUsername());

        var jwt = jwtService.generateToken(user);
        var redirectUrl = UriComponentsBuilder.fromUriString("http://localhost:3000/auth")
                .queryParam("token", jwt)
                .build()
                .toUriString();

        try {
            response.sendRedirect(redirectUrl);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
