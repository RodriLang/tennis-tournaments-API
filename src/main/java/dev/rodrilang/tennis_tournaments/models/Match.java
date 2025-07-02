package dev.rodrilang.tennis_tournaments.models;

import dev.rodrilang.tennis_tournaments.enums.StatusType;
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

    @Enumerated(EnumType.STRING)
    private StatusType status;

    @ManyToOne
    @JoinColumn(name = "round_id", nullable = false)
    private Round round;


    @Override
    public int compareTo(@NonNull Match o) {
        return this.id.compareTo(o.id);
    }

}
