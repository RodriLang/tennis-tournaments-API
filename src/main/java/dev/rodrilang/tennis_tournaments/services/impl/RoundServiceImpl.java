package dev.rodrilang.tennis_tournaments.services.impl;

import dev.rodrilang.tennis_tournaments.dtos.response.MatchResponseDto;
import dev.rodrilang.tennis_tournaments.dtos.response.PlayerResponseDto;
import dev.rodrilang.tennis_tournaments.dtos.response.RoundResponseDto;
import dev.rodrilang.tennis_tournaments.enums.RoundType;
import dev.rodrilang.tennis_tournaments.exceptions.IncompleteMatchException;
import dev.rodrilang.tennis_tournaments.mappers.MatchMapper;
import dev.rodrilang.tennis_tournaments.mappers.PlayerMapper;
import dev.rodrilang.tennis_tournaments.mappers.RoundMapper;
import dev.rodrilang.tennis_tournaments.models.*;
import dev.rodrilang.tennis_tournaments.services.ResultService;
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
    private final ResultService resultService;


    private boolean isRoundComplete(Tournament tournament, RoundType roundType) {
        return tournament.getRounds().stream()
                .filter(r -> r.getType() == roundType)
                .findFirst()
                .map(round -> round.getMatches().stream().allMatch(m -> m.getResult() != null))
                .orElse(false);
    }

    @Override
    public List<PlayerResponseDto> getPlayersStillCompeting(Tournament tournament) {
        Optional<Round> lastRoundOpt = tournament.getRounds().stream()
                .max(Comparator.comparing(r -> r.getType().ordinal()));

        if (lastRoundOpt.isEmpty()) return new ArrayList<>();

        Round lastRound = lastRoundOpt.get();

        return lastRound.getMatches().stream()
                .map(this::getWinnerOfMatch)
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
                .map(this::getWinnerOfMatch)
                .toList();
    }

    private Player getWinnerOfMatch(Match match) {
        if (match.getResult() == null) {
            throw new IncompleteMatchException("The match has not finished or the result was not loaded.");
        }

        // Check if player one has won two sets
        if (resultService.getSetsWonPlayerOne(match.getResult()) == 2) {
            return match.getPlayerOne();
        }
        // Check if player two has won two sets
        else if (resultService.getSetsWonPlayerTwo(match.getResult()) == 2) {
            return match.getPlayerTwo();
        }

        // If there is no defined winner...
        throw new IncompleteMatchException("There is no defined winner.");

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



/*package dev.rodrilang.tennis_tournaments.services.impl;


import dev.rodrilang.tennis_tournaments.dtos.response.MatchResponseDto;
import dev.rodrilang.tennis_tournaments.dtos.response.PlayerResponseDto;
import dev.rodrilang.tennis_tournaments.dtos.response.RoundResponseDto;
import dev.rodrilang.tennis_tournaments.enums.RoundType;
import dev.rodrilang.tennis_tournaments.enums.StatusType;
import dev.rodrilang.tennis_tournaments.mappers.RoundMapper;
import dev.rodrilang.tennis_tournaments.models.Match;
import dev.rodrilang.tennis_tournaments.models.Round;
import dev.rodrilang.tennis_tournaments.models.Tournament;
import dev.rodrilang.tennis_tournaments.services.ResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import dev.rodrilang.tennis_tournaments.repositories.RoundRepository;
import dev.rodrilang.tennis_tournaments.services.RoundService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoundServiceImpl implements RoundService {

    private final RoundRepository roundRepository;
    private final RoundMapper roundMapper;
    private final ResultService resultService;

    private boolean isRoundComplete(Round round) {
        for (Match match : round.getMatches()) {
            if (!match.getStatus().equals(StatusType.FINISHED)) {
                return false; // Algún partido no está completo
            }
        }
        return true;
    }

    @Override
    public List<PlayerResponseDto> getPlayersStillCompeting(Tournament tournament) {
        return List.of();
    }

    @Override
    public Round generateRound(Tournament tournament, RoundType roundType) {
        return null;
    }

    @Override
    public List<RoundResponseDto> getRoundsByTournament(Tournament tournament) {
        return List.of();
    }

    @Override
    public MatchResponseDto getFinalMatch(Tournament tournament) {
        return null;
    }




/*
    @Override
    public Boolean isCurrentRoundComplete() {
        return null;
    }

    @Override
    public List<PlayerResponseDto> getPlayersStillCompeting() {
        return List.of();
    }

    @Override
    public Round nextRound(Round round) {

        Round nextRound = generateNextRound();


    }

    @Override
    public RoundResponseDto generateNextRound(Long tournamentId, RoundType roundType) {
        Tournament tournament = findEntityById(tournamentId); // ya lo tenés en tu service

        // 1. Obtener los jugadores disponibles (últimos ganadores o jugadores originales)
        List<Player> players = getPlayersForRound(tournament, roundType);

        if (players.size() % 2 != 0) {
            throw new IllegalArgumentException("Cantidad impar de jugadores en ronda " + roundType);
        }

        // 2. Crear la ronda
        Round round = Round.builder()
                .type(roundType)
                .tournament(tournament)
                .givenPoints(getPointsForRound(roundType)) // si usás puntos por ronda
                .build();

        // 3. Generar los partidos usando la estrategia adecuada
        RoundMatchGeneratorStrategy strategy = roundMatchGeneratorFactory.getStrategy(roundType);
        List<Match> matches = strategy.generateMatches(players, round);
        round.setMatches(matches);

        // 4. Asociar la ronda al torneo
        tournament.getRounds().add(round);
        tournamentRepository.save(tournament); // persiste en cascada

        return roundMapper.toResponseDto(round); // o el mapper que estés usando
    }



    @Override
    public List<RoundResponseDto> getRoundsByTournament(Long tournamentId) {

        return roundRepository.findRoundByTournamentId(tournamentId)
                .stream()
                .map(roundMapper::toDto)
                .toList();
    }

    @Override
    public MatchResponseDto getFinalMatch() {
        return null;
    }

*/
/*
    public boolean isCurrentRoundComplete() {
        for (Match match : getCurrentRound().getMatches()) {
            if (match.getResult() == null || match.getResult().thereIsNoWinner()) {
                return false; // Algún partido no está completo
            }
        }
        return true; // Todos los partidos están completos
    }


    public List<Player> getPlayersStillCompeting() throws IncompleteMatchException {
        List<Player> playersStillCompeting = new ArrayList<>();

        // Verificar si es la primera ronda
        if (this.tournament.getRounds().isEmpty()) {
            // Devuelve todos los jugadores en el torneo
            playersStillCompeting.addAll(this.tournament.getPlayers());
        } else {
            // Obtiene los ganadores de la última ronda
            for (Match match : getCurrentRound().getMatches()) {
                Player winner = matchService.getWinner(match);
                playersStillCompeting.add(winner);
            }
        }

        return playersStillCompeting;
    }

    public void nextRound() throws IncompleteMatchException {
        Round nextRound = generateNextRound();
        nextRound.generateMatches(getPlayersStillCompeting());
        // Add next round to tournament
        tournament.getRounds().add(nextRound);
        // Check if this is the final round and update the status accordingly
        if (nextRound instanceof Final && isCurrentRoundComplete()) {
            tournament.setStatus(TournamentStatus.FINISHED);
        }
    }

    public Round generateNextRound() {

        int currentRoundIndex = tournament.getRounds().size();
        // Determine the current round based on the current round index
        switch (currentRoundIndex) {
            case 0 -> {
                return new FirstRound(10);
            }
            case 1 -> {
                return new QuarterFinal(20);
            }
            case 2 -> {
                return new Semifinal(30);
            }
            case 3 -> {
                return new Final(50);
            }
            default -> throw new IllegalArgumentException("Can not create more rounds");
        }
    }

    public Round getCurrentRound() {
        if (tournament.getRounds().isEmpty()) {
            throw new IllegalStateException("No current round available.");
        }
        return tournament.getRounds().getLast(); // Asumiendo que la última ronda es la actual
    }

    public Match getFinalMatch() {
        return getCurrentRound().getMatches().getLast();
    }



 */
