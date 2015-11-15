package game;

import database.connection.DatabaseConnection;
import database.dao.AnswersDAO;
import database.dao.QuestionsDAO;
import database.data.AnswersDataSet;
import database.data.QuestionsDataSet;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * alex on 26.10.15.
 */
public class QuestionHelper {

    public Question getRandomQuestion() {
        try {

            Connection connection = DatabaseConnection.getConnection();

            QuestionsDAO questionsDAO = new QuestionsDAO(connection);
            QuestionsDataSet question = questionsDAO.getRandom();

            AnswersDAO answersDAO = new AnswersDAO(connection);

            String[] answers = null;
            String correctAnswer = null;

            if (question.getType() == 1) {

                List<String> answersList = new ArrayList<>();
                for (AnswersDataSet a : answersDAO.getByQuestionId(question.getQuestionId())) {
                    answersList.add(a.getText());
                    if (a.isCorrect()) {
                        correctAnswer = a.getText();
                    }
                }

                answers = new String[answersList.size()];
                answers = answersList.toArray(answers);

            } else {

                AnswersDataSet answer = answersDAO.getCorrectByQuestionId(question.getQuestionId());
                correctAnswer = answer.getText();

            }

            if (connection != null) {
                connection.close();
            }

            return new Question(question.getTitle(), question.getType(), answers, correctAnswer);

        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean checkAnswer(Question question, String answer) {
        return Objects.equals(question.getCorrectAnswer(), answer);
    }
}
