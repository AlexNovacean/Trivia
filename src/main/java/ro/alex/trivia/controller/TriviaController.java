package ro.alex.trivia.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import ro.alex.trivia.model.TriviaQuestion;
import ro.alex.trivia.service.TriviaService;

import java.util.List;

@Controller
public class TriviaController {

    private final TriviaService triviaService;

    public TriviaController(TriviaService triviaService) {
        this.triviaService = triviaService;
    }

    @PostMapping("/trivia")
    @ResponseBody
    public void addTrivia(@RequestBody TriviaQuestion question) {
        triviaService.save(question);
    }

    @GetMapping("/trivia")
    @ResponseBody
    public List<TriviaQuestion> getTriviaQuestions() {
        return triviaService.getQuestions();
    }

}
