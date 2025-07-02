package dev.rodrilang.tennis_tournaments.services.impl;

import dev.rodrilang.tennis_tournaments.dtos.response.TokenResponseDto;
import dev.rodrilang.tennis_tournaments.models.Credential;
import dev.rodrilang.tennis_tournaments.enums.RoleType;
import dev.rodrilang.tennis_tournaments.exceptions.CredentialNotFoundException;
import dev.rodrilang.tennis_tournaments.repositories.CredentialRepository;
import dev.rodrilang.tennis_tournaments.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

class CredentialServiceImplTest {

    private CredentialRepository credentialRepository;
    private JwtService jwtService;
    private CredentialServiceImpl credentialService;

    @BeforeEach
    void setUp() {
        credentialRepository = mock(CredentialRepository.class);
        jwtService = mock(JwtService.class);
        credentialService = new CredentialServiceImpl(
                credentialRepository,
                null, // credentialMapper no lo usamos acá
                null, // passwordEncoder no se usa en generateToken
                jwtService
        );
    }

    @Test
    void generateToken_returnsToken_whenUserExists() {
        // Arrange
        String username = "admin";
        Credential credential = Credential.builder()
                .id(1L)
                .username(username)
                .password("hashed")
                .role(RoleType.ROLE_ADMIN)
                .build();

        when(credentialRepository.findByUsername(username)).thenReturn(java.util.Optional.of(credential));
        when(jwtService.generateToken(credential)).thenReturn("mocked-jwt-token");

        // Act
        TokenResponseDto token = credentialService.generateToken(username);

        // Assert
        assertThat(token).isEqualTo("mocked-jwt-token");
        verify(credentialRepository).findByUsername(username);
        verify(jwtService).generateToken(credential);
    }

    @Test
    void generateToken_throwsException_whenUserNotFound() {
        // Arrange
        String username = "unknown";
        when(credentialRepository.findByUsername(username)).thenReturn(java.util.Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> credentialService.generateToken(username))
                .isInstanceOf(CredentialNotFoundException.class);
    }
}

