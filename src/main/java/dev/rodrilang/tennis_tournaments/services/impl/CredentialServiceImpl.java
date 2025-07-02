package dev.rodrilang.tennis_tournaments.services.impl;

import dev.rodrilang.tennis_tournaments.dtos.requests.CredentialRequestDto;
import dev.rodrilang.tennis_tournaments.dtos.responses.CredentialResponseDto;
import dev.rodrilang.tennis_tournaments.entities.Credential;
import dev.rodrilang.tennis_tournaments.exceptions.CredentialNotFoundException;
import dev.rodrilang.tennis_tournaments.mappers.CredentialMapper;
import dev.rodrilang.tennis_tournaments.repositories.CredentialRepository;
import dev.rodrilang.tennis_tournaments.security.JwtService;
import dev.rodrilang.tennis_tournaments.services.CredentialService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CredentialServiceImpl implements CredentialService {

    private final CredentialRepository credentialRepository;
    private final CredentialMapper credentialMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public CredentialResponseDto login(CredentialRequestDto dto) {
        Credential credential = credentialRepository.findByUsername(dto.username())
                .orElseThrow(CredentialNotFoundException::new);

        if (!passwordEncoder.matches(dto.password(), credential.getPassword())) {
            throw new IllegalArgumentException("Credenciales inválidas");
        }

        return credentialMapper.toResponse(credential);
    }

    @Override
    public String generateToken(String username) {
        Credential credential = credentialRepository.findByUsername(username)
                .orElseThrow(CredentialNotFoundException::new);
        return jwtService.generateToken(credential);
    }
}
