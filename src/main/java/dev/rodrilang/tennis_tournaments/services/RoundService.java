package dev.rodrilang.tennis_tournaments.services;

import dev.rodrilang.tennis_tournaments.dtos.response.MatchResponseDto;
import dev.rodrilang.tennis_tournaments.models.Player;
import dev.rodrilang.tennis_tournaments.models.Tournament;
import dev.rodrilang.tennis_tournaments.models.Round;

import java.util.Set;

public interface RoundService {

    Round generateNextRound(Round currentRound);

    Round generateFirstRound(Set<Player> players);

    void assignPoints(Round round);

    MatchResponseDto getFinalMatch(Tournament tournament);
}
