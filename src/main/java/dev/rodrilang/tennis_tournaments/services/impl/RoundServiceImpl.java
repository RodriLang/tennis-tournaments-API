package dev.rodrilang.tennis_tournaments.services.impl;

import dev.rodrilang.tennis_tournaments.dtos.response.MatchResponseDto;
import dev.rodrilang.tennis_tournaments.dtos.response.PlayerResponseDto;
import dev.rodrilang.tennis_tournaments.dtos.response.RoundResponseDto;
import dev.rodrilang.tennis_tournaments.enums.RoundType;
import dev.rodrilang.tennis_tournaments.mappers.MatchMapper;
import dev.rodrilang.tennis_tournaments.mappers.PlayerMapper;
import dev.rodrilang.tennis_tournaments.mappers.RoundMapper;
import dev.rodrilang.tennis_tournaments.models.*;
import dev.rodrilang.tennis_tournaments.services.MatchService;
import dev.rodrilang.tennis_tournaments.services.RoundService;
import dev.rodrilang.tennis_tournaments.strategy.RoundFactory;
import dev.rodrilang.tennis_tournaments.strategy.RoundStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoundServiceImpl implements RoundService {

    private final RoundMapper roundMapper;
    private final PlayerMapper playerMapper;
    private final MatchMapper matchMapper;
    private final RoundFactory matchFactory;
    private final MatchService matchService;


    @Override
    public List<PlayerResponseDto> getPlayersStillCompeting(Tournament tournament) {
        Optional<Round> lastRoundOpt = tournament.getRounds().stream()
                .max(Comparator.comparing(r -> r.getType().ordinal()));

        if (lastRoundOpt.isEmpty()) return new ArrayList<>();

        Round lastRound = lastRoundOpt.get();

        return lastRound.getMatches().stream()
                .map(matchService::getWinner)
                .map(playerMapper::toDto)
                .toList();
    }

    @Override
    public Round generateRound(Tournament tournament, RoundType roundType) {
        List<Player> players = getPlayersForRound(tournament, roundType);

        if (players.size() % 2 != 0) {
            throw new IllegalStateException("La cantidad de jugadores para la ronda " + roundType + " debe ser par.");
        }

        Round round = Round.builder()
                .type(roundType)
                .tournament(tournament)
                .givenPoints(getPointsForRound(roundType))
                .build();

        RoundStrategy strategy = matchFactory.getStrategy(roundType);
        List<Match> matches = strategy.generateMatches(players, round);
        round.setMatches(matches);

        return round;
    }

    @Override
    public List<RoundResponseDto> getRoundsByTournament(Tournament tournament) {
        return tournament.getRounds().stream()
                .map(roundMapper::toDto)
                .toList();
    }

    @Override
    public MatchResponseDto getFinalMatch(Tournament tournament) {
        return tournament.getRounds().stream()
                .filter(r -> r.getType() == RoundType.FINAL)
                .flatMap(r -> r.getMatches().stream())
                .findFirst()
                .map(matchMapper::toDto)
                .orElseThrow(() -> new IllegalStateException("No se encontró el partido final."));
    }

    // Métodos auxiliares

    private List<Player> getPlayersForRound(Tournament tournament, RoundType roundType) {
        if (roundType == RoundType.FIRST) {
            return new ArrayList<>(tournament.getPlayers());
        }

        Round previousRound = tournament.getRounds().stream()
                .filter(r -> r.getType().ordinal() == roundType.ordinal() - 1)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No se encontró la ronda anterior a " + roundType));

        return previousRound.getMatches().stream()
                .map(matchService::getWinner)
                .toList();
    }

    private int getPointsForRound(RoundType roundType) {
        return switch (roundType) {
            case FIRST -> 100;
            case QUARTER_FINAL -> 200;
            case SEMI_FINAL -> 500;
            case FINAL -> 1000;
        };
    }
}
