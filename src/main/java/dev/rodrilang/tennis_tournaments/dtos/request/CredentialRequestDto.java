package dev.rodrilang.tennis_tournaments.dtos.request;

import dev.rodrilang.tennis_tournaments.enums.RoleType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "DTO para registrar un nuevo usuario")
public record CredentialRequestDto(

        @Schema(description = "Nombre de usuario", example = "admin123")
        @NotBlank
        String username,

        @Schema(description = "Contraseña del usuario", example = "securePassword")
        @NotBlank
        String password,

        @Schema(description = "Rol del usuario", example = "ADMIN")
        @NotBlank
        RoleType role
) {
}
