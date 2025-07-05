package dev.rodrilang.tennis_tournaments.services;

import dev.rodrilang.tennis_tournaments.dtos.request.ResultRequestDto;
import dev.rodrilang.tennis_tournaments.dtos.response.PlayerResponseDto;
import dev.rodrilang.tennis_tournaments.models.Match;
import dev.rodrilang.tennis_tournaments.models.Player;
import dev.rodrilang.tennis_tournaments.dtos.response.MatchResponseDto;

import java.util.List;

public interface MatchService {

    // Buscar partido por ID
    MatchResponseDto findMatchById(Long matchId);

    // Listar todos los partidos
    List<MatchResponseDto> getAllMatches();

    //Asignar un resultado al partido
    MatchResponseDto addResultToMatch(Match match, ResultRequestDto resultRequestDto);

    MatchResponseDto addResultToMatch(Long matchId, ResultRequestDto resultRequestDto);

    // Actualizar resultado de un partido
    MatchResponseDto updateResult(Match match, ResultRequestDto resultRequestDto);

    MatchResponseDto updateResult(Long matchId, ResultRequestDto resultRequestDto);

    // Obtener partidos donde participó un jugador
    List<MatchResponseDto> getMatchesByPlayer(String playerDni);

    // Obtener el ganador del partido
    Player getWinner(Match match);

    PlayerResponseDto getWinner(Long matchId);

}