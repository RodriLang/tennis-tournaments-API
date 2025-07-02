package dev.rodrilang.tennis_tournaments.dtos.response;

import java.util.List;

public record RoundResponseDto(

        List<MatchResponseDto> matches
) {

}
