package dev.rodrilang.tennis_tournaments.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Schema(description = "DTO para asignar resultados a múltiples partidos de una ronda")
public record RoundResultsRequestDto(

        @Schema(description = "Lista de resultados de los partidos")
        @NotEmpty(message = "La lista de resultados no puede estar vacía")
        List<@Valid MatchResultDto> results
) {
    @Schema(description = "DTO con ID del partido y su resultado")
    public record MatchResultDto(

            @Schema(description = "ID del partido", example = "12")
            @NotNull(message = "El ID del partido es obligatorio")
            Long matchId,

            @Schema(description = "Resultado del partido")
            @NotNull(message = "El resultado del partido es obligatorio")
            @Valid
            ResultRequestDto result
    ) {}
}
