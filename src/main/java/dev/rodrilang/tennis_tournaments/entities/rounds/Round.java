package dev.rodrilang.tennis_tournaments.entities.rounds;

import dev.rodrilang.tennis_tournaments.entities.Match;
import dev.rodrilang.tennis_tournaments.entities.Tournament;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // o TABLE_PER_CLASS / JOINED
@DiscriminatorColumn(name = "round_type", discriminatorType = DiscriminatorType.STRING)

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class Round {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "round_id")
    protected List<Match> matches = new ArrayList<>();

    private Integer givenPoints;

    @ManyToOne
    @JoinColumn(name = "tournament_id")
    private Tournament tournament;

}
