package dev.rodrilang.tennis_tournaments.services;

import dev.rodrilang.tennis_tournaments.dtos.response.MatchResponseDto;
import dev.rodrilang.tennis_tournaments.dtos.response.PlayerResponseDto;
import dev.rodrilang.tennis_tournaments.dtos.response.RoundResponseDto;
import dev.rodrilang.tennis_tournaments.enums.RoundType;
import dev.rodrilang.tennis_tournaments.models.Tournament;
import dev.rodrilang.tennis_tournaments.models.Round;

import java.util.List;

public interface RoundService {

    List<PlayerResponseDto> getPlayersStillCompeting(Tournament tournament);

    Round generateRound(Tournament tournament, RoundType roundType);

    List<RoundResponseDto> getRoundsByTournament(Tournament tournament);

    MatchResponseDto getFinalMatch(Tournament tournament);
}
