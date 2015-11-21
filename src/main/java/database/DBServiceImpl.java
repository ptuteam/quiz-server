package database;

import database.connection.DatabaseConnection;
import database.dao.AnswersDAO;
import database.dao.QuestionsDAO;
import database.dao.UsersDAO;
import database.data.AnswersDataSet;
import database.data.QuestionsDataSet;
import database.data.UsersDataSet;
import game.Question;
import model.UserProfile;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by dima on 20.11.15.
 */
public class DBServiceImpl implements DBService {

    private static final int DUPLICATE_ENTRY_ERROR_CODE = 1062;

    @Override
    public boolean signUpUser(UserProfile user) {
        Connection connection = DatabaseConnection.getUsersConnection();
        UsersDAO usersDAO = new UsersDAO(connection);

        try {
            usersDAO.signUpUser(user);
            return true;
        } catch (SQLException e) {
            if (e.getErrorCode() == DUPLICATE_ENTRY_ERROR_CODE) {
                return false;
            } else {
                e.printStackTrace();
            }
        }

        return false;
    }

    @Override
    public UserProfile getUserByEmail(String email) {
        try {
            Connection connection = DatabaseConnection.getUsersConnection();

            UsersDAO usersDAO = new UsersDAO(connection);
            UsersDataSet userDataSet = usersDAO.getByEmail(email);

            if (connection != null) {
                connection.close();
            }

            UserProfile user = new UserProfile(userDataSet.getFirstName(), userDataSet.getLastName(),
                    userDataSet.getEmail(), userDataSet.getAvatarUrl(), userDataSet.isGuest());
            user.setScore(userDataSet.getScore());
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean isUserExist(String email) {
        try {
            Connection connection = DatabaseConnection.getUsersConnection();

            UsersDAO usersDAO = new UsersDAO(connection);
            boolean result = usersDAO.isUserExists(email);

            if (connection != null) {
                connection.close();
            }

            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public int getUsersCount() {
        try {
            Connection connection = DatabaseConnection.getUsersConnection();

            UsersDAO usersDAO = new UsersDAO(connection);
            int usersCount = usersDAO.getUsersCount();

            if (connection != null) {
                connection.close();
            }

            return usersCount;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public Collection<UserProfile> getAllUsers() {
        try {
            Connection connection = DatabaseConnection.getUsersConnection();

            UsersDAO usersDAO = new UsersDAO(connection);
            Collection<UserProfile> users = usersDAO.getAllUsers();

            if (connection != null) {
                connection.close();
            }

            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Question getRandomQuestion() {
        try {

            Connection connection = DatabaseConnection.getQuizConnection();

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
}
