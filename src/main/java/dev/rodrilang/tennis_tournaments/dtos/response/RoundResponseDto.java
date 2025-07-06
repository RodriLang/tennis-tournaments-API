package dev.rodrilang.tennis_tournaments.dtos.response;

import java.util.List;

public record RoundResponseDto(

        String round,
        List<MatchResponseDto> matches
) {

}
