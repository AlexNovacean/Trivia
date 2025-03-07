package ro.alex.trivia.service;

import org.springframework.stereotype.Service;
import ro.alex.trivia.model.TriviaQuestion;
import ro.alex.trivia.repository.TriviaRepository;

import java.util.List;

@Service
public class TriviaService {

    private final TriviaRepository repository;

    public TriviaService(TriviaRepository repository) {
        this.repository = repository;
    }

    public TriviaQuestion getQuestion(Integer questionId) {
        return repository.getReferenceById(questionId);
    }

    public List<TriviaQuestion> getQuestions() {
        return repository.findAll();
    }

    public TriviaQuestion save(TriviaQuestion triviaQuestion) {
        return repository.save(triviaQuestion);
    }

    public void delete(Integer triviaId) {
        repository.deleteById(triviaId);
    }

}
