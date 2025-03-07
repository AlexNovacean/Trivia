package ro.alex.trivia.service;

import org.apache.commons.math3.util.Precision;
import org.springframework.stereotype.Service;
import ro.alex.trivia.model.*;
import ro.alex.trivia.repository.QuizRepository;
import ro.alex.trivia.repository.ScoreBoardRepository;
import ro.alex.trivia.repository.TriviaRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class QuizService {

    private static final double CATEGORY_COUNT_SCORE_WEIGHT = 0.2;
    private static final int BASIC_DIFFICULTY_BASE_SCORE = 1;
    private static final int MODERATE_DIFFICULTY_BASE_SCORE = 2;
    private static final int CHALLENGING_DIFFICULTY_BASE_SCORE = 3;
    private static final int QUIZ_NUMBER_OF_QUESTIONS = 5;
    private static final double WRONG_ANSWER_POINTS_WEIGHT = 0.5;
    private final QuizRepository quizRepository;
    private final TriviaRepository triviaRepository;
    private final ScoreBoardRepository scoreBoardRepository;
    private final Random rand = new Random();

    public QuizService(QuizRepository quizRepository, TriviaRepository triviaRepository, ScoreBoardRepository scoreBoardRepository) {
        this.quizRepository = quizRepository;
        this.triviaRepository = triviaRepository;
        this.scoreBoardRepository = scoreBoardRepository;
    }

    public Quiz findStartedQuiz(String email){
        return quizRepository.findByEmailAndScoreNull(email).orElse(null);
    }

    public boolean hasStartedQuiz(String email){
        Quiz quiz = findStartedQuiz(email);
        return quiz != null;
    }

    public Quiz createQuiz(QuizDto quiz) {
        List<TriviaQuestion> possibleQuestions = new ArrayList<>();
        if (quiz.getCategories().size() == QUIZ_NUMBER_OF_QUESTIONS) {
            possibleQuestions.addAll(triviaRepository.findAllByDifficulty(quiz.getDifficulty()));
        } else {
            for (TriviaCategory category : quiz.getCategories()) {
                possibleQuestions.addAll(triviaRepository.findAllByDifficultyAndCategory(quiz.getDifficulty(), category));
            }
        }

        Collections.shuffle(possibleQuestions);

        Quiz newQuiz = new Quiz();
        newQuiz.setEmail(quiz.getEmail());
        newQuiz.setQuestions(possibleQuestions.stream().limit(QUIZ_NUMBER_OF_QUESTIONS).toList());
        newQuiz.setQuestionPoints(calculateQuestionPoints(quiz));

        return quizRepository.save(newQuiz);
    }

    private Double calculateQuestionPoints(QuizDto quiz) {
        return switch (quiz.getDifficulty()) {
            case BASIC -> BASIC_DIFFICULTY_BASE_SCORE + CATEGORY_COUNT_SCORE_WEIGHT * quiz.getCategories().size();
            case MODERATE -> MODERATE_DIFFICULTY_BASE_SCORE + CATEGORY_COUNT_SCORE_WEIGHT * quiz.getCategories().size();
            default -> CHALLENGING_DIFFICULTY_BASE_SCORE + CATEGORY_COUNT_SCORE_WEIGHT * quiz.getCategories().size();
        };
    }

    public List<ScoreBoard> getLeaderBoard() {
        return scoreBoardRepository.findAll().stream().sorted().toList();
    }

    public QuizResult resolve(String email, Map<Integer, String> answers) {
        QuizResult result = new QuizResult();
        double score = 0.0;
        Quiz quiz = quizRepository.findByEmailAndScoreNull(email).orElseThrow();
        List<TriviaQuestion> questions = quiz.getQuestions();

        for(TriviaQuestion question : questions) {
            if (question.getCorrectAnswer().trim().equalsIgnoreCase(answers.get(question.getId()).trim())) {
                score += quiz.getQuestionPoints();
            } else {
                score -= quiz.getQuestionPoints() * WRONG_ANSWER_POINTS_WEIGHT;
            }
        }

        score = Precision.round(score, 2);

        Optional<ScoreBoard> optionalScoreBoard = scoreBoardRepository.findByEmail(email);
        if (optionalScoreBoard.isPresent()) {
            ScoreBoard scoreBoard = optionalScoreBoard.get();
            double newScore = scoreBoard.getScore() + score;
            scoreBoard.setScore(newScore < 0 ? 0 : newScore);
            scoreBoardRepository.save(scoreBoard);
        } else {
            ScoreBoard newScoreBoard = new ScoreBoard();
            newScoreBoard.setEmail(email);
            newScoreBoard.setScore(score < 0 ? 0 : score);
            scoreBoardRepository.save(newScoreBoard);
        }

        quiz.setScore(score < 0 ? 0 : score);
        quizRepository.save(quiz);

        result.setScore(score < 0 ? 0 : score);
        result.setAnswers(
                questions.stream().collect(Collectors.toMap(TriviaQuestion::getId, TriviaQuestion::getCorrectAnswer))
        );

        return result;
    }
}
