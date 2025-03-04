package ro.alex.trivia.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.authentication.password.CompromisedPasswordDecision;
import org.springframework.stereotype.Component;

@Component
public class CompromisedPasswordValidator implements ConstraintValidator<NotCompromised, String> {

    private final CompromisedPasswordChecker compromisedPasswordChecker;

    public CompromisedPasswordValidator(CompromisedPasswordChecker compromisedPasswordChecker) {
        this.compromisedPasswordChecker = compromisedPasswordChecker;
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        CompromisedPasswordDecision decision = compromisedPasswordChecker.check(password);
        return !decision.isCompromised();
    }
}