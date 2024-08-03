package com.seaneoo.rankulations.security.user;

import com.seaneoo.rankulations.error.exception.UserNotFoundException;
import com.seaneoo.rankulations.user.User;
import com.seaneoo.rankulations.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public User loadUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);
    }

    @Transactional
    public User loadUserByProviderId(String providerId) {
        return userRepository.findByProviderId(providerId)
                .orElseThrow(UserNotFoundException::new);
    }
}
