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
    public TournamentResponseDto updateTournament(TournamentRequestDto tournamentRequestDto) {
        return null;
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

        if (!tournament.getStatus().equals(StatusType.NOT_STARTED)){
            throw new InvalidTournamentStatusException("Tournament already started");
        }

        if (tournament.getPlayers().contains(player)) {
            throw new DuplicatePlayerException(playerDni);
        }

        if(tournament.getPlayers().size() >= 16) {
            throw new TournamentFullException("Tournament is full");
        }

        tournament.getPlayers().add(player);
        tournamentRepository.save(tournament);
    }

    @Override
    public void unsubscribePlayerFromTournament(Long tournamentId, String playerDni) {

    }

    @Override
    public void assignResultToMatch(Long tournamentId, Long matchId, ResultRequestDto resultRequestDto) {

    }

    @Override
    public void modifyResultToMatch(Long tournamentId, Long matchId, ResultRequestDto resultRequestDto) {

    }

    @Override
    public void startTournament(Long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException(tournamentId));


        if(!tournament.getStatus().equals(StatusType.NOT_STARTED)) {
            throw new InvalidTournamentStatusException("Tournament already started");
        }

        if(tournament.getPlayers().size() != 16) {
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
                    "Tournament Status: " + tournament.getStatus() );
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
        return null;
    }

    private Round getCurrentRound(Tournament tournament) {
        return tournament.getRounds().getLast();
    }
/*
    public Integer addTournament(Tournament tournament) throws FileProcessingException {
        this.tournament = tournament;
        initServices();
        return tournamentRepositoryImp.create(tournament);
    }

    public Tournament findTournamentById(Integer id) throws TournamentNotFoundException, FileProcessingException {
        tournament = tournamentRepositoryImp.find(id);
        initServices();
        return tournament;
    }

    public void updateTournament(Tournament tournament) throws TournamentNotFoundException, FileProcessingException {

        if (this.tournament == null) {
            throw new TournamentNotFoundException("No tournament selected for update.");
        }
        if (tournament == null) {
            throw new IllegalArgumentException("Tournament cannot be null.");
        }
        this.setTournament(tournament);
        tournamentRepositoryImp.update(tournament);
    }

    public void deleteTournament(Integer id) throws TournamentNotFoundException, FileProcessingException {
        tournamentRepositoryImp.delete(id);
    }

    public List<Tournament> getAllTournaments() throws TournamentNotFoundException, FileProcessingException {
        return tournamentRepositoryImp.getAll();
    }

    public void registerPlayerInTournament(Player player) throws TournamentFullException, DuplicatePlayerException, TournamentNotFoundException {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null.");
        }
        tournamentPlayerService.registerPlayer(player);
        updateTournament(tournament);
    }

    public void unsubscribePlayerFromTournament(Integer idPlayer) throws TournamentNotFoundException, PlayerNotFoundException, InvalidTournamentStatusException {
        if (!tournament.getStatus().equals(TournamentStatus.NOT_STARTED)) {
            throw new InvalidTournamentStatusException("The tournament is already started");
        }
        tournamentPlayerService.unsubscribePlayer(idPlayer);
        updateTournament(tournament);
    }

    public void assignResultToMatch(Integer matchId, Result result) throws InvalidTournamentStatusException, MatchNotFoundException, TournamentNotFoundException, InvalidResultException, IncompleteMatchException {
        matchService.assignResult(matchId, result);

        updateTournament(tournament);
    }

    public void modifyResultToMatch(Integer matchId, Result result) throws InvalidTournamentStatusException, MatchNotFoundException, TournamentNotFoundException, InvalidResultException, IncompleteMatchException {
        matchService.modifyResult(matchId, result);
        updateTournament(tournament);
    }

    public void advanceTournament() throws IncompleteMatchException, InvalidTournamentStatusException, TournamentFullException, TournamentNotFoundException {
        tournamentStatusService.advanceTournament();
        updateTournament(tournament);
    }


    public Player getTournamentWinner() throws InvalidTournamentStatusException, IncompleteMatchException {
        return tournamentStatusService.getTournamentWinner();
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
        initServices();
    }

    public void setTournamentById(Integer idTournament) throws TournamentNotFoundException {
        setTournament(findTournamentById(idTournament));
    }

    public Tournament getTournament() {
        return tournament;
    }

    public TournamentPlayerService getTournamentPlayerService() {
        return tournamentPlayerService;
    }

    public RoundServiceImpl getRoundService() {
        return roundService;
    }

    public MatchServiceImpl getMatchService() {
        return matchService;
    }


    public void registerPlayer(Player player) throws TournamentFullException, DuplicatePlayerException {
        if (tournament.getPlayers().size() < 16) {
            if (!tournament.getPlayers().add(player)) {
                throw new DuplicatePlayerException(player.getDni());
            }
        } else {
            throw new TournamentFullException("Tournament is full");
        }
    }

    public void unsubscribePlayer(Integer idPlayer) throws PlayerNotFoundException {

        Iterator<Player> iterator = tournament.getPlayers().iterator();
        boolean playerFound = false;

        while (iterator.hasNext()) {
            Player player = iterator.next();
            if (player.getIdPlayer().equals(idPlayer)) {
                iterator.remove();
                playerFound = true;
                break;
            }
        }
        if (!playerFound) {
            throw new PlayerNotFoundException(idPlayer);
        }
    }


     public void advanceTournament() throws IncompleteMatchException, InvalidTournamentStatusException, TournamentFullException {
        switch (tournament.getStatus()) {
            case NOT_STARTED -> startTournament();
            case IN_PROGRESS -> advanceCurrentRound();
            case FINISHED -> throw new InvalidTournamentStatusException(tournament.getStatus().getMessage());
            default -> throw new IllegalStateException("Unexpected tournament status: " + tournament.getStatus());
        }
    }

    private void startTournament() throws TournamentFullException, IncompleteMatchException {
        if (tournament.getPlayers().size() != 16) {
            throw new TournamentFullException("Not enough players to start the tournament.");
        }
        tournament.setStatus(TournamentStatus.IN_PROGRESS);
        roundService.nextRound();
    }

    private void advanceCurrentRound() throws IncompleteMatchException {
        if (!roundService.isCurrentRoundComplete()) {
            throw new IncompleteMatchException("Not all matches have been completed.");
        }
        if (!(roundService.getCurrentRound() instanceof Final)) {
            roundService.nextRound();
        }
        updateTournamentStatus();
    }

    public Player getTournamentWinner() throws InvalidTournamentStatusException, IncompleteMatchException {
        if (isTournamentFinished()) {
            return matchService.getWinner(roundService.getFinalMatch());
        }
        throw new InvalidTournamentStatusException("Tournament has not finished yet.");
    }

    private boolean isTournamentFinished() {
        return tournament.getStatus().equals(TournamentStatus.FINISHED);
    }

    private void updateTournamentStatus() {
        if (isFinalRoundComplete()) {
            tournament.setStatus(TournamentStatus.FINISHED);
        }
    }

    private boolean isFinalRoundComplete() {
        return !tournament.getRounds().isEmpty() &&
                roundService.getCurrentRound() instanceof Final &&
                roundService.isCurrentRoundComplete();
    }
  */

}