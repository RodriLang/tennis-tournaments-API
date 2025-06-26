package dev.rodrilang.tennis_tournaments.service;

import dev.rodrilang.tennis_tournaments.dtos.SetScoreDto;
import dev.rodrilang.tennis_tournaments.entities.Result;

import java.util.List;

public interface ResultService {

    // Agrega un nuevo set al resultado del partido
    void addSetScore(Result result, SetScoreDto setScoreDto);

    // Establece el resultado completo (sobrescribe)
    void setFullResult(Result result, List<SetScoreDto> sets);

    // Devuelve cuántos sets ganó el jugador uno
    Integer getSetsWonPlayerOne(Result result);

    // Devuelve cuántos sets ganó el jugador dos
    Integer getSetsWonPlayerTwo(Result result);

    // Valida si hay un ganador
    Boolean hasWinner(Result result);

    // Devuelve el ganador (1, 2 o 0 si no hay)
    Integer getWinner(Result result);
}

