package dev.rodrilang.tennis_tournaments.controllers;

import dev.rodrilang.tennis_tournaments.dtos.request.CredentialRequestDto;
import dev.rodrilang.tennis_tournaments.dtos.request.LoginRequestDto;
import dev.rodrilang.tennis_tournaments.dtos.response.CredentialResponseDto;
import dev.rodrilang.tennis_tournaments.dtos.response.TokenResponseDto;
import dev.rodrilang.tennis_tournaments.services.CredentialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "Registro y login de usuarios")
public class AuthController {

    private final CredentialService credentialService;

    @Operation(summary = "Registrar un nuevo usuario")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario registrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o usuario ya existe")
    })
    @PostMapping("/register")
    public ResponseEntity<CredentialResponseDto> registerCredential(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos para crear una nueva credencial",
                    required = true
            )
            @RequestBody CredentialRequestDto request) {
        return ResponseEntity.ok(credentialService.create(request));
    }


    @Operation(summary = "Iniciar sesión")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login exitoso, token generado"),
            @ApiResponse(responseCode = "401", description = "Credenciales incorrectas")
    })
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody
                    (description = "Credenciales del usuario", required = true)
            @RequestBody LoginRequestDto request) {
        credentialService.login(request);
        return ResponseEntity.ok(credentialService.generateToken(request.username()));
    }
}
