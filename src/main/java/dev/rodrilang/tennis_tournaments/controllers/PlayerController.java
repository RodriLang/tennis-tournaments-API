package dev.rodrilang.tennis_tournaments.controllers;

import dev.rodrilang.tennis_tournaments.dtos.requests.PlayerRequestDto;
import dev.rodrilang.tennis_tournaments.dtos.responses.PlayerResponseDto;
import dev.rodrilang.tennis_tournaments.service.PlayerService;
import dev.rodrilang.tennis_tournaments.validations.OnCreate;
import dev.rodrilang.tennis_tournaments.validations.OnUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/players")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;

    @PostMapping
    public ResponseEntity<PlayerResponseDto> createPlayer(
            @Validated(OnCreate.class)
            @RequestBody PlayerRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(playerService.create(dto));
    }

    @PutMapping("/{dni}")
    public ResponseEntity<PlayerResponseDto> updatePlayer(
            @PathVariable String dni,
            @Validated(OnUpdate.class)
            @RequestBody PlayerRequestDto dto) {
        return ResponseEntity.ok(playerService.update(dni, dto));
    }

    @PatchMapping("/{dni}/points")
    public ResponseEntity<PlayerResponseDto> adjustPlayerPoints(
            @PathVariable String dni,
            @RequestParam Integer delta) {
        return ResponseEntity.ok(playerService.adjustPoints(dni, delta));
    }


    @GetMapping("/{dni}")
    public ResponseEntity<PlayerResponseDto> getByDni(@PathVariable String dni) {
        return ResponseEntity.ok(playerService.getByDni(dni));
    }

    @GetMapping
    public ResponseEntity<List<PlayerResponseDto>> getAll() {
        return ResponseEntity.ok(playerService.getAll());
    }

    @DeleteMapping("/{dni}")
    public ResponseEntity<Void> deletePlayer(@PathVariable String dni) {
        playerService.softDelete(dni);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{dni}/restore")
    public ResponseEntity<PlayerResponseDto> restorePlayer(@PathVariable String dni) {
        return ResponseEntity.ok(playerService.restoreDeletedPlayer(dni));
    }
}
