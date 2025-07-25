package dev.rodrilang.tennis_tournaments.services.impl;

import dev.rodrilang.tennis_tournaments.dtos.SetScoreDto;
import dev.rodrilang.tennis_tournaments.dtos.response.ResultResponseDto;
import dev.rodrilang.tennis_tournaments.models.Result;
import dev.rodrilang.tennis_tournaments.models.SetScore;
import dev.rodrilang.tennis_tournaments.exceptions.ResultNotFoundException;
import dev.rodrilang.tennis_tournaments.mappers.ResultMapper;
import dev.rodrilang.tennis_tournaments.mappers.SetScoreMapper;
import dev.rodrilang.tennis_tournaments.repositories.ResultRepository;
import dev.rodrilang.tennis_tournaments.services.ResultService;
import dev.rodrilang.tennis_tournaments.services.SetScoreValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResultServiceImpl implements ResultService {

    private final ResultRepository resultRepository;
    private final ResultMapper resultMapper;
    private final SetScoreMapper setScoreMapper;
    private final SetScoreValidator setScoreValidator;

    @Override
    public ResultResponseDto addSetScore(Long resultId, SetScoreDto setScoreDto) {
        Result result = this.findEntityById(resultId);

        SetScore setScore = setScoreMapper.toEntity(setScoreDto);
        setScoreValidator.validateSetScore(setScore);

        result.getSetsScore().add(setScore);

        return resultMapper.toDto(resultRepository.save(result));
    }


    @Override
    public ResultResponseDto setFullResult(Long resultId, List<SetScoreDto> sets) {

        Result result = this.findEntityById(resultId);
        result.setSetsScore(sets
                .stream()
                .map(setScoreMapper::toEntity)
                .toList());

        return resultMapper.toDto(resultRepository.save(result));
    }

    @Override
    public Integer getSetsWonPlayerOne(Result result) {

        return (int) result.getSetsScore()
                .stream()
                .filter(setScore -> setScore.getPlayerOneScore() > setScore.getPlayerTwoScore())
                .count();
    }

    @Override
    public Integer getSetsWonPlayerTwo(Result result) {

            return (int) result.getSetsScore()
                    .stream()
                    .filter(setScore -> setScore.getPlayerTwoScore() > setScore.getPlayerOneScore())
                    .count();
    }

    private Result findEntityById(Long resultId) {
        return resultRepository.findById(resultId)
                .orElseThrow(() -> new ResultNotFoundException(resultId));
    }
}
