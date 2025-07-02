package dev.rodrilang.tennis_tournaments.services.impl;

import dev.rodrilang.tennis_tournaments.models.SetScore;
import dev.rodrilang.tennis_tournaments.exceptions.InvalidResultException;
import org.springframework.stereotype.Service;
import dev.rodrilang.tennis_tournaments.services.SetScoreValidator;
import dev.rodrilang.tennis_tournaments.utils.Utils;

@Service
public class SetScoreValidatorImpl implements SetScoreValidator {

    @Override
    public Boolean isValidSetScore(Integer p1, Integer p2) {
        return Utils.validateFullScore(p1, p2);
    }

    @Override
    public Boolean isValidPartialScore(Integer score) {
        return Utils.validatePartialScore(score);
    }

    @Override
    public void validateSetScore(SetScore score) throws InvalidResultException {
        if (Boolean.FALSE.equals(isValidSetScore(score.getPlayerOneScore(), score.getPlayerTwoScore()))) {
            throw new InvalidResultException("Invalid set score.");
        }
    }

}
