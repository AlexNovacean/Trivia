package ro.alex.trivia.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class ActivationDto {
    @NotEmpty(message = "Please provide an activation code")
    @Size(min=5,max=5,message = "Activation code must have 5 numbers")
    private String code;
    private String email;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
