package dev.rodrilang.tennis_tournaments.repository;

import dev.rodrilang.tennis_tournaments.entities.Credential;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CredentialRepository extends JpaRepository<Credential, Long> {
    Optional<Credential> findByUsername(String username);
}

