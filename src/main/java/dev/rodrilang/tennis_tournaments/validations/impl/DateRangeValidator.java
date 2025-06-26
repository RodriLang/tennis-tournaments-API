package dev.rodrilang.tennis_tournaments.validations.impl;

import dev.rodrilang.tennis_tournaments.dtos.requests.TournamentRequestDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import dev.rodrilang.tennis_tournaments.validations.ValidDateRange;

import java.time.LocalDate;

public class DateRangeValidator implements ConstraintValidator<ValidDateRange, TournamentRequestDto> {

    @Override
    public boolean isValid(TournamentRequestDto dto, ConstraintValidatorContext context) {
        LocalDate start = dto.startingDate();
        LocalDate end = dto.endingDate();

        // Evita validar si alguna es null (NotNull lo valida por separado)
        if (start == null || end == null) return true;

        return !end.isBefore(start);
    }
}
