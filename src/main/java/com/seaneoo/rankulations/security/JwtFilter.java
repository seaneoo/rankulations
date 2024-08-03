package com.seaneoo.rankulations.security;

import com.seaneoo.rankulations.auth.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private static final String HEADER = "Authorization";
    private static final String SCHEME = "Bearer ";

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @SuppressWarnings("NullableProblems")
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        var header = request.getHeader(HEADER);
        if (header == null || !header.startsWith(SCHEME)) {
            filterChain.doFilter(request, response);
            return;
        }

        var token = header.substring(SCHEME.length());
        var subject = jwtService.extractSubject(token);

        var authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        if (subject == null || authentication != null) {
            filterChain.doFilter(request, response);
            return;
        }

        var user = customUserDetailsService.loadUserByUsername(subject);
        var isTokenValid = jwtService.isValid(token, user);

        if (isTokenValid) {
            var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user, null,
                    user.getAuthorities());
            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext()
                    .setAuthentication(usernamePasswordAuthenticationToken);
        }

        filterChain.doFilter(request, response);
    }
}
