package dev.rodrilang.tennis_tournaments.services;

import dev.rodrilang.tennis_tournaments.dtos.request.CredentialRequestDto;
import dev.rodrilang.tennis_tournaments.dtos.request.LoginRequestDto;
import dev.rodrilang.tennis_tournaments.dtos.response.CredentialResponseDto;
import dev.rodrilang.tennis_tournaments.dtos.response.TokenResponseDto;

public interface CredentialService {

    CredentialResponseDto create(CredentialRequestDto credentialRequestDto);

    CredentialResponseDto login(LoginRequestDto dto);

    TokenResponseDto generateToken(String username);
}

