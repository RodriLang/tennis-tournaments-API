package dev.rodrilang.tennis_tournaments.repositories;

import dev.rodrilang.tennis_tournaments.models.Credential;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CredentialRepository extends JpaRepository<Credential, Long> {
    Optional<Credential> findByUsername(String username);
}

