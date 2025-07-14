package dev.rodrilang.tennis_tournaments.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "DTO de respuesta con los datos de un jugador")
public record PlayerResponseDto(

        @Schema(description = "DNI del jugador", example = "12345678")
        String dni,

        @Schema(description = "Nombre del jugador", example = "Juan")
        String name,

        @Schema(description = "Apellido del jugador", example = "Pérez")
        String lastName,

        @Schema(description = "Puntaje acumulado del jugador", example = "1500")
        Integer score,

        @Schema(description = "Fecha de nacimiento", example = "1995-06-15")
        LocalDate dateOfBirth,

        @Schema(description = "Nacionalidad del jugador", example = "Argentina")
        String nationality
) {}
