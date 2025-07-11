package dev.rodrilang.tennis_tournaments.services.impl;

import dev.rodrilang.tennis_tournaments.dtos.response.MatchResponseDto;
import dev.rodrilang.tennis_tournaments.enums.RoundType;
import dev.rodrilang.tennis_tournaments.exceptions.InvalidTournamentStatusException;
import dev.rodrilang.tennis_tournaments.mappers.MatchMapper;
import dev.rodrilang.tennis_tournaments.models.*;
import dev.rodrilang.tennis_tournaments.services.MatchService;
import dev.rodrilang.tennis_tournaments.services.RoundService;
import dev.rodrilang.tennis_tournaments.strategy.RoundFactory;
import dev.rodrilang.tennis_tournaments.strategy.RoundStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RoundServiceImpl implements RoundService {

    private final MatchMapper matchMapper;
    private final RoundFactory matchFactory;
    private final MatchService matchService;


    @Override
    public Round generateFirstRound(Set<Player> players) {

        Round round = Round.builder()
                .type(RoundType.FIRST)
                //.tournament(tournament)
                .givenPoints(getPointsForRound(RoundType.FIRST))
                .build();

        RoundStrategy strategy = matchFactory.getStrategy(RoundType.FIRST);
        List<Match> matches = strategy.generateMatches(players.stream().toList(), round);
        round.setMatches(matches);

        return round;
    }


    @Override
    public Round generateNextRound(Round currentRound) {

        RoundType roundType = currentRound.getType();

        if (roundType == RoundType.FINAL) {
            throw new InvalidTournamentStatusException("Cannot generate next round after FINAL");
        }

        validateAllMatchesCompleted(currentRound);
        assignPoints(currentRound);

        RoundType nextRoundType = roundType.next();
        List<Player> playersStillCompeting = getPlayersForRound(currentRound);

        Round round = Round.builder()
                .type(nextRoundType)
                .givenPoints(getPointsForRound(Objects.requireNonNull(nextRoundType)))
                .build();

        RoundStrategy strategy = matchFactory.getStrategy(nextRoundType);
        List<Match> matches = strategy.generateMatches(playersStillCompeting, round);
        round.setMatches(matches);
        return round;
    }


    @Override
    public void assignPoints(Round round) {
        round.getMatches()
                .stream()
                .map(matchService::getWinner)
                .forEach(winner -> winner.setScore(getPointsForRound(round.getType())));
    }


    @Override
    public MatchResponseDto getFinalMatch(Tournament tournament) {
        return tournament.getRounds().stream()
                .filter(r -> r.getType() == RoundType.FINAL)
                .flatMap(r -> r.getMatches().stream())
                .findFirst()
                .map(matchMapper::toDto)
                .orElseThrow(() -> new IllegalStateException("Final round not found."));
    }


    // Helper methods
    //Returns a list of players who advance to the next round
    private List<Player> getPlayersForRound(Round currentRound) {
        return currentRound.getMatches()
                .stream()
                .map(matchService::getWinner)
                .toList();
    }

    private int getPointsForRound(RoundType roundType) {
        return switch (roundType) {
            case FIRST, QUARTER_FINAL -> 360;
            case SEMI_FINAL -> 480;
            case FINAL -> 800;
        };
    }

    private void validateAllMatchesCompleted(Round currentRound) {

        Optional<Match> incompleteMatch = currentRound.getMatches()
                .stream()
                .filter(match -> !matchService.thereIsAWinner(match))
                .findFirst();

        if (incompleteMatch.isPresent()) {
            throw new InvalidTournamentStatusException(
                    "Match ID " + incompleteMatch.get().getId() + " is not yet completed.");
        }

    }

}
