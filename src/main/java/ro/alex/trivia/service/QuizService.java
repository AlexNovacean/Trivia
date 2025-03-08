package ro.alex.trivia.service;

import org.apache.commons.math3.util.Precision;
import org.springframework.stereotype.Service;
import ro.alex.trivia.model.*;
import ro.alex.trivia.repository.QuizRepository;
import ro.alex.trivia.repository.TriviaRepository;
import ro.alex.trivia.repository.UserRepository;

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
    private final UserRepository userRepository;

    public QuizService(QuizRepository quizRepository, TriviaRepository triviaRepository, UserRepository userRepository) {
        this.quizRepository = quizRepository;
        this.triviaRepository = triviaRepository;
        this.userRepository = userRepository;
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

    public List<Leaderboard> getLeaderBoard() {
        return userRepository.findAllByScoreNotNull().stream()
                .map(user -> new Leaderboard(user.getDisplayName(), user.getScore(), user.getAvatar()))
                .sorted()
                .toList();
    }

    public QuizResult solveQuiz(String email, Map<Integer, String> answers) {
        QuizResult result = new QuizResult();
        double score = 0.0;
        Quiz quiz = quizRepository.findByEmailAndScoreNull(email).orElseThrow();
        TriviaUser triviaUser = userRepository.findByEmail(email).orElseThrow();
        List<TriviaQuestion> questions = quiz.getQuestions();

        for(TriviaQuestion question : questions) {
            if (question.getCorrectAnswer().trim().equalsIgnoreCase(answers.get(question.getId()).trim())) {
                score += quiz.getQuestionPoints();
            } else {
                score -= quiz.getQuestionPoints() * WRONG_ANSWER_POINTS_WEIGHT;
            }
        }

        double checkedScore = Math.max(0, score);
        triviaUser.setScore(Precision.round(
                Objects.nonNull(triviaUser.getScore()) ?
                        Math.max(0, triviaUser.getScore() + score) :
                        checkedScore
                , 2));
        userRepository.save(triviaUser);

        quiz.setScore(Precision.round(checkedScore, 2));
        quizRepository.save(quiz);

        result.setScore(checkedScore);
        result.setAnswers(
                questions.stream().collect(Collectors.toMap(TriviaQuestion::getId, TriviaQuestion::getCorrectAnswer))
        );

        return result;
    }
}
