package ro.alex.trivia.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.alex.trivia.model.ActivationDto;
import ro.alex.trivia.model.UserActivation;
import ro.alex.trivia.repository.UserActivationRepository;
import ro.alex.trivia.repository.UserRepository;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class ActivationService {

    private final Random random = new Random();
    private final UserActivationRepository userActivationRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    public ActivationService(UserActivationRepository userActivationRepository, UserRepository userRepository, EmailService emailService) {
        this.userActivationRepository = userActivationRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    public void generateActivationCode(String email) {
        Optional<UserActivation> activationOptional = userActivationRepository.findByEmail(email);
        activationOptional.ifPresent(userActivationRepository::delete);

        UserActivation userActivation = new UserActivation();
        userActivation.setEmail(email);
        userActivation.setActivationCode(code());
        userActivationRepository.save(userActivation);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> emailService.sendActivationEmail(email, userActivation.getActivationCode()));
        executorService.shutdown();
    }

    @Transactional
    public boolean activateUser(ActivationDto activationDto) {
        String email = activationDto.getEmail();
        Optional<UserActivation> activationOptional = userActivationRepository.findByEmail(email);
        if(activationOptional.isPresent()) {
            UserActivation activation = activationOptional.get();
            if(activationDto.getCode().equals(activation.getActivationCode()) && activate(email)){
                    userActivationRepository.deleteByEmail(email);
                    return true;
                }

            userActivationRepository.deleteByEmail(email);
            generateActivationCode(email);
            return false;
        }
        generateActivationCode(email);
        return false;
    }

    private boolean activate(String email) {
        int updatedEntitiesCount = userRepository.activateUser(email);
        return updatedEntitiesCount == 1;
    }

    private String code() {
        return String.valueOf(Math.round(10000 + random.nextFloat() * 90000));
    }

}
