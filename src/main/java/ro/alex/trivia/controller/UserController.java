package ro.alex.trivia.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ro.alex.trivia.model.*;
import ro.alex.trivia.service.ActivationService;
import ro.alex.trivia.service.UserService;

import java.util.Objects;

@Controller
public class UserController {

    private static final String REGISTER = "register";
    private static final String ACTIVATE = "activate";
    private final UserService userService;
    private final ActivationService activationService;

    public UserController(UserService userService, ActivationService activationService) {
        this.userService = userService;
        this.activationService = activationService;
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute(new RegisterDto());
        return REGISTER;
    }

    @PostMapping("/register")
    public String register(Model model,
                           @Valid @ModelAttribute RegisterDto registerDto,
                           BindingResult bindingResult) {
        TriviaUser triviaUser = userService.findByEmail(registerDto.getEmail());
        if (Objects.nonNull(triviaUser)) {
            bindingResult.addError(
                    new FieldError("registerDto", "email", "Email is already in use")
            );
        }

        if(bindingResult.hasErrors()) {
            return REGISTER;
        }

        if (userService.registerUser(registerDto, bindingResult)) {
            ActivationDto activationDto = new ActivationDto();
            activationDto.setEmail(registerDto.getEmail());
            model.addAttribute(activationDto);
            model.addAttribute("firstActivation", true);
            return ACTIVATE;
        }

        return REGISTER;
    }

    @GetMapping("/password")
    public String password(Model model) {
        model.addAttribute(new ChangePasswordDto());
        return "password";
    }

    @PostMapping("/password")
    public String password(Model model,
                           @Valid @ModelAttribute ChangePasswordDto changePasswordDto,
                           BindingResult bindingResult) {
        if (userService.changePassword(changePasswordDto, bindingResult)) {
            model.addAttribute("changed", true);
            return "login";
        }

        return "password";
    }

    @GetMapping("/activate")
    public String activate() {
        return ACTIVATE;
    }

    @PatchMapping("/activate")
    @ResponseBody
    public void generateActivationCode(@RequestBody String email){
        activationService.generateActivationCode(email);
    }

    @PostMapping("/activate")
    public String activate(Model model,
                           @Valid @ModelAttribute ActivationDto activationDto,
                           BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ACTIVATE;
        }

        if (activationService.activateUser(activationDto)) {
            model.addAttribute("activated", true);
            return "login";
        }

        model.addAttribute("failed", true);
        return ACTIVATE;
    }

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", userService.findAll());
        return "users";
    }

    @PatchMapping("/toggleBan")
    @ResponseBody
    public void toggleBan(@RequestBody BanRequest banRequest) {
        userService.toggleBanUser(banRequest);
    }
}
