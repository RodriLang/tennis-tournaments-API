package dev.rodrilang.tennis_tournaments.dtos.response;

public record MatchResponseDto (

         PlayerResponseDto playerOne,
         PlayerResponseDto playerTwo,
         ResultResponseDto result
) {
}
