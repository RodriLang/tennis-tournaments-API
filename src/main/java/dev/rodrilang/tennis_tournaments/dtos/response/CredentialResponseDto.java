package dev.rodrilang.tennis_tournaments.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta luego del registro de credencial")
public record CredentialResponseDto(

        @Schema(description = "Nombre de usuario registrado", example = "admin123")
        String username,

        @Schema(description = "Rol asignado", example = "ADMIN")
        String role
) {}
