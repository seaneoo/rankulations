package com.seaneoo.rankulations.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityFilter {

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
                .oauth2Login(Customizer.withDefaults())
                .formLogin(FormLoginConfigurer::disable)
                .build();
    }
}
