package dev.rodrilang.tennis_tournaments.repository;

import dev.rodrilang.tennis_tournaments.entities.rounds.Round;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoundRepository extends JpaRepository<Round, Long> {
}
