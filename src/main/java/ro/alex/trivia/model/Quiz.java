package ro.alex.trivia.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String email;

    @ManyToMany
    @JoinTable(
            name = "quiz_questions",
            joinColumns = @JoinColumn(name = "quiz_id"),
            inverseJoinColumns = @JoinColumn(name = "question_id")
    )
    private List<TriviaQuestion> questions;
    private Double questionPoints;
    private Double score;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<TriviaQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(List<TriviaQuestion> questions) {
        this.questions = questions;
    }

    public Double getQuestionPoints() {
        return questionPoints;
    }

    public void setQuestionPoints(Double questionPoints) {
        this.questionPoints = questionPoints;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }
}
