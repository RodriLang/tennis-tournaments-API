package dev.rodrilang.tennis_tournaments.strategies;

import dev.rodrilang.tennis_tournaments.models.Match;
import dev.rodrilang.tennis_tournaments.models.Player;

import java.util.List;

public interface RoundStrategy {
    List<Match> generateMatches(List<Player> players);
}
