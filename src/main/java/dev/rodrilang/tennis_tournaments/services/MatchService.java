package dev.rodrilang.tennis_tournaments.service;

import dev.rodrilang.tennis_tournaments.dtos.requests.ResultRequestDto;
import dev.rodrilang.tennis_tournaments.entities.Match;
import dev.rodrilang.tennis_tournaments.entities.Player;
import dev.rodrilang.tennis_tournaments.dtos.responses.MatchResponseDto;

import java.util.List;

public interface MatchService {

    // Buscar partido por ID
    MatchResponseDto findMatchById(Long matchId);

    // Listar todos los partidos
    List<MatchResponseDto> getAllMatches();

    //Asignar un resultado al partido
    MatchResponseDto addResultToMatch(Long id, ResultRequestDto resultRequestDto);

    // Actualizar resultado de un partido
    MatchResponseDto updateResult(Long matchId, ResultRequestDto resultRequestDto);

    // Obtener partidos donde participó un jugador
    List<MatchResponseDto> getMatchesByPlayer(String playerDni);

    // Obtener el ganador del partido
    Player getWinner(Match match);

    // Verificar si un partido tiene resultado válido o terminado
    Boolean isFinished(Match match);




}