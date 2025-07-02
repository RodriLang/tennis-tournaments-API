package dev.rodrilang.tennis_tournaments.dtos.responses;

public record MatchResponseDto (

         PlayerResponseDto playerOne,
         PlayerResponseDto playerTwo,
         ResultResponseDto result
) {
}
