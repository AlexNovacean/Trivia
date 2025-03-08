package ro.alex.trivia.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import ro.alex.trivia.validation.NotCompromised;

public class RegisterDto {

    @NotEmpty(message = "Please select a `Display Name`")
    private String displayName;

    @Email(message = "Please provide a proper `Email` address")
    @NotEmpty(message = "Please provide an `Email` address")
    private String email;

    @NotEmpty(message = "Please provide a `Password`")
    @NotCompromised
    private String password;

    @NotEmpty(message = "Please select an `Avatar`")
    private String avatarName;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

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

    public String getAvatarName() {
        return avatarName;
    }

    public void setAvatarName(String avatarName) {
        this.avatarName = avatarName;
    }
}
