package ro.alex.trivia.model;

import jakarta.persistence.*;

import java.util.Arrays;
import java.util.List;

@Entity
public class TriviaQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String question;
    /**
     * Comma separated answers
     */
    private String answers;
    private String correctAnswer;
    private TriviaCategory category;
    private TriviaDifficulty difficulty;

    @ManyToMany(mappedBy = "questions")
    private List<Quiz> quizzes;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public List<String> answers(){
        return Arrays.stream(answers.split(",")).toList();
    }

    public List<Quiz> getQuizzes() {
        return quizzes;
    }

    public void setQuizzes(List<Quiz> quizzes) {
        this.quizzes = quizzes;
    }
}
