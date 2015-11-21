package game;

import java.util.Objects;

/**
 * alex on 26.10.15.
 */
public class QuestionHelper {

    public boolean checkAnswer(Question question, String answer) {
        return Objects.equals(question.getCorrectAnswer(), answer);
    }
}
