package dev.rodrilang.tennis_tournaments.dtos.response;

import dev.rodrilang.tennis_tournaments.enums.StatusType;
import dev.rodrilang.tennis_tournaments.enums.SurfaceType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.Set;

@Schema(description = "DTO de respuesta con los datos básicos de un torneo para listar todos")
public record TournamentListDto(

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

        @Schema(description = "Estado actual del torneo", example = "IN_PROGRESS")
        StatusType status,

        @Schema(description = "Lista de jugadores inscriptos")
        Integer numberOfPlayers
){

}

