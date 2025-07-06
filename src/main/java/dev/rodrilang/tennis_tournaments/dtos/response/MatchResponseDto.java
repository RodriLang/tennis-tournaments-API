package dev.rodrilang.tennis_tournaments.dtos.response;

public record MatchResponseDto(

        Long matchId,
        PlayerResponseDto playerOne,
        PlayerResponseDto playerTwo,
        ResultResponseDto result
) {
}
