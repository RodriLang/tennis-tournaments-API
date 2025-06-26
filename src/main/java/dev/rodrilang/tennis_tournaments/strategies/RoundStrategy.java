package dev.rodrilang.tennis_tournaments.strategies;

import dev.rodrilang.tennis_tournaments.entities.Match;
import dev.rodrilang.tennis_tournaments.entities.Player;

import java.util.List;

public interface RoundStrategy {
    List<Match> generateMatches(List<Player> players);
}
