package dev.rodrilang.tennis_tournaments.strategy;

import dev.rodrilang.tennis_tournaments.enums.RoundType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RoundFactory {

    private final Map<RoundType, RoundStrategy> strategies;

    public RoundFactory(List<RoundStrategy> strategyList) {
        this.strategies = strategyList.stream()
                .collect(Collectors.toMap(RoundStrategy::getRoundType, s -> s));
    }

    public RoundStrategy getStrategy(RoundType roundType) {
        return strategies.get(roundType);
    }
}
