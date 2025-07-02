package dev.rodrilang.tennis_tournaments.services;

import dev.rodrilang.tennis_tournaments.dtos.SetScoreDto;
import dev.rodrilang.tennis_tournaments.dtos.response.ResultResponseDto;
import dev.rodrilang.tennis_tournaments.models.Result;

import java.util.List;

public interface ResultService {

    // Agrega un nuevo set al resultado del partido
    ResultResponseDto addSetScore(Long resultId, SetScoreDto setScoreDto);

    // Establece el resultado completo (sobrescribe)
    ResultResponseDto setFullResult(Long resultId, List<SetScoreDto> sets);

    // Devuelve cuántos sets ganó el jugador uno
    Integer getSetsWonPlayerOne(Result result);

    // Devuelve cuántos sets ganó el jugador dos
    Integer getSetsWonPlayerTwo(Result result);

}

