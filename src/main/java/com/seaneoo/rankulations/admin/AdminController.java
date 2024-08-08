package com.seaneoo.rankulations.admin;

import com.seaneoo.rankulations.error.exception.UserNotFoundException;
import com.seaneoo.rankulations.user.User;
import com.seaneoo.rankulations.user.UserRepository;
import com.seaneoo.rankulations.user.UserRole;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/users/{userId}/change-role")
    public ResponseEntity<User> changeUserRole(@PathVariable Long userId,
                                               @RequestBody @Valid ChangeUserRolePayload payload) {
        var foundUser = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        var role = UserRole.valueOf(payload.role.toUpperCase());

        foundUser.setRole(role);
        userRepository.save(foundUser);
        return ResponseEntity.ok(foundUser);
    }

    @PostMapping("/users/{userId}/enable-disable")
    public ResponseEntity<User> enableDisableUser(@PathVariable Long userId) {
        var foundUser = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        foundUser.setEnabled(!foundUser.isEnabled());
        userRepository.save(foundUser);
        return ResponseEntity.ok(foundUser);
    }
}
