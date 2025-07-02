package dev.rodrilang.tennis_tournaments.dtos.response;

import dev.rodrilang.tennis_tournaments.enums.SurfaceType;
import dev.rodrilang.tennis_tournaments.enums.StatusType;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public record TournamentResponseDto(
        Long id,
        String name,
        String location,
        SurfaceType surface,
        LocalDate startingDate,
        LocalDate endingDate,
        StatusType status,
        Set<PlayerResponseDto> players,
        List<RoundResponseDto> rounds
) {}
