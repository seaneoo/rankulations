package com.seaneoo.rankulations.user.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthenticatePayload {

    @NotBlank(message = "Username field is required.")
    String username;

    @NotBlank(message = "Password field is required.")
    String password;
}
