package dev.rodrilang.tennis_tournaments.dtos.responses;

import java.time.LocalDate;

public record PlayerResponseDto(
        String dni,
        String name,
        String lastName,
        Integer points,
        LocalDate dateOfBirth,
        String nationality
) {
}
