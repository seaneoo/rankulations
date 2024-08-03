package com.seaneoo.rankulations.security;

import com.seaneoo.rankulations.auth.CustomAuthSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableMethodSecurity
public class SecurityFilter {

    @Autowired
    private CustomAuthSuccessHandler customAuthSuccessHandler;

    @Autowired
    private JwtFilter jwtFilter;

    // Any routes that do not require authentication to go to
    private static final List<String> PERMITTED_MATCHERS = List.of("favicon.ico", "/h2-console/**", "/error/**",
            "/oauth2/**");

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        //noinspection CodeBlock2Expr
        return httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> {
                    headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable);
                })
                .authorizeHttpRequests(matcherRegistry -> {
                    matcherRegistry.requestMatchers(PERMITTED_MATCHERS.toArray(new String[0]))
                            .permitAll()
                            .anyRequest()
                            .authenticated();
                })
                .oauth2Login(oauth -> {
                    oauth.successHandler(customAuthSuccessHandler);
                })
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> {
                    session.sessionCreationPolicy(STATELESS);
                })
                .exceptionHandling(exceptionHandling -> {
                    exceptionHandling.authenticationEntryPoint(new Http403ForbiddenEntryPoint());
                })
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
