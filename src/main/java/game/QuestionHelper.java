package game;

import database.DBService;
import database.DBServiceImpl;

import java.util.Objects;

/**
 * alex on 26.10.15.
 */
public class QuestionHelper {

    final DBService dbService = new DBServiceImpl();

    public Question getRandomQuestion() {
        return dbService.getRandomQuestion();
    }

    public boolean checkAnswer(Question question, String answer) {
        return Objects.equals(question.getCorrectAnswer(), answer);
    }
}
