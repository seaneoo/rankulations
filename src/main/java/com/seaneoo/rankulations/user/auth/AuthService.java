package com.seaneoo.rankulations.user.auth;

import com.seaneoo.rankulations.error.exception.MismatchedPasswordsException;
import com.seaneoo.rankulations.error.exception.UserNotFoundException;
import com.seaneoo.rankulations.error.exception.UsernameTakenException;
import com.seaneoo.rankulations.security.JwtService;
import com.seaneoo.rankulations.user.User;
import com.seaneoo.rankulations.user.UserRepository;
import com.seaneoo.rankulations.user.model.AuthenticatePayload;
import com.seaneoo.rankulations.user.model.RegisterPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    public User register(RegisterPayload payload) {
        if (!payload.getPassword()
                .equals(payload.getVerifyPassword())) {
            throw new MismatchedPasswordsException();
        }

        var hashedPassword = passwordEncoder.encode(payload.getPassword());
        var user = User.builder()
                .username(payload.getUsername()
                        .toLowerCase())
                .password(hashedPassword)
                .build();

        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new UsernameTakenException();
        }
    }

    public String authenticate(AuthenticatePayload payload) {
        var userOptional = userRepository.findByUsername(payload.getUsername()
                .toLowerCase());
        if (userOptional.isEmpty()) throw new UserNotFoundException();

        var auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userOptional.get()
                .getUsername(), payload.getPassword()));
        var user = (User) auth.getPrincipal();
        return jwtService.generateToken(user);
    }
}
