package dev.rodrilang.tennis_tournaments.validations.impl;

import dev.rodrilang.tennis_tournaments.dtos.requests.ResultRequestDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import dev.rodrilang.tennis_tournaments.validations.ValidResultSetCount;

public class ResultSetCountValidator implements ConstraintValidator<ValidResultSetCount, ResultRequestDto> {

    @Override
    public boolean isValid(ResultRequestDto dto, ConstraintValidatorContext context) {
        if (dto == null || dto.setsScore() == null) return true;

        return dto.setsScore().size() <= 3;
    }
}
