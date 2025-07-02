package dev.rodrilang.tennis_tournaments.repositories;

import dev.rodrilang.tennis_tournaments.models.Round;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoundRepository extends JpaRepository<Round, Long> {

    List<Round> findRoundByTournamentId(Long tournamentId);
}
