package ro.alex.trivia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.alex.trivia.model.ScoreBoard;

import java.util.Optional;

@Repository
public interface ScoreBoardRepository extends JpaRepository<ScoreBoard, Integer> {
    Optional<ScoreBoard> findByEmail(String email);
}
