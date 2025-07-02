package dev.rodrilang.tennis_tournaments.controllers;

import dev.rodrilang.tennis_tournaments.dtos.request.ResultRequestDto;
import dev.rodrilang.tennis_tournaments.dtos.request.TournamentRequestDto;
import dev.rodrilang.tennis_tournaments.dtos.response.PlayerResponseDto;
import dev.rodrilang.tennis_tournaments.dtos.response.RoundResponseDto;
import dev.rodrilang.tennis_tournaments.dtos.response.TournamentResponseDto;
import dev.rodrilang.tennis_tournaments.services.TournamentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/tournaments")
@RequiredArgsConstructor
public class TournamentController {

    private final TournamentService tournamentService;

    // Crear torneo
    //@PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<TournamentResponseDto> createTournament(
            @RequestBody @Valid TournamentRequestDto tournamentRequestDto) {
        return ResponseEntity.ok(tournamentService.create(tournamentRequestDto));
    }

    // Obtener todos los torneos
    @GetMapping
    public ResponseEntity<List<TournamentResponseDto>> getAllTournaments() {
        return ResponseEntity.ok(tournamentService.getAll());
    }

    // Obtener torneo por ID
    @GetMapping("/{id}")
    public ResponseEntity<TournamentResponseDto> getTournamentById(@PathVariable Long id) {
        return ResponseEntity.ok(tournamentService.findById(id));
    }

    // Eliminar torneo
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTournament(@PathVariable Long id) {
        tournamentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Iniciar torneo
    @PatchMapping("/{id}/start")
    public ResponseEntity<Void> startTournament(@PathVariable Long id) {
        tournamentService.startTournament(id);
        return ResponseEntity.ok().build();
    }

    // Registrar jugador en torneo
    @PostMapping("/{tournamentId}/players/{dni}")
    public ResponseEntity<Void> registerPlayer(
            @PathVariable Long tournamentId,
            @PathVariable String dni) {
        tournamentService.registerPlayerInTournament(tournamentId, dni);
        return ResponseEntity.ok().build();
    }

    // Desinscribir jugador del torneo
    @DeleteMapping("/{tournamentId}/players/{dni}")
    public ResponseEntity<Void> unsubscribePlayer(
            @PathVariable Long tournamentId,
            @PathVariable String dni) {
        tournamentService.unsubscribePlayerFromTournament(tournamentId, dni);
        return ResponseEntity.noContent().build();
    }

    // Asignar resultado a un partido
    @PatchMapping("/{tournamentId}/matches/{matchId}/result")
    public ResponseEntity<Void> assignResultToMatch(
            @PathVariable Long tournamentId,
            @PathVariable Long matchId,
            @RequestBody @Valid ResultRequestDto resultRequestDto) {
        tournamentService.assignResultToMatch(tournamentId, matchId, resultRequestDto);
        return ResponseEntity.ok().build();
    }

    // Modificar resultado de un partido
    @PutMapping("/{tournamentId}/matches/{matchId}/result")
    public ResponseEntity<Void> modifyResultToMatch(
            @PathVariable Long tournamentId,
            @PathVariable Long matchId,
            @RequestBody @Valid ResultRequestDto resultRequestDto) {
        tournamentService.modifyResultToMatch(tournamentId, matchId, resultRequestDto);
        return ResponseEntity.ok().build();
    }

    // Obtener ganador del torneo
    @GetMapping("/{tournamentId}/winner")
    public ResponseEntity<PlayerResponseDto> getTournamentWinner(
            @PathVariable Long tournamentId) {
        return ResponseEntity.ok(tournamentService.getTournamentWinner(tournamentId));
    }

    // Obtener rondas del torneo
    @GetMapping("/{id}/rounds")
    public ResponseEntity<List<RoundResponseDto>> getRoundsOfTournament(
            @PathVariable Long id) {
        return ResponseEntity.ok(tournamentService.getRoundsOfTournament(id));
    }

    // (Opcional) Obtener jugadores del torneo
    @GetMapping("/{id}/players")
    public ResponseEntity<List<PlayerResponseDto>> getTournamentPlayers(
            @PathVariable Long id) {
        return ResponseEntity.ok(tournamentService.getTournamentPlayers(id));
    }

    // (Opcional) Actualizar datos del torneo
    @PutMapping("/{id}")
    public ResponseEntity<TournamentResponseDto> updateTournament(
            @PathVariable Long id,
            @RequestBody @Valid TournamentRequestDto tournamentRequestDto) {
        return ResponseEntity.ok(tournamentService.updateTournament(tournamentRequestDto));
    }
}
