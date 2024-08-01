package com.seaneoo.rankulations.user.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterPayload {

    @NotBlank(message = "Username field is required.")
    @Size(min = 2, max = 20, message = "Username must be between 2 and 20 characters.")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Username can only contain letters and numbers.")
    String username;

    @NotBlank(message = "Password field is required.")
    @Size(min = 8, message = "Password must be at least 8 characters.")
    String password;

    @NotBlank(message = "Password verification field is required.")
    @JsonProperty("verify_password")
    String verifyPassword;
}
