package dev.rodrilang.tennis_tournaments.models.rounds;

import dev.rodrilang.tennis_tournaments.enums.RoundType;
import dev.rodrilang.tennis_tournaments.models.Match;
import dev.rodrilang.tennis_tournaments.models.Tournament;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rounds")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Round {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RoundType type;

    private Integer givenPoints;

    @ManyToOne
    @JoinColumn(name = "tournament_id", nullable = false)
    private Tournament tournament;

    @OneToMany(mappedBy = "round", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Match> matches = new ArrayList<>();
}
