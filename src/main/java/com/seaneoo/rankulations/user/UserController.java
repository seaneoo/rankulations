package com.seaneoo.rankulations.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/me")
    public ResponseEntity<User> me(Authentication authentication) {
        var user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/delete-account")
    public ResponseEntity<Object> deleteAccount(Authentication authentication) {
        var user = (User) authentication.getPrincipal();
        userRepository.delete(user);
        return ResponseEntity.status(HttpStatus.GONE)
                .build();
    }
}
