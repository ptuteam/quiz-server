package game;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import database.connection.DatabaseConnection;
import database.dao.AnswersDAO;
import database.dao.QuestionsDAO;
import database.dataSets.AnswersDataSet;
import database.dataSets.QuestionsDataSet;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * alex on 26.10.15.
 */
public class QuestionHelper {
    public static final int QUESTIONS_COUNT = 5;

    public JsonObject getQuestionWithAnswersJson(int questionId) {
        try {
            Connection connection = DatabaseConnection.getConnection();

            // get question
            QuestionsDAO questionDAO = new QuestionsDAO(connection);
            QuestionsDataSet question = questionDAO.get(questionId);

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("question", question.getText());

            // get answer
            AnswersDAO answersDAO = new AnswersDAO(connection);
            ArrayList<AnswersDataSet> answers = answersDAO.getByQuestionId(questionId);

            JsonArray answerArray = new JsonArray();
            jsonObject.add("answers", answerArray);
            for (AnswersDataSet answer : answers) {
                answerArray.add(answer.getText());
            }

            if (connection != null) {
                connection.close();
            }

            return jsonObject;
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
