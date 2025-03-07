package ro.alex.trivia.model;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class QuizDto {
    private String email;
    private TriviaDifficulty difficulty;
    @NotEmpty
    private List<TriviaCategory> categories;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public TriviaDifficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(TriviaDifficulty difficulty) {
        this.difficulty = difficulty;
    }

    public List<TriviaCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<TriviaCategory> categories) {
        this.categories = categories;
    }
}
