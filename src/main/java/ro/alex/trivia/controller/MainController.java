package ro.alex.trivia.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping({"", "/"})
    public String index() {
        return "home";
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/hello")
    public String hello(/*Model model, Authentication auth*/) {
//        model.addAttribute("username", auth.getName());
//        model.addAttribute("role", auth.getAuthorities().toString());
        return "hello";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
