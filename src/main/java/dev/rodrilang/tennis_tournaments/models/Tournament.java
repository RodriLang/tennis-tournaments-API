package dev.rodrilang.tennis_tournaments.models;

import dev.rodrilang.tennis_tournaments.enums.StatusType;
import dev.rodrilang.tennis_tournaments.enums.SurfaceType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "tournaments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tournament implements Comparable<Tournament> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String location;

    @Enumerated(EnumType.STRING)
    private SurfaceType surface;

    @Column(name = "starting_date")
    private LocalDate startingDate;

    @Column(name = "ending_date")
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

    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Round> rounds = new ArrayList<>();

    @PrePersist
    public void onCreate() {
        if(this.status == null) this.status = StatusType.NOT_STARTED;
    }

    @Override
    public int compareTo(Tournament o) {
        return this.id.compareTo(o.getId());
    }
}
