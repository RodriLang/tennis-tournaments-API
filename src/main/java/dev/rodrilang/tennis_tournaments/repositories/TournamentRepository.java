package dev.rodrilang.tennis_tournaments.repositories;

import dev.rodrilang.tennis_tournaments.models.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Long> {

}
