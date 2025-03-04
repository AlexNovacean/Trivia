package ro.alex.trivia.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ro.alex.trivia.model.Role;
import ro.alex.trivia.model.TriviaUser;
import ro.alex.trivia.repository.UserRepository;

import java.util.Optional;

@Service
public class StartUpService {

    private final UserRepository userRepository;
    private final String adminEmail;
    private final String adminPassword;
    private final PasswordEncoder encoder;

    public StartUpService(UserRepository userRepository,
                          @Value("${trivia.admin.email:admin@trivia.ro}") String adminEmail,
                          @Value("${trivia.admin.email:1!sUpErSeCrEtAdMinPwD!2}") String adminPassword,
                          PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.adminEmail = adminEmail;
        this.adminPassword = adminPassword;
        this.encoder = encoder;
    }

    @EventListener
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Optional<TriviaUser> optionalAdmin = userRepository.findByEmail(adminEmail);
        if (optionalAdmin.isEmpty()) {
            TriviaUser admin = new TriviaUser();
            admin.setEmail(adminEmail);
            admin.setPassword(encoder.encode(adminPassword));
            admin.setRole(Role.ADMIN);
            admin.setAccountLocked(Boolean.FALSE);
            userRepository.save(admin);
        }
    }
}
