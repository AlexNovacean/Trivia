package ro.alex.trivia.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;
import ro.alex.trivia.service.AuthenticationFailureService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private static final String HOME = "/home";
    private final AuthenticationFailureService authenticationFailureService;

    public WebSecurityConfig(AuthenticationFailureService authenticationFailureService) {
        this.authenticationFailureService = authenticationFailureService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/", HOME, "/login/**", "/register", "/activate", "/password","/joke").permitAll()
                        .requestMatchers("/css/**", "/js/**", "/avatar/**", "/trophy/**", "/favicon.ico").permitAll()
                        .anyRequest().authenticated())
                .formLogin(form -> form.loginPage("/login").defaultSuccessUrl(HOME, true).failureHandler(authenticationFailureService))
                .httpBasic(Customizer.withDefaults())
                .logout(logoutConf -> logoutConf.logoutSuccessUrl(HOME))
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CompromisedPasswordChecker compromisedPasswordChecker() {
        return new HaveIBeenPwnedRestApiPasswordChecker();
    }
}
