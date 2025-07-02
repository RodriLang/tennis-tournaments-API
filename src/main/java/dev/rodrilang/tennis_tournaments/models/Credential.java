package dev.rodrilang.tennis_tournaments.models;

import dev.rodrilang.tennis_tournaments.enums.RoleType;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "credentials")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Credential {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private RoleType role;
}