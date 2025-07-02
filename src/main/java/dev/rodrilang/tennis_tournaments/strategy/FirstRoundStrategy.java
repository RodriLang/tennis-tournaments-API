package dev.rodrilang.tennis_tournaments.strategy;

import dev.rodrilang.tennis_tournaments.enums.RoundType;
import org.springframework.stereotype.Component;

@Component
public class FirstRoundStrategy extends AbstractRoundStrategy {

    @Override
    public RoundType getRoundType() {
        return RoundType.FIRST;
    }
}
