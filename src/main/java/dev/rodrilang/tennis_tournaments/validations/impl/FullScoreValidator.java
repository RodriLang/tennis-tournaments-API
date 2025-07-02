package dev.rodrilang.tennis_tournaments.validations.impl;

import dev.rodrilang.tennis_tournaments.dtos.request.SetScoreRequestDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import dev.rodrilang.tennis_tournaments.validations.ValidFullScore;

public class FullScoreValidator implements ConstraintValidator<ValidFullScore, SetScoreRequestDto> {

    @Override
    public boolean isValid(SetScoreRequestDto setScore, ConstraintValidatorContext context) {
        if (setScore == null) return true; // @NotNull debe validar esto

        Integer playerOneScore = setScore.playerOneScore();
        Integer playerTwoScore = setScore.playerTwoScore();

        if (playerOneScore == null || playerTwoScore == null) return true; // otros validadores se encargan

        if (playerOneScore < 0 || playerTwoScore < 0) {
            return false;
        }

        if (playerOneScore.equals(playerTwoScore)) {
            return false;
        }

        return withoutTieBreak(playerOneScore, playerTwoScore);
    }

    public static boolean withoutTieBreak(Integer playerOneScore, Integer playerTwoScore) {
        boolean playerOneWinsWithSix = playerOneScore == 6 && playerTwoScore <= 4;
        boolean playerTwoWinsWithSix = playerTwoScore == 6 && playerOneScore <= 4;

        boolean playerOneWinsWithSeven = playerOneScore == 7 && (playerTwoScore == 5 || playerTwoScore == 6);
        boolean playerTwoWinsWithSeven = playerTwoScore == 7 && (playerOneScore == 5 || playerOneScore == 6);

        return playerOneWinsWithSix || playerTwoWinsWithSix || playerOneWinsWithSeven || playerTwoWinsWithSeven;
    }
}