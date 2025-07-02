package dev.rodrilang.tennis_tournaments.repositories;

import dev.rodrilang.tennis_tournaments.models.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {

    List<Match> getMatchesByPlayerOne_DniOrPlayerTwo_Dni(String playerOneDni, String playerTwoDni);
}
