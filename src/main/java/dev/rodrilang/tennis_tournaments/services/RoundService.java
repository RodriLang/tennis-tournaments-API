package dev.rodrilang.tennis_tournaments.service;

import dev.rodrilang.tennis_tournaments.dtos.responses.MatchResponseDto;
import dev.rodrilang.tennis_tournaments.dtos.responses.PlayerResponseDto;
import dev.rodrilang.tennis_tournaments.dtos.responses.RoundResponseDto;

import java.util.List;

public interface RoundService {

    Boolean isCurrentRoundComplete();

    List<PlayerResponseDto> getPlayersStillCompeting();

    void nextRound();

    RoundResponseDto generateNextRound();

    RoundResponseDto getCurrentRound();

    MatchResponseDto getFinalMatch();
}
