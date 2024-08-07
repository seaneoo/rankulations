package com.seaneoo.rankulations.admin;

import com.seaneoo.rankulations.error.exception.UserNotFoundException;
import com.seaneoo.rankulations.user.User;
import com.seaneoo.rankulations.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/_internal/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users")
    public ResponseEntity<List<User>> userById() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<User> allUsers(@PathVariable Long userId) {
        var user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        return ResponseEntity.ok(user);
    }
}
