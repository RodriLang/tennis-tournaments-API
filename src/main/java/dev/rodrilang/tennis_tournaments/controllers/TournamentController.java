package dev.rodrilang.tennis_tournaments.controllers;

import dev.rodrilang.tennis_tournaments.dtos.request.ResultRequestDto;
import dev.rodrilang.tennis_tournaments.dtos.request.RoundResultsRequestDto;
import dev.rodrilang.tennis_tournaments.dtos.request.TournamentRequestDto;
import dev.rodrilang.tennis_tournaments.dtos.response.PlayerResponseDto;
import dev.rodrilang.tennis_tournaments.dtos.response.RoundResponseDto;
import dev.rodrilang.tennis_tournaments.dtos.response.TournamentResponseDto;
import dev.rodrilang.tennis_tournaments.enums.RoundType;
import dev.rodrilang.tennis_tournaments.services.TournamentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/tournaments")
@RequiredArgsConstructor
@Tag(name = "Torneos", description = "Operaciones relacionadas con torneos de tenis")
public class TournamentController {

    private final TournamentService tournamentService;

    @Operation(summary = "Crear un nuevo torneo", description = "Requiere rol ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Torneo creado correctamente",
                    content = @Content(schema = @Schema(implementation = TournamentResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content)
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<TournamentResponseDto> createTournament(
            @RequestBody @Valid TournamentRequestDto tournamentRequestDto) {
        return ResponseEntity.ok(tournamentService.create(tournamentRequestDto));
    }

    @Operation(summary = "Listar todos los torneos")
    @ApiResponse(responseCode = "200", description = "Listado exitoso",
            content = @Content(schema = @Schema(implementation = TournamentResponseDto.class)))
    @GetMapping
    public ResponseEntity<List<TournamentResponseDto>> getAllTournaments() {
        return ResponseEntity.ok(tournamentService.getAll());
    }

    @Operation(summary = "Obtener torneo por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Torneo encontrado",
                    content = @Content(schema = @Schema(implementation = TournamentResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Torneo no encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<TournamentResponseDto> getTournamentById(@PathVariable Long id) {
        return ResponseEntity.ok(tournamentService.findById(id));
    }

    @Operation(summary = "Eliminar torneo por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Torneo eliminado"),
            @ApiResponse(responseCode = "404", description = "Torneo no encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTournament(@PathVariable Long id) {
        tournamentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Iniciar torneo")
    @ApiResponse(responseCode = "200", description = "Torneo iniciado correctamente")
    @PatchMapping("/{id}/start")
    public ResponseEntity<Void> startTournament(@PathVariable Long id) {
        tournamentService.startTournament(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Avanzar a la siguiente ronda del torneo")
    @PatchMapping("/{id}/next-round")
    public ResponseEntity<Void> advanceToNextRound(@PathVariable Long id) {
        tournamentService.advanceToNextRound(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Registrar jugador en torneo")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Jugador registrado correctamente"),
            @ApiResponse(responseCode = "404", description = "Torneo o jugador no encontrado", content = @Content)
    })
    @PostMapping("/{tournamentId}/players/{dni}")
    public ResponseEntity<Void> registerPlayer(
            @PathVariable Long tournamentId,
            @PathVariable String dni) {
        tournamentService.registerPlayerInTournament(tournamentId, dni);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Desinscribir jugador del torneo")
    @ApiResponse(responseCode = "204", description = "Jugador eliminado del torneo")
    @DeleteMapping("/{tournamentId}/players/{dni}")
    public ResponseEntity<Void> unsubscribePlayer(
            @PathVariable Long tournamentId,
            @PathVariable String dni) {
        tournamentService.unsubscribePlayerFromTournament(tournamentId, dni);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Asignar resultado a un partido")
    @PatchMapping("/{tournamentId}/matches/{matchId}/result")
    public ResponseEntity<Void> assignResultToMatch(
            @PathVariable Long tournamentId,
            @PathVariable Long matchId,
            @RequestBody @Valid ResultRequestDto resultRequestDto) {
        tournamentService.assignResultToMatch(tournamentId, matchId, resultRequestDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Asignar resultado a todos los partidos de una ronda")
    @PatchMapping("/{tournamentId}/rounds/{roundType}/results")
    public ResponseEntity<Void> assignResultsToRound(
            @PathVariable Long tournamentId,
            @PathVariable RoundType roundType,
            @RequestBody @Valid RoundResultsRequestDto roundResultsRequestDto) {
        tournamentService.assignResultsToRound(tournamentId, roundType, roundResultsRequestDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Modificar resultado a todos los partidos de una ronda")
    @PutMapping("/{tournamentId}/rounds/{roundType}/results")
    public ResponseEntity<Void> modifyResultsToRound(
            @PathVariable Long tournamentId,
            @PathVariable RoundType roundType,
            @RequestBody @Valid RoundResultsRequestDto roundResultsRequestDto) {
        tournamentService.modifyResultsToRound(tournamentId, roundType, roundResultsRequestDto);
        return ResponseEntity.ok().build();
    }



    @Operation(summary = "Modificar resultado de un partido")
    @PutMapping("/{tournamentId}/matches/{matchId}/result")
    public ResponseEntity<Void> modifyResultToMatch(
            @PathVariable Long tournamentId,
            @PathVariable Long matchId,
            @RequestBody @Valid ResultRequestDto resultRequestDto) {
        tournamentService.modifyResultToMatch(tournamentId, matchId, resultRequestDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Obtener el ganador del torneo")
    @GetMapping("/{tournamentId}/winner")
    public ResponseEntity<PlayerResponseDto> getTournamentWinner(@PathVariable Long tournamentId) {
        return ResponseEntity.ok(tournamentService.getTournamentWinner(tournamentId));
    }

    @Operation(summary = "Obtener rondas del torneo")
    @GetMapping("/{id}/rounds")
    public ResponseEntity<List<RoundResponseDto>> getRoundsOfTournament(@PathVariable Long id) {
        return ResponseEntity.ok(tournamentService.getRoundsOfTournament(id));
    }

    @Operation(summary = "Obtener jugadores del torneo")
    @GetMapping("/{id}/players")
    public ResponseEntity<List<PlayerResponseDto>> getTournamentPlayers(@PathVariable Long id) {
        return ResponseEntity.ok(tournamentService.getTournamentPlayers(id));
    }

    @Operation(summary = "Actualizar información de un torneo")
    @PutMapping("/{id}")
    public ResponseEntity<TournamentResponseDto> updateTournament(
            @PathVariable Long id,
            @RequestBody @Valid TournamentRequestDto tournamentRequestDto) {
        return ResponseEntity.ok(tournamentService.updateTournament(id, tournamentRequestDto));
    }
}
