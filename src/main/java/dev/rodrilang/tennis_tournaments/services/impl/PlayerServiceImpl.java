package dev.rodrilang.tennis_tournaments.services.impl;


import dev.rodrilang.tennis_tournaments.dtos.request.CredentialRequestDto;
import dev.rodrilang.tennis_tournaments.dtos.request.PlayerRequestDto;
import dev.rodrilang.tennis_tournaments.dtos.response.PlayerResponseDto;
import dev.rodrilang.tennis_tournaments.enums.RoleType;
import dev.rodrilang.tennis_tournaments.models.Player;
import dev.rodrilang.tennis_tournaments.exceptions.DeletedPlayerException;
import dev.rodrilang.tennis_tournaments.exceptions.DuplicatePlayerException;
import dev.rodrilang.tennis_tournaments.exceptions.PlayerNotFoundException;
import dev.rodrilang.tennis_tournaments.mappers.PlayerMapper;
import dev.rodrilang.tennis_tournaments.services.CredentialService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import dev.rodrilang.tennis_tournaments.repositories.PlayerRepository;
import dev.rodrilang.tennis_tournaments.services.PlayerService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;
    private final PlayerMapper playerMapper;
    private final CredentialService credentialService;


    @Override
    public PlayerResponseDto create(PlayerRequestDto playerRequestDto) {

        this.verifyDni(playerRequestDto.dni());

        //Create default credentials
        credentialService.create(new CredentialRequestDto(
                playerRequestDto.dni(),
                playerRequestDto.dni(),
                RoleType.ROLE_PLAYER));

        return playerMapper.toDto(playerRepository.save(playerMapper.toEntity(playerRequestDto)));
    }

    @Override
    public Player findById(Long id) {
        return playerRepository.findById(id).orElseThrow(() -> new PlayerNotFoundException(id));
    }

    @Override
    public List<PlayerResponseDto> getAll() {
        return playerRepository.findAllByDeletedFalse()
                .stream()
                .map(playerMapper::toDto)
                .toList();
    }

    @Override
    public PlayerResponseDto update(String dni, PlayerRequestDto playerRequestDto) {

        Player existingPlayer = this.findEntityByDni(dni);

        playerMapper.updatePlayerFromDto(playerRequestDto, existingPlayer);

        return playerMapper.toDto(playerRepository.save(existingPlayer));
    }


    @Override
    public void softDelete(String dni) {

        Player foundPlayer = this.findEntityByDni(dni);

        foundPlayer.setDeleted(true);
        playerRepository.save(foundPlayer);
    }

    @Override
    public PlayerResponseDto restoreDeletedPlayer(String dni) {

        Player player = playerRepository.findByDniAndDeleted(dni, true)
                .orElseThrow(() -> new PlayerNotFoundException("No eliminated player was found with the DNI:" + dni));

        player.setDeleted(false);
        playerRepository.save(player);

        return playerMapper.toDto(player);
    }

    @Override
    public PlayerResponseDto getByDni(String dni) {
        return playerMapper.toDto(playerRepository.findByDniAndDeleted(dni, false)
                .orElseThrow(() -> new PlayerNotFoundException(dni)));
    }

    @Override
    public PlayerResponseDto adjustPoints(String dni, int delta) {
        Player player = this.findEntityByDni(dni);

        int newPoints = player.getScore() + delta;
        if (newPoints < 0) {
            throw new IllegalArgumentException("The score cannot be negative");
        }
        player.setScore(newPoints);
        playerRepository.save(player);

        return playerMapper.toDto(player);
    }

    @Override
    public List<PlayerResponseDto> getPlayersOrderedByPoints() {
        return List.of();
    }

    @Override
    public List<PlayerResponseDto> getPlayersByTournamentId(Long tournamentId) {
        return List.of();
    }


    private void verifyDni(String dni) {

        if (Boolean.TRUE.equals(playerRepository.existsByDniAndDeleted(dni, false))) {
            throw new DuplicatePlayerException(dni);
        }

        if (Boolean.TRUE.equals(playerRepository.existsByDniAndDeleted(dni, true))) {
            throw new DeletedPlayerException(dni);
        }
    }

    private Player findEntityByDni(String dni) {

        return playerRepository.findByDniAndDeleted(dni, false)
                .orElseThrow(() -> new PlayerNotFoundException(dni));
    }
}
