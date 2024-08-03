package com.seaneoo.rankulations.security;

import com.seaneoo.rankulations.security.user.CustomAuthSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
public class SecurityFilter {

    @Autowired
    private CustomAuthSuccessHandler customAuthSuccessHandler;

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        //noinspection CodeBlock2Expr
        return httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> {
                    headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable);
                })
                .authorizeHttpRequests(httpRequests -> {
                    httpRequests.requestMatchers("favicon.ico", "/error/**", "/h2-console/**", "/oauth2/**",
                                    "/login/**", "/logout/**")
                            .permitAll()
                            .anyRequest()
                            .authenticated();
                })
                .oauth2Login(oauth -> oauth.successHandler(customAuthSuccessHandler))
                .formLogin(FormLoginConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .exceptionHandling(exceptionHandling -> exceptionHandling.authenticationEntryPoint(
                        new Http403ForbiddenEntryPoint()))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
