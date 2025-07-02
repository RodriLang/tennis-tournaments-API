package dev.rodrilang.tennis_tournaments.service;


import dev.rodrilang.tennis_tournaments.dtos.requests.ResultRequestDto;
import dev.rodrilang.tennis_tournaments.dtos.requests.TournamentRequestDto;
import dev.rodrilang.tennis_tournaments.dtos.responses.PlayerResponseDto;
import dev.rodrilang.tennis_tournaments.dtos.responses.TournamentResponseDto;
import dev.rodrilang.tennis_tournaments.entities.Tournament;

import java.util.List;

public interface TournamentService {

        TournamentResponseDto create(TournamentRequestDto tournamentRequestDto);

        Tournament findEntityById(Long id);

        TournamentResponseDto findById(Long id);

        TournamentResponseDto updateTournament(TournamentRequestDto tournamentRequestDto);

        void delete(Long id);

        List<Tournament> getAll();

        void registerPlayerInTournament(Long tournamentId, String playerDni);

        void unsubscribePlayerFromTournament(Long tournamentId, String playerDni);

        void assignResultToMatch(Long tournamentId, Long matchId, ResultRequestDto resultRequestDto);

        void modifyResultToMatch(Long tournamentId, Long matchId, ResultRequestDto resultRequestDto);

        void advanceTournament(Long tournamentId);

        PlayerResponseDto getTournamentWinner(Long tournamentId);

}
