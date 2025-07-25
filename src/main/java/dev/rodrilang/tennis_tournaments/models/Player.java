package dev.rodrilang.tennis_tournaments.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "players")
public class Player implements Comparable<Player> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    ///Será único como regla de negocio en el Service para no interferir con el borrado lógico
    @Column(name = "dni", nullable = false)
    private String dni;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "nationality")
    private String nationality;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "score")
    private Integer score;

    @Column(name = "deleted")
    private Boolean deleted;

    @Override
    public int compareTo(Player o) {
        return this.dni.compareTo(o.dni);
    }

    @PrePersist
    public void onCreate() {
        if (this.score == null) this.score = 0;
        if (this.deleted == null) this.deleted = false;
    }
}
