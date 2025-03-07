package ro.alex.trivia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.alex.trivia.model.TriviaCategory;
import ro.alex.trivia.model.TriviaDifficulty;
import ro.alex.trivia.model.TriviaQuestion;

import java.util.List;
import java.util.Optional;

@Repository
public interface TriviaRepository extends JpaRepository<TriviaQuestion, Integer> {

    Optional<TriviaQuestion> findByQuestion(String question);

    List<TriviaQuestion> findAllByDifficulty(TriviaDifficulty difficulty);

    List<TriviaQuestion> findAllByDifficultyAndCategory(TriviaDifficulty difficulty, TriviaCategory category);
}
