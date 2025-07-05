package dev.rodrilang.tennis_tournaments.controllers;

import dev.rodrilang.tennis_tournaments.dtos.request.ResultRequestDto;
import dev.rodrilang.tennis_tournaments.dtos.response.MatchResponseDto;
import dev.rodrilang.tennis_tournaments.dtos.response.PlayerResponseDto;
import dev.rodrilang.tennis_tournaments.services.MatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/matches")
@RequiredArgsConstructor
@Tag(name = "Partidos", description = "Operaciones relacionadas con los partidos del torneo")
public class MatchController {

    private final MatchService matchService;

    @Operation(summary = "Obtener todos los partidos")
    @ApiResponse(responseCode = "200", description = "Listado exitoso de partidos")
    @GetMapping
    public ResponseEntity<List<MatchResponseDto>> getAllMatches() {
        return ResponseEntity.ok(matchService.getAllMatches());
    }

    @Operation(summary = "Obtener partido por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Partido encontrado"),
            @ApiResponse(responseCode = "404", description = "Partido no encontrado")
    })
    @GetMapping("/{matchId}")
    public ResponseEntity<MatchResponseDto> getMatchById(
            @Parameter(description = "ID del partido", example = "1")
            @PathVariable Long matchId) {
        return ResponseEntity.ok(matchService.findMatchById(matchId));
    }

    @Operation(summary = "Asignar resultado a un partido")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Resultado asignado correctamente"),
            @ApiResponse(responseCode = "404", description = "Partido no encontrado o datos inválidos")
    })
    @PatchMapping("/{matchId}/result")
    public ResponseEntity<MatchResponseDto> assignResultToMatch(
            @Parameter(description = "ID del partido", example = "1")
            @PathVariable Long matchId,
            @RequestBody ResultRequestDto resultRequestDto) {
        return ResponseEntity.ok(matchService.addResultToMatch(matchId, resultRequestDto));
    }

    @Operation(summary = "Actualizar el resultado de un partido")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Resultado actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Partido no encontrado")
    })
    @PutMapping("/{matchId}/result")
    public ResponseEntity<MatchResponseDto> updateMatchResult(
            @Parameter(description = "ID del partido", example = "1")
            @PathVariable Long matchId,
            @RequestBody ResultRequestDto resultRequestDto) {
        return ResponseEntity.ok(matchService.updateResult(matchId, resultRequestDto));
    }

    @Operation(summary = "Obtener todos los partidos de un jugador")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado de partidos del jugador"),
            @ApiResponse(responseCode = "404", description = "Jugador no encontrado o sin partidos")
    })
    @GetMapping("/player/{dni}")
    public ResponseEntity<List<MatchResponseDto>> getMatchesByPlayer(
            @Parameter(description = "DNI del jugador", example = "12345678")
            @PathVariable String dni) {
        return ResponseEntity.ok(matchService.getMatchesByPlayer(dni));
    }

    @Operation(summary = "Obtener el ganador de un partido")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ganador obtenido correctamente"),
            @ApiResponse(responseCode = "404", description = "Partido no encontrado o sin resultado")
    })
    @GetMapping("/{matchId}/winner")
    public ResponseEntity<PlayerResponseDto> getMatchWinner(
            @Parameter(description = "ID del partido", example = "1")
            @PathVariable Long matchId) {
        return ResponseEntity.ok(matchService.getWinner(matchId));
    }
}
