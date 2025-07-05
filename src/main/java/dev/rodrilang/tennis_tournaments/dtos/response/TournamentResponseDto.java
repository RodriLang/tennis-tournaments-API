package dev.rodrilang.tennis_tournaments.dtos.response;

import dev.rodrilang.tennis_tournaments.enums.SurfaceType;
import dev.rodrilang.tennis_tournaments.enums.StatusType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Schema(description = "DTO de respuesta con los datos de un torneo")
public record TournamentResponseDto(

        @Schema(description = "ID del torneo", example = "1")
        Long id,

        @Schema(description = "Nombre del torneo", example = "Copa Primavera")
        String name,

        @Schema(description = "Ubicación del torneo", example = "Buenos Aires")
        String location,

        @Schema(description = "Superficie del torneo", example = "CLAY")
        SurfaceType surface,

        @Schema(description = "Fecha de inicio", example = "2025-10-01")
        LocalDate startingDate,

        @Schema(description = "Fecha de fin", example = "2025-10-07")
        LocalDate endingDate,

        @Schema(description = "Estado actual del torneo", example = "IN_PROGRESS")
        StatusType status,

        @Schema(description = "Lista de jugadores inscriptos")
        Set<PlayerResponseDto> players,

        @Schema(description = "Lista de rondas del torneo")
        List<RoundResponseDto> rounds
) {}
