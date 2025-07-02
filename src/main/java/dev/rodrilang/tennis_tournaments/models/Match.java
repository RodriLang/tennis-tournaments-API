package dev.rodrilang.tennis_tournaments.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity (name = "matches")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Match implements Comparable<Match> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "player_one_id", nullable = false)
    private Player playerOne;

    @ManyToOne
    @JoinColumn(name = "player_two_id", nullable = false)
    private Player playerTwo;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "result_id")
    private Result result;

    @Override
    public int compareTo(@NonNull Match o) {
        return this.id.compareTo(o.id);
    }

}
