package dev.rodrilang.tennis_tournaments.dtos.requests;


import dev.rodrilang.tennis_tournaments.enums.RoleType;

public record CredentialRequestDto(
        String username,
        String password,
        RoleType role) {
}