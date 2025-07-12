package dev.rodrilang.tennis_tournaments.models;

import dev.rodrilang.tennis_tournaments.enums.StatusType;
import dev.rodrilang.tennis_tournaments.enums.SurfaceType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.*;

@Entity(name = "tournaments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tournament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String location;

    @Enumerated(EnumType.STRING)
    private SurfaceType surface;

    @Column
    private LocalDate startingDate;

    @Column
    private LocalDate endingDate;

    @Enumerated(EnumType.STRING)
    private StatusType status;

    @ManyToMany
    @JoinTable(
            name = "tournament_players",
            joinColumns = @JoinColumn(name = "tournament_id"),
            inverseJoinColumns = @JoinColumn(name = "player_id")
    )
    private Set<Player> players = new TreeSet<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "tournament_id")
    private List<Round> rounds = new ArrayList<>();

    @PrePersist
    public void onCreate() {
        if(this.status == null) this.status = StatusType.NOT_STARTED;
    }

}
