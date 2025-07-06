package dev.rodrilang.tennis_tournaments.services.impl;


import dev.rodrilang.tennis_tournaments.dtos.request.ResultRequestDto;
import dev.rodrilang.tennis_tournaments.dtos.response.MatchResponseDto;
import dev.rodrilang.tennis_tournaments.dtos.response.PlayerResponseDto;
import dev.rodrilang.tennis_tournaments.enums.StatusType;
import dev.rodrilang.tennis_tournaments.mappers.PlayerMapper;
import dev.rodrilang.tennis_tournaments.models.Match;
import dev.rodrilang.tennis_tournaments.models.Player;
import dev.rodrilang.tennis_tournaments.exceptions.IncompleteMatchException;
import dev.rodrilang.tennis_tournaments.exceptions.InvalidTournamentStatusException;
import dev.rodrilang.tennis_tournaments.exceptions.MatchNotFoundException;
import dev.rodrilang.tennis_tournaments.mappers.MatchMapper;
import dev.rodrilang.tennis_tournaments.mappers.ResultMapper;
import dev.rodrilang.tennis_tournaments.services.ResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import dev.rodrilang.tennis_tournaments.repositories.MatchRepository;
import dev.rodrilang.tennis_tournaments.services.MatchService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {

    private final MatchRepository matchRepository;
    private final MatchMapper matchMapper;
    private final ResultMapper resultMapper;
    private final ResultService resultService;
    private final PlayerMapper playerMapper;


    @Override
    public MatchResponseDto findMatchById(Long matchId) {

        return matchMapper.toDto(this.findEntityById(matchId));
    }

    @Override
    public List<MatchResponseDto> getAllMatches() {

        return matchRepository.findAll()
                .stream()
                .map(matchMapper::toDto)
                .toList();
    }

    @Override
    public MatchResponseDto addResultToMatch(Match match, ResultRequestDto resultRequestDto) {

        if (match.getResult() != null) {
            throw new InvalidTournamentStatusException("The match with id " + match.getId() + " already has a result");
        }

        return updateResult(match, resultRequestDto);
    }

    @Override
    public MatchResponseDto updateResult(Match match, ResultRequestDto resultRequestDto) {

        match.setResult(resultMapper.toEntity(resultRequestDto));

        if (thereIsAWinner(match)) {
            match.setStatus(StatusType.FINISHED);
        } else {
            match.setStatus(StatusType.IN_PROGRESS);
        }

        return matchMapper.toDto(matchRepository.save(match));
    }

    @Override
    public List<MatchResponseDto> getMatchesByPlayer(String playerDni) {

        return matchRepository.getMatchesByPlayerOne_DniOrPlayerTwo_Dni(playerDni, playerDni)
                .stream()
                .map(matchMapper::toDto)
                .toList();
    }

    @Override
    public Player getWinner(Match match) {

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

    @Override
    public PlayerResponseDto getWinner(Long matchId) {
        return playerMapper.toDto(getWinner(this.findEntityById(matchId)));
    }


    private Match findEntityById(Long matchId) {
        return matchRepository.findById(matchId)
                .orElseThrow(() -> new MatchNotFoundException(matchId));
    }

    private boolean thereIsAWinner(Match match) {

        if (match.getResult() == null) {
            return false;
        }

        // Check if player one has won two sets
        if (resultService.getSetsWonPlayerOne(match.getResult()) == 2) {
            return true;
        }

        // Check if player two has won two sets
        else return resultService.getSetsWonPlayerTwo(match.getResult()) == 2;
    }
}
