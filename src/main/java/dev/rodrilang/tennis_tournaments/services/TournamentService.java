package dev.rodrilang.tennis_tournaments.services;


import dev.rodrilang.tennis_tournaments.dtos.request.ResultRequestDto;
import dev.rodrilang.tennis_tournaments.dtos.request.TournamentRequestDto;
import dev.rodrilang.tennis_tournaments.dtos.response.PlayerResponseDto;
import dev.rodrilang.tennis_tournaments.dtos.response.RoundResponseDto;
import dev.rodrilang.tennis_tournaments.dtos.response.TournamentResponseDto;
import dev.rodrilang.tennis_tournaments.models.Tournament;

import java.util.List;

public interface TournamentService {

        TournamentResponseDto create(TournamentRequestDto tournamentRequestDto);

        Tournament findEntityById(Long id);

        TournamentResponseDto findById(Long id);

        TournamentResponseDto updateTournament(Long tournamentId, TournamentRequestDto tournamentRequestDto);

        void delete(Long id);

        List<TournamentResponseDto> getAll();

        void registerPlayerInTournament(Long tournamentId, String playerDni);

        void unsubscribePlayerFromTournament(Long tournamentId, String playerDni);

        void assignResultToMatch(Long tournamentId, Long matchId, ResultRequestDto resultRequestDto);

        void modifyResultToMatch(Long tournamentId, Long matchId, ResultRequestDto resultRequestDto);

        void startTournament(Long tournamentId);

        void advanceToNextRound(Long tournamentId);

        PlayerResponseDto getTournamentWinner(Long tournamentId);

        List<RoundResponseDto> getRoundsOfTournament(Long tournamentId);

        List<PlayerResponseDto> getTournamentPlayers(Long tournamentId);

}
