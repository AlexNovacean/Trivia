package ro.alex.trivia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.alex.trivia.model.UserActivation;

import java.util.Optional;

public interface UserActivationRepository extends JpaRepository<UserActivation, Integer> {
    Optional<UserActivation> findByEmail(String email);

    void deleteByEmail(String email);
}
