package dev.rodrilang.tennis_tournaments.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import dev.rodrilang.tennis_tournaments.validations.OnCreate;
import dev.rodrilang.tennis_tournaments.validations.OnUpdate;

import java.time.LocalDate;

public record PlayerRequestDto(

        @NotBlank(message = "El DNI no puede estar vacío", groups = {OnCreate.class})
        @Pattern(regexp = "\\d{8}", message = "El DNI debe tener exactamente 8 números",
                groups = {OnCreate.class, OnUpdate.class})
        @Size(min = 8, max = 8)
        String dni,

        @NotBlank(message = "Se debe ingresar un Nombre", groups = {OnCreate.class})
        String name,

        @NotBlank(message = "Se debe ingresar un Apellido", groups = {OnCreate.class})
        String lastName,

        @NotNull(message = "Se debe ingresar la Fecha de Nacimiento", groups = {OnCreate.class})
        LocalDate dateOfBirth,

        String nationality) {
}
