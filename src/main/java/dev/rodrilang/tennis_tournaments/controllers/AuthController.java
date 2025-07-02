package dev.rodrilang.tennis_tournaments.controllers;

import dev.rodrilang.tennis_tournaments.dtos.request.CredentialRequestDto;
import dev.rodrilang.tennis_tournaments.dtos.request.LoginRequestDto;
import dev.rodrilang.tennis_tournaments.dtos.response.CredentialResponseDto;
import dev.rodrilang.tennis_tournaments.dtos.response.TokenResponseDto;
import dev.rodrilang.tennis_tournaments.services.CredentialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final CredentialService credentialService;

    @PostMapping("/register")
    public ResponseEntity<CredentialResponseDto> login(@RequestBody CredentialRequestDto request) {
        return ResponseEntity.ok(credentialService.create(request));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody LoginRequestDto request) {
        credentialService.login(request);
        return ResponseEntity.ok(credentialService.generateToken(request.username()));
    }


}
