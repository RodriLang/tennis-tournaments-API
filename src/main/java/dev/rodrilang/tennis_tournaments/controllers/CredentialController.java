package dev.rodrilang.tennis_tournaments.controllers;

import dev.rodrilang.tennis_tournaments.dtos.request.ChangePasswordRequest;
import dev.rodrilang.tennis_tournaments.dtos.request.CredentialRequestDto;
import dev.rodrilang.tennis_tournaments.dtos.response.CredentialResponseDto;
import dev.rodrilang.tennis_tournaments.services.CredentialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/credentials")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "Registro y login de usuarios")
public class CredentialController {

    private final CredentialService credentialService;

    @Operation(summary = "Registrar un nuevo usuario")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuario registrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o usuario ya existe")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'ROOT')")
    @PostMapping("/register")
    public ResponseEntity<CredentialResponseDto> registerCredential(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos para crear una nueva credencial",
                    required = true
            )
            @RequestBody CredentialRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(credentialService.create(request));
    }


    @Operation(summary = "Obtener la lista de usuarios")
    @ApiResponse(responseCode = "200", description = "Listado exitoso")
    @PreAuthorize("hasRole('ROOT')")
    @GetMapping()
    public ResponseEntity<List<CredentialResponseDto>> getAllCredentials() {
        return ResponseEntity.ok(credentialService.getCredentials());
    }

    @Operation(summary = "Eliminar un usuario")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuario borrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PreAuthorize("hasRole('ROOT')")
    @DeleteMapping("/revoke/{id}")
    public ResponseEntity<Void> revokeCredential(
            @PathVariable Long id) {
        credentialService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Eliminar un usuario")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuario borrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PatchMapping("/change-password")
    public ResponseEntity<CredentialResponseDto> updatePassword(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Verificación de contraseña antigua y nueva",
                    required = true
            )
            @RequestBody ChangePasswordRequest changePasswordRequest) {
       return ResponseEntity.ok(credentialService.updatePassword(changePasswordRequest));
    }

}
