package dev.rodrilang.tennis_tournaments.service;

import dev.rodrilang.tennis_tournaments.dtos.requests.PlayerRequestDto;
import dev.rodrilang.tennis_tournaments.dtos.responses.PlayerResponseDto;
import dev.rodrilang.tennis_tournaments.entities.Player;

import java.util.List;

public interface PlayerService {

    // Crear un nuevo jugador
    PlayerResponseDto create(PlayerRequestDto playerRequestDto);

    // Obtener un jugador por ID
    Player findById(Long id);

    // Listar todos los jugadores
    List<PlayerResponseDto> getAll();

    // Actualizar un jugador
    PlayerResponseDto update(String dni, PlayerRequestDto playerRequestDto);

    // Eliminar un jugador
    void softDelete(String dni);

    // Restaurar un jugador eliminado
    PlayerResponseDto restoreDeletedPlayer(String dni);

    // Buscar por DNI u otro campo único
    PlayerResponseDto getByDni(String dni);

    // Modificar el puntaje
    PlayerResponseDto adjustPoints(String dni, int points);

    // Ver ranking por puntos
    List<PlayerResponseDto> getPlayersOrderedByPoints();

}
