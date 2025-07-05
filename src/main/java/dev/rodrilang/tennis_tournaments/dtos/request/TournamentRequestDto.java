package dev.rodrilang.tennis_tournaments.dtos.request;

import dev.rodrilang.tennis_tournaments.enums.SurfaceType;
import dev.rodrilang.tennis_tournaments.validations.ValidDateRange;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@ValidDateRange
@Schema(description = "DTO para la creación o actualización de un torneo")
public record TournamentRequestDto(

        @NotBlank(message = "El nombre del torneo es obligatorio")
        @Schema(description = "Nombre del torneo", example = "Copa Primavera")
        String name,

        @NotBlank(message = "La ubicación es obligatoria")
        @Schema(description = "Ubicación del torneo", example = "Buenos Aires")
        String location,

        @NotNull(message = "La superficie es obligatoria")
        @Schema(description = "Tipo de superficie del torneo", example = "CLAY")
        SurfaceType surface,

        @NotNull(message = "La fecha de inicio es obligatoria")
        @Schema(description = "Fecha de inicio del torneo", example = "2025-10-01")
        LocalDate startingDate,

        @NotNull(message = "La fecha de finalización es obligatoria")
        @Schema(description = "Fecha de finalización del torneo", example = "2025-10-07")
        LocalDate endingDate
) {}
