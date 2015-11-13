package game;

import database.connection.DatabaseConnection;
import database.dao.AnswersDAO;
import database.dao.QuestionsDAO;
import database.data.AnswersDataSet;
import database.data.QuestionsDataSet;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * alex on 26.10.15.
 */
public class QuestionHelper {
    public static final int QUESTIONS_COUNT = 5;

    public Question getQuestion(int questionId) {

        try {
            Connection connection = DatabaseConnection.getConnection();

            QuestionsDAO questionsDAO = new QuestionsDAO(connection);
            QuestionsDataSet question = questionsDAO.get(questionId);

            String[] answers;

            if (question.getType() == 1) {
                AnswersDAO answersDAO = new AnswersDAO(connection);

                List<String> answersList = answersDAO.getByQuestionId(questionId).stream()
                        .map(AnswersDataSet::getText).collect(Collectors.toList());

                answers = new String[answersList.size()];
                answers = answersList.toArray(answers);
            } else {
                answers = null;
            }

            if (connection != null) {
                connection.close();
            }

            return new Question(question.getText(), question.getType(), answers);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean checkAnswer(int questionId, String answer) {
        try {
            Connection connection = DatabaseConnection.getConnection();

            AnswersDAO answersDAO = new AnswersDAO(connection);
            String correctAnswer = answersDAO.getCorrectByQuestionId(questionId).getText();

            if (connection != null) {
                connection.close();
            }

            return Objects.equals(correctAnswer, answer);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
