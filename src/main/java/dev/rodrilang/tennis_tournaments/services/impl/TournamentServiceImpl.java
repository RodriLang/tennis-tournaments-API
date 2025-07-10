package dev.rodrilang.tennis_tournaments.services.impl;

import dev.rodrilang.tennis_tournaments.dtos.request.ResultRequestDto;
import dev.rodrilang.tennis_tournaments.dtos.request.RoundResultsRequestDto;
import dev.rodrilang.tennis_tournaments.dtos.request.TournamentRequestDto;
import dev.rodrilang.tennis_tournaments.dtos.response.PlayerResponseDto;
import dev.rodrilang.tennis_tournaments.dtos.response.RoundResponseDto;
import dev.rodrilang.tennis_tournaments.dtos.response.TournamentDetailDto;
import dev.rodrilang.tennis_tournaments.dtos.response.TournamentListDto;
import dev.rodrilang.tennis_tournaments.enums.RoundType;
import dev.rodrilang.tennis_tournaments.mappers.PlayerMapper;
import dev.rodrilang.tennis_tournaments.mappers.RoundMapper;
import dev.rodrilang.tennis_tournaments.models.Match;
import dev.rodrilang.tennis_tournaments.models.Player;
import dev.rodrilang.tennis_tournaments.models.Tournament;
import dev.rodrilang.tennis_tournaments.enums.StatusType;
import dev.rodrilang.tennis_tournaments.exceptions.*;
import dev.rodrilang.tennis_tournaments.mappers.TournamentMapper;
import dev.rodrilang.tennis_tournaments.models.Round;
import dev.rodrilang.tennis_tournaments.repositories.PlayerRepository;
import dev.rodrilang.tennis_tournaments.services.MatchService;
import dev.rodrilang.tennis_tournaments.services.RoundService;
import dev.rodrilang.tennis_tournaments.utils.TournamentValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import dev.rodrilang.tennis_tournaments.repositories.TournamentRepository;
import dev.rodrilang.tennis_tournaments.services.TournamentService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TournamentServiceImpl implements TournamentService {


    private final TournamentRepository tournamentRepository;
    private final TournamentMapper tournamentMapper;
    private final PlayerRepository playerRepository;
    private final PlayerMapper playerMapper;
    private final RoundMapper roundMapper;
    private final RoundService roundService;
    private final MatchService matchService;


    @Override
    public TournamentDetailDto create(TournamentRequestDto tournamentRequestDto) {

        return tournamentMapper.toDetailDto(tournamentRepository
                .save(tournamentMapper.toEntity(tournamentRequestDto)));
    }

    @Override
    public Tournament findEntityById(Long id) {
        return tournamentRepository.findById(id)
                .orElseThrow(() -> new TournamentNotFoundException(id));
    }

    @Override
    public TournamentDetailDto findById(Long id) {
        return tournamentMapper.toDetailDto(this.findEntityById(id));
    }

    @Override
    public TournamentDetailDto updateTournament(
            Long tournamentId, TournamentRequestDto tournamentRequestDto) {

        Tournament tournament = this.findEntityById(tournamentId);
        tournamentMapper.updateTournamentFromDto(tournamentRequestDto, tournament);

        return tournamentMapper.toDetailDto(tournament);
    }

    @Override
    public void delete(Long id) {

        Tournament tournament = findEntityById(id);
        tournamentRepository.delete(tournament);
    }

    @Override
    public List<TournamentListDto> getAll() {
        return tournamentRepository.findAll()
                .stream()
                .map(tournamentMapper::toListDto)
                .toList();
    }

    @Override
    public void registerPlayerInTournament(Long tournamentId, String playerDni) {
        Tournament tournament = findEntityById(tournamentId);
        Player player = playerRepository.findByDniAndDeleted(playerDni, false)
                .orElseThrow(() -> new PlayerNotFoundException(playerDni));

        TournamentValidator.validateModifiableStatus(tournament);
        TournamentValidator.validateTournamentHasRoom(tournament);
        TournamentValidator.validateNonDuplicatedPlayer(tournament, player);

        tournament.getPlayers().add(player);
        tournamentRepository.save(tournament);
    }

    @Override
    public void unsubscribePlayerFromTournament(Long tournamentId, String playerDni) {

        Tournament tournament = findEntityById(tournamentId);
        TournamentValidator.validateModifiableStatus(tournament);

        if (!tournament.getPlayers().removeIf(player -> player.getDni().equals(playerDni))) {
            throw new PlayerNotFoundException("The player " + playerDni + " is not in this Tournament");
        }

        tournamentRepository.save(tournament);
    }

    @Override
    public void assignResultToMatch(Long tournamentId, Long matchId, ResultRequestDto resultRequestDto) {

        Tournament tournament = findEntityById(tournamentId);
        boolean foundMatch = false;

        for (Match match : getCurrentRound(tournament).getMatches()) {
            if (match.getId().equals(matchId)) {
                matchService.addResultToMatch(match, resultRequestDto);
                foundMatch = true;
            }
        }
        if (!foundMatch) {
            throw new MatchNotFoundException("Match not found in Tournament");
        }
    }

    @Transactional
    @Override
    public void assignResultsToRound(
            Long tournamentId, RoundType roundType, RoundResultsRequestDto roundResultsRequestDto) {

        Tournament tournament = findEntityById(tournamentId);
        Round currentRound = getCurrentRound(tournament);

        if (!currentRound.getType().equals(roundType)) {
            throw new InvalidTournamentStatusException("The tournament "
                    + tournamentId + " is not in this Round");
        }

        roundResultsRequestDto.results()
                .forEach(result ->
                        assignResultToMatch(tournamentId, result.matchId(), result.result()));
    }

    @Override
    public void modifyResultToMatch(Long tournamentId, Long matchId, ResultRequestDto resultRequestDto) {

        Tournament tournament = findEntityById(tournamentId);
        boolean foundMatch = false;

        for (Match match : getCurrentRound(tournament).getMatches()) {
            if (match.getId().equals(matchId)) {
                matchService.updateResult(match, resultRequestDto);
                foundMatch = true;
            }
        }
        if (!foundMatch) {
            throw new MatchNotFoundException("Match not found in Tournament");
        }
    }

    @Transactional
    @Override
    public void modifyResultsToRound(
            Long tournamentId, RoundType roundType, RoundResultsRequestDto roundResultsRequestDto) {

        Tournament tournament = findEntityById(tournamentId);
        Round currentRound = getCurrentRound(tournament);

        if (!currentRound.getType().equals(roundType)) {
            throw new InvalidTournamentStatusException("The tournament "
                    + tournamentId + " is not in this Round");
        }
        roundResultsRequestDto.results()
                .forEach(result ->
                        modifyResultToMatch(tournamentId, result.matchId(), result.result()));
    }

    @Override
    public void startTournament(Long tournamentId) {
        Tournament tournament = this.findEntityById(tournamentId);

        TournamentValidator.validateTournamentNotStarted(tournament);
        TournamentValidator.validateTournamentIsReadyToStart(tournament);

        tournament.getRounds().add(roundService.generateFirstRound(tournament.getPlayers()));
        tournament.setStatus(StatusType.IN_PROGRESS);

        tournamentRepository.save(tournament);
    }


    @Override
    public void advanceToNextRound(Long tournamentId) {
        Tournament tournament = this.findEntityById(tournamentId);
        Round currentRound = getCurrentRound(tournament);
        
        TournamentValidator.validateTournamentNotFinished(tournament);

        if (currentRound.getType() == RoundType.FINAL) {
            finalizeTournament(tournament, currentRound);
        } else {
            Round nextRound = roundService.generateNextRound(currentRound);
            tournament.getRounds().add(nextRound);
            tournamentRepository.save(tournament);
        }
    }

    private void finalizeTournament(Tournament tournament, Round finalRound) {
        tournament.setStatus(StatusType.FINISHED);
        roundService.assignPoints(finalRound);
        tournamentRepository.save(tournament);
    }

    @Override
    public PlayerResponseDto getTournamentWinner(Long tournamentId) {

        Tournament tournament = findEntityById(tournamentId);

        if (!tournament.getStatus().equals(StatusType.FINISHED)) {
            throw new InvalidTournamentStatusException("There is no winner for this tournament yet. " +
                    "Tournament Status: " + tournament.getStatus());
        }
        return playerMapper.toDto(matchService.getWinner(this.getCurrentRound(tournament).getMatches().getLast()));
    }

    @Override
    public List<RoundResponseDto> getRoundsOfTournament(Long tournamentId) {
        Tournament tournament = findEntityById(tournamentId);

        TournamentValidator.validateTournamentNotStarted(tournament);

        return tournament.getRounds()
                .stream()
                .map(roundMapper::toDto)
                .toList();
    }

    @Override
    public List<PlayerResponseDto> getTournamentPlayers(Long tournamentId) {
        Tournament tournament = findEntityById(tournamentId);
        return tournament.getPlayers()
                .stream()
                .map(playerMapper::toDto)
                .toList();
    }

    private Round getCurrentRound(Tournament tournament) {
        return tournament.getRounds().getLast();
    }
}