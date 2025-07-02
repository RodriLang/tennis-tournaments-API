package dev.rodrilang.tennis_tournaments.strategy;

import dev.rodrilang.tennis_tournaments.enums.RoundType;
import dev.rodrilang.tennis_tournaments.models.Match;
import dev.rodrilang.tennis_tournaments.models.Player;
import dev.rodrilang.tennis_tournaments.models.Round;

import java.util.List;

public interface RoundStrategy {
    RoundType getRoundType();
    List<Match> generateMatches(List<Player> players, Round round);
}
