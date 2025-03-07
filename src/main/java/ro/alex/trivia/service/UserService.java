package ro.alex.trivia.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import ro.alex.trivia.model.*;
import ro.alex.trivia.repository.UserRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ActivationService activationService;
    private final String adminEmail;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       ActivationService activationService, @Value("${trivia.admin.email:admin@trivia.ro}") String adminEmail) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.activationService = activationService;
        this.adminEmail = adminEmail;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<TriviaUser> optionalUser = userRepository.findByEmail(username);

        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("No user found with username: " + username);
        }

        TriviaUser triviaUser = optionalUser.get();
        return User.withUsername(triviaUser.getEmail())
                .password(triviaUser.getPassword())
                .roles(triviaUser.getRole().name())
                .accountLocked(triviaUser.getAccountLocked())
                .disabled(triviaUser.getAccountDisabled())
                .build();
    }

    public TriviaUser findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public boolean registerUser(RegisterDto registerDto, BindingResult bindingResult) {
        try {

            TriviaUser triviaUser = new TriviaUser();
            triviaUser.setEmail(registerDto.getEmail());
            triviaUser.setPassword(passwordEncoder.encode(registerDto.getPassword()));
            triviaUser.setRole(Role.USER);
            triviaUser.setAccountLocked(Boolean.FALSE);
            triviaUser.setAccountDisabled(Boolean.TRUE);
            userRepository.save(triviaUser);
            activationService.generateActivationCode(triviaUser.getEmail());

        } catch (Exception e) {
            bindingResult.addError(
                    new FieldError("registerDto", "email", e.getMessage())
            );
            return false;
        }
        return true;
    }

    public boolean changePassword(ChangePasswordDto changePasswordDto, BindingResult bindingResult) {
        TriviaUser triviaUser = findByEmail(changePasswordDto.getEmail());
        if (Objects.isNull(triviaUser)) {
            bindingResult.addError(
                    new FieldError("changePasswordDto", "email", "No user registered with this email")
            );
            return false;
        }

        if (!passwordEncoder.matches(changePasswordDto.getOldPassword(), triviaUser.getPassword())) {
            bindingResult.addError(
                    new FieldError("changePasswordDto", "oldPassword", "Old password is incorrect")
            );
            return false;
        }

        if(bindingResult.hasErrors()) {
            return false;
        }

        triviaUser.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
        userRepository.save(triviaUser);

        return true;
    }

    public List<TriviaUser> findAll() {
        return userRepository.findAll().stream().filter(user -> !user.getEmail().equals(adminEmail)).toList();
    }

    public void toggleBanUser(BanRequest banRequest) {
        Optional<TriviaUser> optionalUser = userRepository.findByEmail(banRequest.getEmail());
        if (optionalUser.isPresent()) {
            TriviaUser triviaUser = optionalUser.get();
            triviaUser.setAccountLocked(!triviaUser.getAccountLocked());
            triviaUser.setLockedReason(banRequest.getReason());
            userRepository.save(triviaUser);
        }
    }
}
