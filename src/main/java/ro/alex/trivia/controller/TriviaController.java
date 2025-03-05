package ro.alex.trivia.controller;

import org.springframework.stereotype.Controller;
import ro.alex.trivia.service.TriviaService;

@Controller
public class TriviaController {

    private final TriviaService triviaService;

    public TriviaController(TriviaService triviaService) {
        this.triviaService = triviaService;
    }
}
