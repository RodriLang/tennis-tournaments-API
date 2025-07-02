package dev.rodrilang.tennis_tournaments.dtos.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import dev.rodrilang.tennis_tournaments.validations.ValidFullScore;

@ValidFullScore(message = "El resultado del set no es válido según las reglas del tenis")
public record SetScoreRequestDto(
        @NotNull(message = "El puntaje de playerOne no puede ser nulo")
        @Min(value = 0, message = "El puntaje debe ser mayor o igual a 0")
        Integer playerOneScore,

        @NotNull(message = "El puntaje de playerTwo no puede ser nulo")
        @Min(value = 0, message = "El puntaje debe ser mayor o igual a 0")
        Integer playerTwoScore) {
}
