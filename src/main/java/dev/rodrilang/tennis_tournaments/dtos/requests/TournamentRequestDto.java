package dev.rodrilang.tennis_tournaments.dtos.requests;

import dev.rodrilang.tennis_tournaments.enums.SurfaceType;
import jakarta.validation.constraints.*;
import dev.rodrilang.tennis_tournaments.validations.ValidDateRange;

import java.time.LocalDate;

@ValidDateRange
public record TournamentRequestDto(

        @NotBlank(message = "El nombre del torneo es obligatorio")
        String name,

        @NotBlank(message = "La ubicación es obligatoria")
        String location,

        @NotNull(message = "La superficie es obligatoria")
        SurfaceType surface,

        @NotNull(message = "La fecha de inicio es obligatoria")
        LocalDate startingDate,

        @NotNull(message = "La fecha de finalización es obligatoria")
        LocalDate endingDate) {
}

