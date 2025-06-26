package dev.rodrilang.tennis_tournaments.repository;

import dev.rodrilang.tennis_tournaments.entities.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {
}
