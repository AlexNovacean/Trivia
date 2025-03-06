package ro.alex.trivia.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ro.alex.trivia.model.TriviaCategory;
import ro.alex.trivia.model.TriviaDifficulty;
import ro.alex.trivia.model.TriviaQuestion;
import ro.alex.trivia.service.TriviaService;

import java.util.Arrays;

@RestController
public class TriviaController {

    private final TriviaService triviaService;

    public TriviaController(TriviaService triviaService) {
        this.triviaService = triviaService;
    }

    @PostMapping("/trivia")
    public ResponseEntity<TriviaQuestion> addTrivia(@RequestBody TriviaQuestion triviaQuestion) {
        return ResponseEntity.ok(triviaService.save(triviaQuestion));
    }

    @PatchMapping("/trivia")
    public TriviaQuestion updateTriviaQuestion(@RequestBody TriviaQuestion triviaQuestion){
        return triviaService.save(triviaQuestion);
    }

    @GetMapping("/trivia")
    public ModelAndView getTriviaQuestions() {
        ModelAndView mav = new ModelAndView("trivia");
        mav.addObject("questions", triviaService.getQuestions());
        mav.addObject("categories", Arrays.stream(TriviaCategory.values()).map(TriviaCategory::name).toList());
        mav.addObject("difficulties", Arrays.stream(TriviaDifficulty.values()).map(TriviaDifficulty::name).toList());
        return mav;
    }

    @DeleteMapping("/trivia")
    public void deleteTriviaQuestion(@RequestBody Integer triviaId) {
        triviaService.delete(triviaId);
    }

}
