package ro.alex.trivia.model;

import java.util.Map;

public class QuizResult {
    private Double score;
    private Map<Integer,String> answers;

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Map<Integer, String> getAnswers() {
        return answers;
    }

    public void setAnswers(Map<Integer, String> answers) {
        this.answers = answers;
    }
}
