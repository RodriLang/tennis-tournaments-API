package dev.rodrilang.tennis_tournaments.service.impl;


import dev.rodrilang.tennis_tournaments.dtos.responses.MatchResponseDto;
import dev.rodrilang.tennis_tournaments.dtos.responses.PlayerResponseDto;
import dev.rodrilang.tennis_tournaments.dtos.responses.RoundResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import dev.rodrilang.tennis_tournaments.repository.RoundRepository;
import dev.rodrilang.tennis_tournaments.service.RoundService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoundServiceImpl implements RoundService {

    private final RoundRepository roundRepository;

    @Override
    public Boolean isCurrentRoundComplete() {
        return null;
    }

    @Override
    public List<PlayerResponseDto> getPlayersStillCompeting() {
        return List.of();
    }

    @Override
    public void nextRound() {

    }

    @Override
    public RoundResponseDto generateNextRound() {
        return null;
    }

    @Override
    public RoundResponseDto getCurrentRound() {
        return null;
    }

    @Override
    public MatchResponseDto getFinalMatch() {
        return null;
    }


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
}