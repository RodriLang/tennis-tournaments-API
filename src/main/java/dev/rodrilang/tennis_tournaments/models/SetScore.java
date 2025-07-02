package dev.rodrilang.tennis_tournaments.models;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class SetScore {
    private Integer playerOneScore;
    private Integer playerTwoScore;
}
