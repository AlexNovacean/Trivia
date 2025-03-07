package ro.alex.trivia.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ro.alex.trivia.model.*;
import ro.alex.trivia.service.QuizService;
import ro.alex.trivia.service.UserService;

import java.security.Principal;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

@Controller
public class MainController {

    private final QuizService quizService;

    public MainController(QuizService quizService, UserService userService) {
        this.quizService = quizService;
    }

    @GetMapping({"", "/", "/home"})
    public String index(Model model, Authentication auth) {
        if (Objects.nonNull(auth) && auth.isAuthenticated() && quizService.hasStartedQuiz(auth.getName())) {
            model.addAttribute("startedQuiz", true);
        } else {
            model.addAttribute("startedQuiz", false);
            model.addAttribute("quiz", new QuizDto());
        }
        model.addAttribute("categories", Arrays.stream(TriviaCategory.values()).map(TriviaCategory::name).toList());
        model.addAttribute("difficulties", Arrays.stream(TriviaDifficulty.values()).map(TriviaDifficulty::name).toList());
        model.addAttribute("leaderboard", quizService.getLeaderBoard());
        return "home";
    }

    @PostMapping("/quiz")
    public String quiz(Model model, @ModelAttribute QuizDto quiz, Authentication auth) {
        quiz.setEmail(auth.getName());
        model.addAttribute("quiz", quizService.createQuiz(quiz));
        return "quiz";
    }

    @GetMapping("/quiz")
    public String quiz(Model model, Principal principal) {
        Quiz startedQuiz = quizService.findStartedQuiz(principal.getName());
        model.addAttribute("quiz", startedQuiz);
        return "quiz";
    }

    @PostMapping("/answer-quiz")
    @ResponseBody
    public QuizResult answerQuiz(@RequestBody Map<Integer, String> answers, Authentication auth) {
        return quizService.resolve(auth.getName(), answers);
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
