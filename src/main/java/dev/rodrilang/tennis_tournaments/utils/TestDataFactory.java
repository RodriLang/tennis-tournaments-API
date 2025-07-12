package dev.rodrilang.tennis_tournaments.utils;

import dev.rodrilang.tennis_tournaments.dtos.SetScoreDto;
import dev.rodrilang.tennis_tournaments.dtos.request.ResultRequestDto;
import dev.rodrilang.tennis_tournaments.dtos.request.TournamentRequestDto;
import dev.rodrilang.tennis_tournaments.dtos.response.*;
import dev.rodrilang.tennis_tournaments.enums.RoundType;
import dev.rodrilang.tennis_tournaments.enums.StatusType;
import dev.rodrilang.tennis_tournaments.enums.SurfaceType;
import dev.rodrilang.tennis_tournaments.models.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TestDataFactory {

    private TestDataFactory() {
    }

    private static final String TOURNAMENT_NAME = "Roland Garros";

    public static PlayerResponseDto buildPlayerOneDto() {
        return new PlayerResponseDto("12345678", "Juan", "Pérez", 300, LocalDate.of(1990, 5, 3), "Argentino");
    }

    public static PlayerResponseDto buildPlayerTwoDto() {
        return new PlayerResponseDto("87654321", "Luis", "Gómez", 280, LocalDate.of(1993, 7, 22), "Argentino");
    }

    public static SetScoreDto set1() {
        return new SetScoreDto(6, 4);
    }

    public static SetScoreDto set2() {
        return new SetScoreDto(3, 6);
    }

    public static SetScoreDto set3() {
        return new SetScoreDto(7, 5);
    }

    public static ResultResponseDto buildResultResponseDto() {
        return new ResultResponseDto(List.of(set1(), set2(), set3()));
    }

    public static ResultRequestDto buildResultRequestDto() {
        return new ResultRequestDto(List.of(set1(), set2(), set3()));
    }

    public static MatchResponseDto buildMatchResponseDto() {
        return new MatchResponseDto(1L, buildPlayerOneDto(), buildPlayerTwoDto(), buildResultResponseDto());
    }

    public static TournamentRequestDto buildTournamentRequestDto() {
        return new TournamentRequestDto(TOURNAMENT_NAME, StatusType.NOT_STARTED.name(), SurfaceType.CARPET,
                LocalDate.now(), LocalDate.now().plusDays(1L));
    }

    public static TournamentListDto buildTournamentListDto() {
        return new TournamentListDto(1L, TOURNAMENT_NAME, StatusType.NOT_STARTED.name(), SurfaceType.CARPET,
                LocalDate.now(), StatusType.NOT_STARTED, 16);
    }

    public static TournamentDetailDto buildTournamentDetailDto() {
        return new TournamentDetailDto(1L, TOURNAMENT_NAME, StatusType.NOT_STARTED.name(),
                SurfaceType.CARPET, LocalDate.now(), LocalDate.now().plusDays(1), StatusType.NOT_STARTED,
                Set.of(buildPlayerOneDto(), buildPlayerTwoDto()), List.of(buildRoundResponseDto()));
    }

    public static Tournament buildTournament() {
        return Tournament.builder()
                .id(1L)
                .status(StatusType.NOT_STARTED)
                .rounds(new ArrayList<>())
                .players(new HashSet<>())
                .build();

    }

    public static RoundResponseDto buildRoundResponseDto() {
        return new RoundResponseDto(RoundType.FIRST.name(), List.of(buildMatchResponseDto()));
    }

    public static Result buildResultEntity() {
        Result result = new Result();
        result.setSetsScore(List.of());
        return result;
    }

}
