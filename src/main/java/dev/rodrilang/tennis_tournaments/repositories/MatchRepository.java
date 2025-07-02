package dev.rodrilang.tennis_tournaments.repository;

import dev.rodrilang.tennis_tournaments.entities.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
}
