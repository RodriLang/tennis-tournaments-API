package dev.rodrilang.tennis_tournaments.services.impl;

import dev.rodrilang.tennis_tournaments.dtos.request.CredentialRequestDto;
import dev.rodrilang.tennis_tournaments.dtos.request.PlayerRequestDto;
import dev.rodrilang.tennis_tournaments.dtos.response.PlayerResponseDto;
import dev.rodrilang.tennis_tournaments.enums.RoleType;
import dev.rodrilang.tennis_tournaments.exceptions.DeletedPlayerException;
import dev.rodrilang.tennis_tournaments.exceptions.DuplicatePlayerException;
import dev.rodrilang.tennis_tournaments.exceptions.PlayerNotFoundException;
import dev.rodrilang.tennis_tournaments.mappers.PlayerMapper;
import dev.rodrilang.tennis_tournaments.models.Player;
import dev.rodrilang.tennis_tournaments.repositories.PlayerRepository;
import dev.rodrilang.tennis_tournaments.services.CredentialService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayerServiceImplTest {

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private PlayerMapper playerMapper;

    @Mock
    private CredentialService credentialService;

    @InjectMocks
    private PlayerServiceImpl playerService;

    private PlayerRequestDto playerRequestDto;
    private PlayerResponseDto playerResponseDto;
    private Player player;
    private static final String DNI = "12345678";
    private static final Long PLAYER_ID = 1L;

    @BeforeEach
    void setUp() {
        playerRequestDto = new PlayerRequestDto(DNI, "John Doe", "john@example.com", LocalDate.of(1990, 3, 5), "Argentino");
        playerResponseDto = new PlayerResponseDto(DNI, "John Doe", "john@example.com", 25, LocalDate.of(1990, 3, 5), "Argentino");
        player = new Player();
        player.setId(PLAYER_ID);
        player.setDni(DNI);
        player.setName("John Doe");
        player.setDateOfBirth(LocalDate.of(1990, 3, 5));
        player.setNationality("Argentino");
        player.setScore(1000);
        player.setDeleted(false);
    }

    @Test
    @DisplayName("Should create player successfully")
    void create_ShouldCreatePlayerSuccessfully() {
        // Given
        when(playerRepository.existsByDniAndDeleted(DNI, false)).thenReturn(false);
        when(playerRepository.existsByDniAndDeleted(DNI, true)).thenReturn(false);
        when(playerMapper.toEntity(playerRequestDto)).thenReturn(player);
        when(playerRepository.save(player)).thenReturn(player);
        when(playerMapper.toDto(player)).thenReturn(playerResponseDto);

        // When
        PlayerResponseDto result = playerService.create(playerRequestDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.dni()).isEqualTo(DNI);
        assertThat(result.name()).isEqualTo("John Doe");

        // Verify credential creation
        ArgumentCaptor<CredentialRequestDto> credentialCaptor = ArgumentCaptor.forClass(CredentialRequestDto.class);
        verify(credentialService).create(credentialCaptor.capture());

        CredentialRequestDto capturedCredential = credentialCaptor.getValue();
        assertThat(capturedCredential.username()).isEqualTo(DNI);
        assertThat(capturedCredential.password()).isEqualTo(DNI);
        assertThat(capturedCredential.role()).isEqualTo(RoleType.ROLE_PLAYER);
    }

    @Test
    @DisplayName("Should throw DuplicatePlayerException when player already exists")
    void create_ShouldThrowDuplicatePlayerException_WhenPlayerAlreadyExists() {
        // Given
        when(playerRepository.existsByDniAndDeleted(DNI, false)).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> playerService.create(playerRequestDto))
                .isInstanceOf(DuplicatePlayerException.class);

        verify(credentialService, never()).create(any());
        verify(playerRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw DeletedPlayerException when trying to create player with deleted DNI")
    void create_ShouldThrowDeletedPlayerException_WhenPlayerWasDeleted() {
        // Given
        when(playerRepository.existsByDniAndDeleted(DNI, false)).thenReturn(false);
        when(playerRepository.existsByDniAndDeleted(DNI, true)).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> playerService.create(playerRequestDto))
                .isInstanceOf(DeletedPlayerException.class);

        verify(credentialService, never()).create(any());
        verify(playerRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should find player by ID successfully")
    void findById_ShouldReturnPlayer_WhenPlayerExists() {
        // Given
        when(playerRepository.findById(PLAYER_ID)).thenReturn(Optional.of(player));

        // When
        Player result = playerService.findById(PLAYER_ID);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(PLAYER_ID);
        assertThat(result.getDni()).isEqualTo(DNI);
    }

    @Test
    @DisplayName("Should throw PlayerNotFoundException when player not found by ID")
    void findById_ShouldThrowPlayerNotFoundException_WhenPlayerNotFound() {
        // Given
        when(playerRepository.findById(PLAYER_ID)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> playerService.findById(PLAYER_ID))
                .isInstanceOf(PlayerNotFoundException.class);
    }

    @Test
    @DisplayName("Should return all active players")
    void getAll_ShouldReturnAllActivePlayers() {
        // Given
        List<Player> players = Collections.singletonList(player);
        when(playerRepository.findAllByDeletedFalse()).thenReturn(players);
        when(playerMapper.toDto(player)).thenReturn(playerResponseDto);

        // When
        List<PlayerResponseDto> result = playerService.getAll();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().dni()).isEqualTo(DNI);
    }

    @Test
    @DisplayName("Should update player successfully")
    void update_ShouldUpdatePlayerSuccessfully() {
        // Given
        PlayerRequestDto updateDto = new PlayerRequestDto(DNI, "Updated Name", "updated@example.com", LocalDate.of(1990, 3, 5), "Updated nationality");
        when(playerRepository.findByDniAndDeleted(DNI, false)).thenReturn(Optional.of(player));
        when(playerRepository.save(player)).thenReturn(player);
        when(playerMapper.toDto(player)).thenReturn(playerResponseDto);

        // When
        PlayerResponseDto result = playerService.update(DNI, updateDto);

        // Then
        assertThat(result).isNotNull();
        verify(playerMapper).updatePlayerFromDto(updateDto, player);
        verify(playerRepository).save(player);
    }

    @Test
    @DisplayName("Should throw PlayerNotFoundException when updating non-existent player")
    void update_ShouldThrowPlayerNotFoundException_WhenPlayerNotFound() {
        // Given
        PlayerRequestDto updateDto = new PlayerRequestDto(DNI, "Updated Name", "updated@example.com", LocalDate.of(2000, 10, 1),"Updated nationality" );
        when(playerRepository.findByDniAndDeleted(DNI, false)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> playerService.update(DNI, updateDto))
                .isInstanceOf(PlayerNotFoundException.class);
    }

    @Test
    @DisplayName("Should soft delete player successfully")
    void softDelete_ShouldMarkPlayerAsDeleted() {
        // Given
        when(playerRepository.findByDniAndDeleted(DNI, false)).thenReturn(Optional.of(player));

        // When
        playerService.softDelete(DNI);

        // Then
        assertThat(player.getDeleted()).isTrue();
        verify(playerRepository).save(player);
    }

    @Test
    @DisplayName("Should throw PlayerNotFoundException when soft deleting non-existent player")
    void softDelete_ShouldThrowPlayerNotFoundException_WhenPlayerNotFound() {
        // Given
        when(playerRepository.findByDniAndDeleted(DNI, false)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> playerService.softDelete(DNI))
                .isInstanceOf(PlayerNotFoundException.class);
    }

    @Test
    @DisplayName("Should restore deleted player successfully")
    void restoreDeletedPlayer_ShouldRestorePlayerSuccessfully() {
        // Given
        player.setDeleted(true);
        when(playerRepository.findByDniAndDeleted(DNI, true)).thenReturn(Optional.of(player));
        when(playerMapper.toDto(player)).thenReturn(playerResponseDto);

        // When
        PlayerResponseDto result = playerService.restoreDeletedPlayer(DNI);

        // Then
        assertThat(result).isNotNull();
        assertThat(player.getDeleted()).isFalse();
        verify(playerRepository).save(player);
    }

    @Test
    @DisplayName("Should throw PlayerNotFoundException when restoring non-deleted player")
    void restoreDeletedPlayer_ShouldThrowPlayerNotFoundException_WhenPlayerNotDeleted() {
        // Given
        when(playerRepository.findByDniAndDeleted(DNI, true)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> playerService.restoreDeletedPlayer(DNI))
                .isInstanceOf(PlayerNotFoundException.class);
    }

    @Test
    @DisplayName("Should get player by DNI successfully")
    void getByDni_ShouldReturnPlayer_WhenPlayerExists() {
        // Given
        when(playerRepository.findByDniAndDeleted(DNI, false)).thenReturn(Optional.of(player));
        when(playerMapper.toDto(player)).thenReturn(playerResponseDto);

        // When
        PlayerResponseDto result = playerService.getByDni(DNI);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.dni()).isEqualTo(DNI);
    }

    @Test
    @DisplayName("Should throw PlayerNotFoundException when getting player by non-existent DNI")
    void getByDni_ShouldThrowPlayerNotFoundException_WhenPlayerNotFound() {
        // Given
        when(playerRepository.findByDniAndDeleted(DNI, false)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> playerService.getByDni(DNI))
                .isInstanceOf(PlayerNotFoundException.class);
    }

    @Test
    @DisplayName("Should adjust points successfully when result is positive")
    void adjustPoints_ShouldAdjustPointsSuccessfully_WhenResultIsPositive() {
        // Given
        int delta = 100;
        int initialScore = 1000;
        player.setScore(initialScore);
        when(playerRepository.findByDniAndDeleted(DNI, false)).thenReturn(Optional.of(player));
        when(playerRepository.save(player)).thenReturn(player);
        when(playerMapper.toDto(player)).thenReturn(playerResponseDto);

        // When
        PlayerResponseDto result = playerService.adjustPoints(DNI, delta);

        // Then
        assertThat(result).isNotNull();
        assertThat(player.getScore()).isEqualTo(initialScore + delta);
        verify(playerRepository).save(player);
    }

    @Test
    @DisplayName("Should adjust points successfully when result is zero")
    void adjustPoints_ShouldAdjustPointsSuccessfully_WhenResultIsZero() {
        // Given
        int delta = -1000;
        int initialScore = 1000;
        player.setScore(initialScore);
        when(playerRepository.findByDniAndDeleted(DNI, false)).thenReturn(Optional.of(player));
        when(playerRepository.save(player)).thenReturn(player);
        when(playerMapper.toDto(player)).thenReturn(playerResponseDto);

        // When
        PlayerResponseDto result = playerService.adjustPoints(DNI, delta);

        // Then
        assertThat(result).isNotNull();
        assertThat(player.getScore()).isZero();
        verify(playerRepository).save(player);
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when adjusting points results in negative score")
    void adjustPoints_ShouldThrowIllegalArgumentException_WhenResultIsNegative() {
        // Given
        int delta = -1500;
        int initialScore = 1000;
        player.setScore(initialScore);
        when(playerRepository.findByDniAndDeleted(DNI, false)).thenReturn(Optional.of(player));

        // When & Then
        assertThatThrownBy(() -> playerService.adjustPoints(DNI, delta))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("The score cannot be negative");

        verify(playerRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw PlayerNotFoundException when adjusting points for non-existent player")
    void adjustPoints_ShouldThrowPlayerNotFoundException_WhenPlayerNotFound() {
        // Given
        when(playerRepository.findByDniAndDeleted(DNI, false)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> playerService.adjustPoints(DNI, 100))
                .isInstanceOf(PlayerNotFoundException.class);
    }

    @Test
    @DisplayName("Should return empty list for getPlayersOrderedByPoints - method not implemented")
    void getPlayersOrderedByPoints_ShouldReturnEmptyList() {
        // When
        List<PlayerResponseDto> result = playerService.getPlayersOrderedByPoints();

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should return empty list for getPlayersByTournamentId - method not implemented")
    void getPlayersByTournamentId_ShouldReturnEmptyList() {
        // When
        List<PlayerResponseDto> result = playerService.getPlayersByTournamentId(1L);

        // Then
        assertThat(result).isEmpty();
    }
}