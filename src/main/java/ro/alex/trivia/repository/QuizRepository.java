package ro.alex.trivia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ro.alex.trivia.model.Quiz;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
    Optional<Quiz> findByEmailAndScoreNull(String email);
    List<Quiz> findAllByEmail(String email);

    @Modifying
    @Query("update Quiz q set q.score=?1 where q.id=?2")
    void updateScore(Double score, Long id);
}
