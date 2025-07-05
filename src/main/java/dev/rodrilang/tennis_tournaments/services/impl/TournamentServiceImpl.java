package dev.rodrilang.tennis_tournaments.services.impl;

import dev.rodrilang.tennis_tournaments.dtos.request.ResultRequestDto;
import dev.rodrilang.tennis_tournaments.dtos.request.TournamentRequestDto;
import dev.rodrilang.tennis_tournaments.dtos.response.PlayerResponseDto;
import dev.rodrilang.tennis_tournaments.dtos.response.RoundResponseDto;
import dev.rodrilang.tennis_tournaments.dtos.response.TournamentResponseDto;
import dev.rodrilang.tennis_tournaments.enums.RoundType;
import dev.rodrilang.tennis_tournaments.mappers.PlayerMapper;
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
import dev.rodrilang.tennis_tournaments.strategy.RoundFactory;
import dev.rodrilang.tennis_tournaments.strategy.RoundStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import dev.rodrilang.tennis_tournaments.repositories.TournamentRepository;
import dev.rodrilang.tennis_tournaments.services.TournamentService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TournamentServiceImpl implements TournamentService {


    private final TournamentRepository tournamentRepository;
    private final TournamentMapper tournamentMapper;
    private final PlayerRepository playerRepository;
    private final PlayerMapper playerMapper;
    private final RoundService roundService;
    private final MatchService matchService;
    private final RoundFactory roundFactory;


    @Override
    public TournamentResponseDto create(TournamentRequestDto tournamentRequestDto) {

        return tournamentMapper.toDto(tournamentRepository
                .save(tournamentMapper.toEntity(tournamentRequestDto)));
    }

    @Override
    public Tournament findEntityById(Long id) {
        return tournamentRepository.findById(id)
                .orElseThrow(() -> new TournamentNotFoundException(id));
    }

    @Override
    public TournamentResponseDto findById(Long id) {
        return tournamentMapper.toDto(this.findEntityById(id));
    }

    @Override
    public TournamentResponseDto updateTournament(Long tournamentId, TournamentRequestDto tournamentRequestDto) {

        Tournament tournament = this.findEntityById(tournamentId);
        tournamentMapper.updateTournamentFromDto(tournamentRequestDto, tournament);

        return tournamentMapper.toDto(tournament);
    }

    @Override
    public void delete(Long id) {

        Tournament tournament = findEntityById(id);
        tournamentRepository.delete(tournament);
    }

    @Override
    public List<TournamentResponseDto> getAll() {
        return tournamentRepository.findAll()
                .stream()
                .map(tournamentMapper::toDto)
                .toList();
    }

    @Override
    public void registerPlayerInTournament(Long tournamentId, String playerDni) {
        Tournament tournament = findEntityById(tournamentId);
        Player player = playerRepository.findByDniAndDeleted(playerDni, false)
                .orElseThrow(() -> new PlayerNotFoundException(playerDni));

        checkModifiableStatus(tournament);

        if (tournament.getPlayers().contains(player)) {
            throw new DuplicatePlayerException(playerDni);
        }

        if (tournament.getPlayers().size() >= 16) {
            throw new TournamentFullException("Tournament is full");
        }

        tournament.getPlayers().add(player);
        tournamentRepository.save(tournament);
    }

    @Override
    public void unsubscribePlayerFromTournament(Long tournamentId, String playerDni) {

        Tournament tournament = findEntityById(tournamentId);
        checkModifiableStatus(tournament);

        tournament.getPlayers().removeIf(player -> player.getDni().equals(playerDni));
        tournamentRepository.save(tournament);
    }

    @Override
    public void assignResultToMatch(Long tournamentId, Long matchId, ResultRequestDto resultRequestDto) {

        Tournament tournament = findEntityById(tournamentId);
        checkModifiableStatus(tournament);
        boolean foundMatch = false;

        for(Match match : getCurrentRound(tournament).getMatches()) {
            if(match.getId().equals(matchId)) {
                matchService.addResultToMatch(match, resultRequestDto);
                foundMatch = true;
            }
        }
        if(!foundMatch) {
            throw new MatchNotFoundException("Match not found in Tournament");
        }
    }

    @Override
    public void modifyResultToMatch(Long tournamentId, Long matchId, ResultRequestDto resultRequestDto) {

        Tournament tournament = findEntityById(tournamentId);
        checkModifiableStatus(tournament);
        boolean foundMatch = false;

        for(Match match : getCurrentRound(tournament).getMatches()) {
            if(match.getId().equals(matchId)) {
                matchService.updateResult(match, resultRequestDto);
                foundMatch = true;
            }
        }
        if(!foundMatch) {
            throw new MatchNotFoundException("Match not found in Tournament");
        }
    }

    @Override
    public void startTournament(Long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException(tournamentId));

        checkTournamentNotStarted(tournament);

        if (tournament.getPlayers().size() != 16) {
            throw new InvalidTournamentStatusException("There is not enough players to start tournament");
        }

        Round round = Round.builder()
                .type(RoundType.FIRST)
                .tournament(tournament)
                .build();

        List<Player> players = new ArrayList<>(tournament.getPlayers());
        RoundStrategy strategy = roundFactory.getStrategy(RoundType.FIRST);
        List<Match> matches = strategy.generateMatches(players, round);
        round.setMatches(matches);

        tournament.getRounds().add(round);
        tournament.setStatus(StatusType.IN_PROGRESS);
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

        if (tournament.getStatus().equals(StatusType.NOT_STARTED)) {
            throw new InvalidTournamentStatusException("Tournament doesn't start yet.");
        }

        return roundService.getRoundsByTournament(tournament);
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

    private void checkTournamentNotStarted(Tournament tournament) {
        if(!tournament.getStatus().equals(StatusType.NOT_STARTED)) {
            throw new InvalidTournamentStatusException("Tournament already started");
        }
    }

    private void checkTournamentNotFinished(Tournament tournament) {
        if(tournament.getStatus().equals(StatusType.FINISHED)) {
            throw new InvalidTournamentStatusException("Tournament has already ended");
        }
    }

    private void checkModifiableStatus(Tournament tournament) {

        checkTournamentNotFinished(tournament);
        checkTournamentNotStarted(tournament);
    }
}