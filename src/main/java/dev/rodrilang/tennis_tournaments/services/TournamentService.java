package dev.rodrilang.tennis_tournaments.services;


import dev.rodrilang.tennis_tournaments.dtos.request.ResultRequestDto;
import dev.rodrilang.tennis_tournaments.dtos.request.RoundResultsRequestDto;
import dev.rodrilang.tennis_tournaments.dtos.request.TournamentRequestDto;
import dev.rodrilang.tennis_tournaments.dtos.response.PlayerResponseDto;
import dev.rodrilang.tennis_tournaments.dtos.response.RoundResponseDto;
import dev.rodrilang.tennis_tournaments.dtos.response.TournamentDetailDto;
import dev.rodrilang.tennis_tournaments.dtos.response.TournamentListDto;
import dev.rodrilang.tennis_tournaments.enums.RoundType;
import dev.rodrilang.tennis_tournaments.models.Tournament;

import java.util.List;

public interface TournamentService {

        TournamentDetailDto create(TournamentRequestDto tournamentRequestDto);

        Tournament findEntityById(Long id);

        TournamentDetailDto findById(Long id);

        TournamentDetailDto updateTournament(Long tournamentId, TournamentRequestDto tournamentRequestDto);

        void delete(Long id);

        List<TournamentListDto> getAll();

        void registerPlayerInTournament(Long tournamentId, String playerDni);

        void unsubscribePlayerFromTournament(Long tournamentId, String playerDni);

        void assignResultToMatch(Long tournamentId, Long matchId, ResultRequestDto resultRequestDto);

        void assignResultsToRound(Long tournamentId, RoundType roundType, RoundResultsRequestDto roundResultsRequestDto);

        void modifyResultToMatch(Long tournamentId, Long matchId, ResultRequestDto resultRequestDto);

        void modifyResultsToRound(Long tournamentId, RoundType roundType, RoundResultsRequestDto roundResultsRequestDto);

        void startTournament(Long tournamentId);

        void advanceToNextRound(Long tournamentId);

        PlayerResponseDto getTournamentWinner(Long tournamentId);

        List<RoundResponseDto> getRoundsOfTournament(Long tournamentId);

        List<PlayerResponseDto> getTournamentPlayers(Long tournamentId);

}
