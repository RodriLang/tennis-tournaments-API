package dev.rodrilang.tennis_tournaments.repository;

import dev.rodrilang.tennis_tournaments.entities.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Long> {
}
