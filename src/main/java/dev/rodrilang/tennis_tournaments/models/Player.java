package dev.rodrilang.tennis_tournaments.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    @Column(name = "points")
    private Integer points;

    @Column(name = "deleted")
    private Boolean deleted;

    @Override
    public int compareTo(Player o) {
        return this.dni.compareTo(o.dni);
    }

    @PrePersist
    public void onCreate() {
        if (this.points == null) this.points = 0;
        if (this.deleted == null) this.deleted = false;
    }
}
