package com.seaneoo.rankulations.admin;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChangeUserRolePayload {

    @NotBlank(message = "'role' field is required.")
    String role;
}
