package com.seaneoo.rankulations.user.auth;

import com.seaneoo.rankulations.user.User;
import com.seaneoo.rankulations.user.model.AuthenticatePayload;
import com.seaneoo.rankulations.user.model.RegisterPayload;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping(value = "/register", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<User> register(@RequestBody @Valid RegisterPayload registerPayload) {
        var registeredUser = authService.register(registerPayload);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(registeredUser);
    }

    @PostMapping(value = "/authenticate", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> authenticate(@RequestBody @Valid AuthenticatePayload authenticatePayload) {
        var token = authService.authenticate(authenticatePayload);
        return ResponseEntity.ok(token);
    }
}
