package dev.rodrilang.tennis_tournaments.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity (name = "results")
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    @CollectionTable(name = "set_scores", joinColumns = @JoinColumn(name = "result_id"))
    private List<SetScore> setsScore = new ArrayList<>();

    @OneToOne(mappedBy = "result")
    private Match match;

}
