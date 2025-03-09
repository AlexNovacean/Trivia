package ro.alex.trivia.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ro.alex.trivia.model.Quiz;
import ro.alex.trivia.model.QuizDto;
import ro.alex.trivia.model.QuizResult;
import ro.alex.trivia.service.JokeService;
import ro.alex.trivia.service.QuizService;

import java.security.Principal;
import java.util.Map;
import java.util.Objects;

@Controller
public class HomeController {

    private final QuizService quizService;
    private final JokeService jokeService;

    public HomeController(QuizService quizService, JokeService jokeService) {
        this.quizService = quizService;
        this.jokeService = jokeService;
    }

    @GetMapping({"", "/", "/home"})
    public String index(Model model, Authentication auth) {
        if (Objects.nonNull(auth) && auth.isAuthenticated() && quizService.hasStartedQuiz(auth.getName())) {
            model.addAttribute("startedQuiz", true);
        } else {
            model.addAttribute("startedQuiz", false);
            model.addAttribute("quiz", new QuizDto());
        }
        model.addAttribute("leaderboard", quizService.getLeaderBoard());
        return "home";
    }

    @PostMapping("/quiz")
    public String quiz(@ModelAttribute QuizDto quiz, Authentication auth) {
        quiz.setEmail(auth.getName());
        quizService.createQuiz(quiz);
        return "redirect:/quiz";
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
        return quizService.solveQuiz(auth.getName(), answers);
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/joke")
    @ResponseBody
    public String getJoke(){
        return jokeService.getJoke();
    }
}
