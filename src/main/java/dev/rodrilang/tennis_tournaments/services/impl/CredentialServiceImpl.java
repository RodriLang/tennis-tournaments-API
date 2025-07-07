package dev.rodrilang.tennis_tournaments.services.impl;

import dev.rodrilang.tennis_tournaments.dtos.request.CredentialRequestDto;
import dev.rodrilang.tennis_tournaments.dtos.request.LoginRequestDto;
import dev.rodrilang.tennis_tournaments.dtos.response.CredentialResponseDto;
import dev.rodrilang.tennis_tournaments.dtos.response.TokenResponseDto;
import dev.rodrilang.tennis_tournaments.models.Credential;
import dev.rodrilang.tennis_tournaments.exceptions.CredentialNotFoundException;
import dev.rodrilang.tennis_tournaments.mappers.CredentialMapper;
import dev.rodrilang.tennis_tournaments.repositories.CredentialRepository;
import dev.rodrilang.tennis_tournaments.security.JwtService;
import dev.rodrilang.tennis_tournaments.services.CredentialService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CredentialServiceImpl implements CredentialService {

    private final CredentialRepository credentialRepository;
    private final CredentialMapper credentialMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;


    @Override
    public CredentialResponseDto create(CredentialRequestDto credentialRequestDto) {
        log.info("Create credential");

        Credential credential = credentialMapper.toEntity(credentialRequestDto);
        credential.setPassword(passwordEncoder.encode(credentialRequestDto.password()));
        credentialRepository.save(credential);

        log.info("Credential created with id: {}", credential.getId());
        return credentialMapper.toDto(credential);
    }


    @Override
    public CredentialResponseDto login(LoginRequestDto dto) {
        log.info("Login attempt by username: {}", dto.username());

        Credential credential = credentialRepository.findByUsername(dto.username())
                .orElseThrow(() -> {
                    log.warn("Credential not found for username: {}", dto.username());
                    return new CredentialNotFoundException();
                });

        if (!passwordEncoder.matches(dto.password(), credential.getPassword())) {
            log.warn("Invalid password for username: {}", dto.username());
            throw new CredentialNotFoundException();
        }

        log.info("Successful login for username: {}", dto.username());
        return credentialMapper.toDto(credential);
    }

    @Override
    public TokenResponseDto generateToken(String username) {
        log.info("Generating token for username: {}", username);

        Credential credential = credentialRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.error("Credential not found for token of: {}", username);
                    return new CredentialNotFoundException();
                });

        log.info("Token successfully generated for username: {}", username);
        return new TokenResponseDto(jwtService.generateToken(credential));

    }

    @Override
    public void delete(Long credentialId) {
        Credential credential = credentialRepository.findById(credentialId)
                .orElseThrow(CredentialNotFoundException::new);

        credentialRepository.delete(credential);
    }

    @Override
    public List<CredentialResponseDto> getCredentials() {
        return credentialRepository.findAll()
                .stream()
                .map(credentialMapper::toDto)
                .toList();
    }
}