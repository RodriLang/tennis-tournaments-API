package dev.rodrilang.tennis_tournaments.services;

import dev.rodrilang.tennis_tournaments.models.SetScore;
import org.springframework.stereotype.Service;

@Service
public interface SetScoreValidator {

    Boolean isValidSetScore(Integer p1, Integer p2);

    Boolean isValidPartialScore(Integer score);

    void validateSetScore(SetScore score);

}
