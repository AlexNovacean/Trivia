package ro.alex.trivia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ro.alex.trivia.model.TriviaUser;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<TriviaUser, Integer> {

    Optional<TriviaUser> findByEmail(String email);

    @Modifying
    @Query("update TriviaUser t set t.accountDisabled=false where t.email=?1")
    int activateUser(String email);
}
