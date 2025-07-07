package dev.rodrilang.tennis_tournaments.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ChangePasswordRequest(

        @Email(message = "Invalid email format")
        String username,

        @NotBlank(message = "Old Password is required")
        String oldPassword,

        @NotBlank(message = "New Password is required")
        String newPassword) {

}
