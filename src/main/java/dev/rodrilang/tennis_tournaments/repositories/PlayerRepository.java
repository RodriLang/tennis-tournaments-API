package dev.rodrilang.tennis_tournaments.repositories;

import dev.rodrilang.tennis_tournaments.models.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

    Optional<Player> findByDniAndDeleted(String dni, Boolean deleted);
    Optional<Player> findByIdAndDeletedFalse(Long id);
    List<Player> findAllByDeletedFalse();
    Boolean existsByDniAndDeleted(String dni, Boolean deleted);
}
