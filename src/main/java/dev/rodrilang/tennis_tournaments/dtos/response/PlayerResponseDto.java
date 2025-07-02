package dev.rodrilang.tennis_tournaments.dtos.response;

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
