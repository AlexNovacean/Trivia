package ro.alex.trivia.model;

import jakarta.validation.constraints.NotEmpty;

public class TriviaDto {
    @NotEmpty
    private String question;
    @NotEmpty
    private String answers;
    @NotEmpty
    private String correctAnswer;
    @NotEmpty
    private TriviaCategory category;
    @NotEmpty
    private TriviaDifficulty difficulty;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswers() {
        return answers;
    }

    public void setAnswers(String answers) {
        this.answers = answers;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public TriviaCategory getCategory() {
        return category;
    }

    public void setCategory(TriviaCategory category) {
        this.category = category;
    }

    public TriviaDifficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(TriviaDifficulty difficulty) {
        this.difficulty = difficulty;
    }
}
