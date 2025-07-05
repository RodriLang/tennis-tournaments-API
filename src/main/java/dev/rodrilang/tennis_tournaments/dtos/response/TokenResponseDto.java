package dev.rodrilang.tennis_tournaments.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta que contiene el token JWT")
public record TokenResponseDto(

        @Schema(description = "Token JWT generado", example = "eyJhbGciOiJIUzI1NiIsIn...")
        String token
) {}
