package dev.rodrilang.tennis_tournaments.strategy;

import dev.rodrilang.tennis_tournaments.models.Match;
import dev.rodrilang.tennis_tournaments.models.Player;
import dev.rodrilang.tennis_tournaments.models.Round;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractRoundStrategy implements RoundStrategy {

    @Override
    public List<Match> generateMatches(List<Player> players, Round round) {
        List<Match> matches = new ArrayList<>();
        for (int i = 0; i < players.size(); i += 2) {
            matches.add(Match.builder()
                    .playerOne(players.get(i))
                    .playerTwo(players.get(i + 1))
                    .round(round)
                    .build());
        }
        return matches;
    }
}
