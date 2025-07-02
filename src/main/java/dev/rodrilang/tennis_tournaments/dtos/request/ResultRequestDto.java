package dev.rodrilang.tennis_tournaments.dtos.request;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import dev.rodrilang.tennis_tournaments.validations.ValidResultSetCount;

import java.util.List;

@ValidResultSetCount
public record ResultRequestDto(

        @NotNull(message = "La lista de sets no puede ser nula")
        @NotEmpty(message = "Debe haber al menos un set jugado")
        @Valid
        List<SetScoreRequestDto> setsScore

) {}
