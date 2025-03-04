package ro.alex.trivia.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import ro.alex.trivia.validation.NotCompromised;

public class ChangePasswordDto {

    @Email
    @NotEmpty
    private String email;
    @NotEmpty
    private String oldPassword;
    @NotEmpty
    @NotCompromised
    private String newPassword;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
