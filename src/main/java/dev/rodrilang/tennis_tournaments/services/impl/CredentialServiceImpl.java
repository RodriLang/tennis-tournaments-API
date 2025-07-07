package dev.rodrilang.tennis_tournaments.services.impl;

import dev.rodrilang.tennis_tournaments.dtos.request.ChangePasswordRequest;
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
    public void login(LoginRequestDto dto) {
        log.info("Login attempt by username: {}", dto.username());

        Credential credential = this.getCredentialByUsername(dto.username());

        this.checkPassword(credential, dto.password());

        log.info("Successful login for username: {}", dto.username());
    }

    @Override
    public TokenResponseDto generateToken(String username) {
        log.info("Generating token for username: {}", username);

        Credential credential = this.getCredentialByUsername(username);

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

    @Override
    public CredentialResponseDto updatePassword(ChangePasswordRequest changePasswordRequest) {
        Credential credential = this.getCredentialByUsername(changePasswordRequest.username());
        this.checkPassword(credential, changePasswordRequest.oldPassword());

        credential.setPassword(passwordEncoder.encode(changePasswordRequest.newPassword()));
        credentialRepository.save(credential);
        return credentialMapper.toDto(credential);
    }

    private Credential getCredentialByUsername(String username) {
        return credentialRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("Credential not found for username: {}", username);
                    return new CredentialNotFoundException();
                });
    }

    private void checkPassword(Credential credential, String password) {
        if(!passwordEncoder.matches(password, credential.getPassword())) {
            log.warn("Invalid password for username: {}", credential.getUsername());
            throw new CredentialNotFoundException();
        }
    }
}