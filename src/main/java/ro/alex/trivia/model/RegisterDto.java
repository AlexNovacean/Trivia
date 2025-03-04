package ro.alex.trivia.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import ro.alex.trivia.validation.NotCompromised;

public class RegisterDto {

    @Email
    @NotEmpty
    private String email;

    @NotEmpty
    @NotCompromised
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
