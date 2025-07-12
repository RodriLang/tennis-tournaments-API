package dev.rodrilang.tennis_tournaments.services.impl;

import dev.rodrilang.tennis_tournaments.dtos.request.ChangePasswordRequest;
import dev.rodrilang.tennis_tournaments.dtos.request.CredentialRequestDto;
import dev.rodrilang.tennis_tournaments.dtos.request.LoginRequestDto;
import dev.rodrilang.tennis_tournaments.dtos.response.CredentialResponseDto;
import dev.rodrilang.tennis_tournaments.dtos.response.TokenResponseDto;
import dev.rodrilang.tennis_tournaments.enums.RoleType;
import dev.rodrilang.tennis_tournaments.exceptions.CredentialNotFoundException;
import dev.rodrilang.tennis_tournaments.mappers.CredentialMapper;
import dev.rodrilang.tennis_tournaments.models.Credential;
import dev.rodrilang.tennis_tournaments.repositories.CredentialRepository;
import dev.rodrilang.tennis_tournaments.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CredentialServiceImplTest {

    @Mock
    private CredentialRepository credentialRepository;

    @Mock
    private CredentialMapper credentialMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private CredentialServiceImpl credentialService;

    private CredentialRequestDto credentialRequestDto;
    private CredentialResponseDto credentialResponseDto;
    private Credential credential;
    private LoginRequestDto loginRequestDto;
    private ChangePasswordRequest changePasswordRequest;

    private static final String USERNAME = "testuser";
    private static final String PASSWORD = "testpassword";
    private static final String ENCODED_PASSWORD = "encodedPassword";
    private static final String NEW_PASSWORD = "newPassword";
    private static final String ENCODED_NEW_PASSWORD = "encodedNewPassword";
    private static final String JWT_TOKEN = "jwt.token.here";
    private static final Long CREDENTIAL_ID = 1L;

    @BeforeEach
    void setUp() {
        credentialRequestDto = new CredentialRequestDto(USERNAME, PASSWORD, RoleType.ROLE_PLAYER);
        credentialResponseDto = new CredentialResponseDto(USERNAME, RoleType.ROLE_PLAYER.name());
        loginRequestDto = new LoginRequestDto(USERNAME, PASSWORD);
        changePasswordRequest = new ChangePasswordRequest(USERNAME, PASSWORD, NEW_PASSWORD);

        credential = new Credential();
        credential.setId(CREDENTIAL_ID);
        credential.setUsername(USERNAME);
        credential.setPassword(ENCODED_PASSWORD);
        credential.setRole(RoleType.ROLE_PLAYER);
    }

    @Test
    @DisplayName("Should create credential successfully")
    void create_ShouldCreateCredentialSuccessfully() {
        // Given
        when(credentialMapper.toEntity(credentialRequestDto)).thenReturn(credential);
        when(passwordEncoder.encode(PASSWORD)).thenReturn(ENCODED_PASSWORD);
        when(credentialRepository.save(credential)).thenReturn(credential);
        when(credentialMapper.toDto(credential)).thenReturn(credentialResponseDto);

        // When
        CredentialResponseDto result = credentialService.create(credentialRequestDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.username()).isEqualTo(USERNAME);
        assertThat(result.role()).isEqualTo(RoleType.ROLE_PLAYER.name());

        verify(passwordEncoder).encode(PASSWORD);
        verify(credentialRepository).save(credential);
        assertThat(credential.getPassword()).isEqualTo(ENCODED_PASSWORD);
    }

    @Test
    @DisplayName("Should login successfully with valid credentials")
    void login_ShouldLoginSuccessfully_WhenCredentialsAreValid() {
        // Given
        when(credentialRepository.findByUsername(USERNAME)).thenReturn(Optional.of(credential));
        when(passwordEncoder.matches(PASSWORD, ENCODED_PASSWORD)).thenReturn(true);

        // When & Then
        assertThatCode(() -> credentialService.login(loginRequestDto))
                .doesNotThrowAnyException();

        verify(credentialRepository).findByUsername(USERNAME);
        verify(passwordEncoder).matches(PASSWORD, ENCODED_PASSWORD);
    }

    @Test
    @DisplayName("Should throw CredentialNotFoundException when username not found during login")
    void login_ShouldThrowCredentialNotFoundException_WhenUsernameNotFound() {
        // Given
        when(credentialRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> credentialService.login(loginRequestDto))
                .isInstanceOf(CredentialNotFoundException.class);

        verify(credentialRepository).findByUsername(USERNAME);
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    @DisplayName("Should throw CredentialNotFoundException when password is invalid during login")
    void login_ShouldThrowCredentialNotFoundException_WhenPasswordIsInvalid() {
        // Given
        when(credentialRepository.findByUsername(USERNAME)).thenReturn(Optional.of(credential));
        when(passwordEncoder.matches(PASSWORD, ENCODED_PASSWORD)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> credentialService.login(loginRequestDto))
                .isInstanceOf(CredentialNotFoundException.class);

        verify(credentialRepository).findByUsername(USERNAME);
        verify(passwordEncoder).matches(PASSWORD, ENCODED_PASSWORD);
    }

    @Test
    @DisplayName("Should generate token successfully")
    void generateToken_ShouldGenerateTokenSuccessfully() {
        // Given
        when(credentialRepository.findByUsername(USERNAME)).thenReturn(Optional.of(credential));
        when(jwtService.generateToken(credential)).thenReturn(JWT_TOKEN);

        // When
        TokenResponseDto result = credentialService.generateToken(USERNAME);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.token()).isEqualTo(JWT_TOKEN);

        verify(credentialRepository).findByUsername(USERNAME);
        verify(jwtService).generateToken(credential);
    }

    @Test
    @DisplayName("Should throw CredentialNotFoundException when generating token for non-existent username")
    void generateToken_ShouldThrowCredentialNotFoundException_WhenUsernameNotFound() {
        // Given
        when(credentialRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> credentialService.generateToken(USERNAME))
                .isInstanceOf(CredentialNotFoundException.class);

        verify(credentialRepository).findByUsername(USERNAME);
        verify(jwtService, never()).generateToken(any());
    }

    @Test
    @DisplayName("Should delete credential successfully")
    void delete_ShouldDeleteCredentialSuccessfully() {
        // Given
        when(credentialRepository.findById(CREDENTIAL_ID)).thenReturn(Optional.of(credential));

        // When
        credentialService.delete(CREDENTIAL_ID);

        // Then
        verify(credentialRepository).findById(CREDENTIAL_ID);
        verify(credentialRepository).delete(credential);
    }

    @Test
    @DisplayName("Should throw CredentialNotFoundException when deleting non-existent credential")
    void delete_ShouldThrowCredentialNotFoundException_WhenCredentialNotFound() {
        // Given
        when(credentialRepository.findById(CREDENTIAL_ID)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> credentialService.delete(CREDENTIAL_ID))
                .isInstanceOf(CredentialNotFoundException.class);

        verify(credentialRepository).findById(CREDENTIAL_ID);
        verify(credentialRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Should get all credentials successfully")
    void getCredentials_ShouldReturnAllCredentials() {
        // Given
        List<Credential> credentials = Collections.singletonList(credential);
        when(credentialRepository.findAll()).thenReturn(credentials);
        when(credentialMapper.toDto(credential)).thenReturn(credentialResponseDto);

        // When
        List<CredentialResponseDto> result = credentialService.getCredentials();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().username()).isEqualTo(USERNAME);

        verify(credentialRepository).findAll();
        verify(credentialMapper).toDto(credential);
    }

    @Test
    @DisplayName("Should return empty list when no credentials exist")
    void getCredentials_ShouldReturnEmptyList_WhenNoCredentialsExist() {
        // Given
        when(credentialRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<CredentialResponseDto> result = credentialService.getCredentials();

        // Then
        assertThat(result).isEmpty();
        verify(credentialRepository).findAll();
        verify(credentialMapper, never()).toDto(any());
    }

    @Test
    @DisplayName("Should update password successfully")
    void updatePassword_ShouldUpdatePasswordSuccessfully() {
        // Given
        when(credentialRepository.findByUsername(USERNAME)).thenReturn(Optional.of(credential));
        when(passwordEncoder.matches(PASSWORD, ENCODED_PASSWORD)).thenReturn(true);
        when(passwordEncoder.encode(NEW_PASSWORD)).thenReturn(ENCODED_NEW_PASSWORD);
        when(credentialRepository.save(credential)).thenReturn(credential);
        when(credentialMapper.toDto(credential)).thenReturn(credentialResponseDto);

        // When
        CredentialResponseDto result = credentialService.updatePassword(changePasswordRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.username()).isEqualTo(USERNAME);
        assertThat(credential.getPassword()).isEqualTo(ENCODED_NEW_PASSWORD);

        verify(credentialRepository).findByUsername(USERNAME);
        verify(passwordEncoder).matches(PASSWORD, ENCODED_PASSWORD);
        verify(passwordEncoder).encode(NEW_PASSWORD);
        verify(credentialRepository).save(credential);
        verify(credentialMapper).toDto(credential);
    }

    @Test
    @DisplayName("Should throw CredentialNotFoundException when updating password for non-existent username")
    void updatePassword_ShouldThrowCredentialNotFoundException_WhenUsernameNotFound() {
        // Given
        when(credentialRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> credentialService.updatePassword(changePasswordRequest))
                .isInstanceOf(CredentialNotFoundException.class);

        verify(credentialRepository).findByUsername(USERNAME);
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(passwordEncoder, never()).encode(anyString());
        verify(credentialRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw CredentialNotFoundException when old password is invalid during password update")
    void updatePassword_ShouldThrowCredentialNotFoundException_WhenOldPasswordIsInvalid() {
        // Given
        when(credentialRepository.findByUsername(USERNAME)).thenReturn(Optional.of(credential));
        when(passwordEncoder.matches(PASSWORD, ENCODED_PASSWORD)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> credentialService.updatePassword(changePasswordRequest))
                .isInstanceOf(CredentialNotFoundException.class);

        verify(credentialRepository).findByUsername(USERNAME);
        verify(passwordEncoder).matches(PASSWORD, ENCODED_PASSWORD);
        verify(passwordEncoder, never()).encode(NEW_PASSWORD);
        verify(credentialRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should verify password encoding in create method")
    void create_ShouldEncodePasswordCorrectly() {
        // Given
        when(credentialMapper.toEntity(credentialRequestDto)).thenReturn(credential);
        when(passwordEncoder.encode(PASSWORD)).thenReturn(ENCODED_PASSWORD);
        when(credentialRepository.save(credential)).thenReturn(credential);
        when(credentialMapper.toDto(credential)).thenReturn(credentialResponseDto);

        // When
        credentialService.create(credentialRequestDto);

        // Then
        verify(passwordEncoder).encode(PASSWORD);
        assertThat(credential.getPassword()).isEqualTo(ENCODED_PASSWORD);
    }

    @Test
    @DisplayName("Should handle different role types in credential creation")
    void create_ShouldHandleDifferentRoleTypes() {
        // Given
        CredentialRequestDto adminCredentialRequest = new CredentialRequestDto(USERNAME, PASSWORD, RoleType.ROLE_ADMIN);
        CredentialResponseDto adminCredentialResponse = new CredentialResponseDto(USERNAME, RoleType.ROLE_ADMIN.name());

        Credential adminCredential = new Credential();
        adminCredential.setId(CREDENTIAL_ID);
        adminCredential.setUsername(USERNAME);
        adminCredential.setPassword(ENCODED_PASSWORD);
        adminCredential.setRole(RoleType.ROLE_ADMIN);

        when(credentialMapper.toEntity(adminCredentialRequest)).thenReturn(adminCredential);
        when(passwordEncoder.encode(PASSWORD)).thenReturn(ENCODED_PASSWORD);
        when(credentialRepository.save(adminCredential)).thenReturn(adminCredential);
        when(credentialMapper.toDto(adminCredential)).thenReturn(adminCredentialResponse);

        // When
        CredentialResponseDto result = credentialService.create(adminCredentialRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.role()).isEqualTo(RoleType.ROLE_ADMIN.name());
        verify(credentialMapper).toEntity(adminCredentialRequest);
        verify(credentialRepository).save(adminCredential);
    }
}