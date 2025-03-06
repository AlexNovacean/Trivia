package ro.alex.trivia.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ro.alex.trivia.model.TriviaDto;
import ro.alex.trivia.model.TriviaQuestion;
import ro.alex.trivia.service.TriviaService;

@Controller
public class TriviaController {

    private final TriviaService triviaService;

    public TriviaController(TriviaService triviaService) {
        this.triviaService = triviaService;
    }

    @PostMapping("/trivia")
    public String addTrivia(Model model,
                          @Valid @ModelAttribute TriviaDto triviaDto,
                          BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "trivia";
        }
        return "redirect:/trivia";
    }

    @GetMapping("/trivia")
    public String getTriviaQuestions(Model model) {
        model.addAttribute("questions", triviaService.getQuestions());
        model.addAttribute("question", new TriviaDto());
        return "trivia";
    }

    @DeleteMapping("/trivia")
    public String deleteTriviaQuestion(@RequestBody TriviaQuestion question) {
        triviaService.delete(question);
        return "redirect:/trivia";
    }

}
