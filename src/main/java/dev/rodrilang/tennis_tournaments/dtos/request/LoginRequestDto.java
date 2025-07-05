package dev.rodrilang.tennis_tournaments.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "DTO para login de usuario")
public record LoginRequestDto(

        @Schema(description = "Nombre de usuario", example = "admin123")
        @NotBlank
        String username,

        @Schema(description = "Contraseña del usuario", example = "securePassword")
        @NotBlank
        String password
) {
}
