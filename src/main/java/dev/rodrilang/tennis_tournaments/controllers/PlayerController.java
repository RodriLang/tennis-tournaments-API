package dev.rodrilang.tennis_tournaments.controllers;

import dev.rodrilang.tennis_tournaments.dtos.request.PlayerRequestDto;
import dev.rodrilang.tennis_tournaments.dtos.response.PlayerResponseDto;
import dev.rodrilang.tennis_tournaments.services.PlayerService;
import dev.rodrilang.tennis_tournaments.validations.OnCreate;
import dev.rodrilang.tennis_tournaments.validations.OnUpdate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/players")
@RequiredArgsConstructor
@Tag(name = "Jugadores", description = "Operaciones relacionadas con los jugadores")
public class PlayerController {

    private final PlayerService playerService;

    @Operation(summary = "Crear jugador")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Jugador creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<PlayerResponseDto> createPlayer(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del jugador a crear",
                    required = true
            )
            @Validated(OnCreate.class)
            @RequestBody PlayerRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(playerService.create(dto));
    }


    @Operation(summary = "Actualizar datos de un jugador")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Jugador actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Jugador no encontrado")
    })
    @PutMapping("/{dni}")
    public ResponseEntity<PlayerResponseDto> updatePlayer(
            @Parameter(description = "DNI del jugador", example = "12345678")
            @PathVariable String dni,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos actualizados del jugador",
                    required = true
            )
            @Validated(OnUpdate.class)
            @RequestBody PlayerRequestDto dto) {
        return ResponseEntity.ok(playerService.update(dni, dto));
    }


    @Operation(summary = "Ajustar puntos de un jugador")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Puntos ajustados correctamente"),
            @ApiResponse(responseCode = "404", description = "Jugador no encontrado")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{dni}/points")
    public ResponseEntity<PlayerResponseDto> adjustPlayerPoints(
            @Parameter(description = "DNI del jugador", example = "12345678")
            @PathVariable String dni,
            @Parameter(description = "Valor a sumar o restar de los puntos", example = "10")
            @RequestParam Integer delta) {
        return ResponseEntity.ok(playerService.adjustPoints(dni, delta));
    }

    @Operation(summary = "Obtener jugador por DNI")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Jugador encontrado"),
            @ApiResponse(responseCode = "404", description = "Jugador no encontrado")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{dni}")
    public ResponseEntity<PlayerResponseDto> getByDni(
            @Parameter(description = "DNI del jugador", example = "12345678")
            @PathVariable String dni) {
        return ResponseEntity.ok(playerService.getByDni(dni));
    }

    @Operation(summary = "Obtener todos los jugadores")
    @ApiResponse(responseCode = "200", description = "Listado exitoso")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<PlayerResponseDto>> getAll() {
        return ResponseEntity.ok(playerService.getAll());
    }

    @Operation(summary = "Eliminar lógicamente un jugador")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Jugador eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Jugador no encontrado")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{dni}")
    public ResponseEntity<Void> deletePlayer(
            @Parameter(description = "DNI del jugador", example = "12345678")
            @PathVariable String dni) {
        playerService.softDelete(dni);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Restaurar jugador eliminado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Jugador restaurado correctamente"),
            @ApiResponse(responseCode = "404", description = "Jugador no encontrado")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{dni}/restore")
    public ResponseEntity<PlayerResponseDto> restorePlayer(
            @Parameter(description = "DNI del jugador", example = "12345678")
            @PathVariable String dni) {
        return ResponseEntity.ok(playerService.restoreDeletedPlayer(dni));
    }
}
