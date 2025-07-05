package dev.rodrilang.tennis_tournaments.dtos.request;

import dev.rodrilang.tennis_tournaments.validations.OnCreate;
import dev.rodrilang.tennis_tournaments.validations.OnUpdate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Schema(description = "DTO para la creación o actualización de un jugador")
public record PlayerRequestDto(

        @NotBlank(message = "El DNI no puede estar vacío", groups = {OnCreate.class})
        @Pattern(regexp = "\\d{8}", message = "El DNI debe tener exactamente 8 números",
                groups = {OnCreate.class, OnUpdate.class})
        @Schema(description = "DNI del jugador. Debe contener 8 dígitos", example = "12345678")
        String dni,

        @NotBlank(message = "Se debe ingresar un Nombre", groups = {OnCreate.class})
        @Schema(description = "Nombre del jugador", example = "Juan")
        String name,

        @NotBlank(message = "Se debe ingresar un Apellido", groups = {OnCreate.class})
        @Schema(description = "Apellido del jugador", example = "Pérez")
        String lastName,

        @NotNull(message = "Se debe ingresar la Fecha de Nacimiento", groups = {OnCreate.class})
        @Schema(description = "Fecha de nacimiento del jugador", example = "1995-06-15")
        LocalDate dateOfBirth,

        @Schema(description = "Nacionalidad del jugador", example = "Argentina")
        String nationality
) {}
