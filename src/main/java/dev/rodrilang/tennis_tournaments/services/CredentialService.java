package dev.rodrilang.tennis_tournaments.services;

import dev.rodrilang.tennis_tournaments.dtos.request.ChangePasswordRequest;
import dev.rodrilang.tennis_tournaments.dtos.request.CredentialRequestDto;
import dev.rodrilang.tennis_tournaments.dtos.request.LoginRequestDto;
import dev.rodrilang.tennis_tournaments.dtos.response.CredentialResponseDto;
import dev.rodrilang.tennis_tournaments.dtos.response.TokenResponseDto;

import java.util.List;

public interface CredentialService {

    CredentialResponseDto create(CredentialRequestDto credentialRequestDto);

    void login(LoginRequestDto dto);

    TokenResponseDto generateToken(String username);

    void delete(Long credentialId);

    List<CredentialResponseDto> getCredentials();

    CredentialResponseDto updatePassword(ChangePasswordRequest changePasswordRequest);
}

