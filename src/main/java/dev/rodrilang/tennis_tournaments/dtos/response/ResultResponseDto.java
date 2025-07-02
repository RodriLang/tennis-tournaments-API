package dev.rodrilang.tennis_tournaments.dtos.response;

import dev.rodrilang.tennis_tournaments.dtos.SetScoreDto;

import java.util.List;

public record ResultResponseDto(

        List<SetScoreDto> setsScore

) {
}
