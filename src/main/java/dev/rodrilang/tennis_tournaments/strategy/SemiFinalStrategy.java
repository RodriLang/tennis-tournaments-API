package dev.rodrilang.tennis_tournaments.strategy;

import dev.rodrilang.tennis_tournaments.models.Match;
import dev.rodrilang.tennis_tournaments.models.Player;
import dev.rodrilang.tennis_tournaments.models.rounds.Round;
import dev.rodrilang.tennis_tournaments.enums.RoundType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FirstRoundStrategy implements RoundStrategy {

    @Override
    public RoundType getRoundType() {
        return RoundType.FIRST;
    }

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
