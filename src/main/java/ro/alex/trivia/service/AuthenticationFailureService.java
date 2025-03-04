package ro.alex.trivia.service;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.password.CompromisedPasswordException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.FlashMapManager;
import org.springframework.web.servlet.support.SessionFlashMapManager;
import ro.alex.trivia.model.ActivationDto;
import ro.alex.trivia.model.TriviaUser;
import ro.alex.trivia.repository.UserRepository;

import java.io.IOException;
import java.util.Optional;

@Component
public class AuthenticationFailureService implements AuthenticationFailureHandler {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationFailureService.class);
    private final UserRepository userRepository;
    private final String defaultLockedReason;

    public AuthenticationFailureService(UserRepository userRepository,
                                        @Value("${default.locked.reason}") String defaultLockedReason) {
        this.userRepository = userRepository;
        this.defaultLockedReason = defaultLockedReason;
    }


    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        logger.error(exception.getMessage(), exception);

        final FlashMap flashMap = new FlashMap();
        final FlashMapManager flashMapManager = new SessionFlashMapManager();
        String username = request.getParameter("username");
        String redirect = "/login";
        switch (exception) {
            case CompromisedPasswordException e -> {
                flashMap.put("compromised", true);
                redirect = "/password";
            }
            case BadCredentialsException e -> flashMap.put("badCredentials", true);
            case LockedException e -> {
                flashMap.put("userLocked", true);
                Optional<TriviaUser> optionalUser = userRepository.findByEmail(username);
                optionalUser.ifPresent(triviaUser -> flashMap.put("reason",
                        StringUtils.hasText(triviaUser.getLockedReason()) ?
                                triviaUser.getLockedReason() :
                                defaultLockedReason));

            }
            case DisabledException e -> {
                ActivationDto activationDto = new ActivationDto();
                activationDto.setEmail(username);
                flashMap.put("activationDto", activationDto);
                flashMap.put("activating", true);
                redirect = "/activate";
            }
            default -> flashMap.put("error", true);
        }

        flashMapManager.saveOutputFlashMap(flashMap, request, response);
        response.sendRedirect(redirect);
    }
}
