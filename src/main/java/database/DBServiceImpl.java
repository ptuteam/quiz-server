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
import java.util.Set;

/**
 * Created by dima on 20.11.15.
 */
public class DBServiceImpl implements DBService {

    private static final int DUPLICATE_ENTRY_ERROR_CODE = 1062;

    @Override
    public void signUpUser(UserProfile user) {
        try {
            Connection connection = DatabaseConnection.getUsersConnection();
            UsersDAO usersDAO = new UsersDAO(connection);

            usersDAO.signUpUser(user);

            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            if (e.getErrorCode() != DUPLICATE_ENTRY_ERROR_CODE) {
                e.printStackTrace();
            }
        }
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
                    userDataSet.getEmail(), userDataSet.getAvatarUrl());
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
    public void updateUserScore(String email, int score) {
        try {
            Connection connection = DatabaseConnection.getUsersConnection();

            UsersDAO usersDAO = new UsersDAO(connection);
            usersDAO.updateUserScore(email, score);

            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Question getRandomQuestion(int type, Set<Integer> askedQuestions) {
        try {

            Connection connection = DatabaseConnection.getQuizConnection();

            QuestionsDAO questionsDAO = new QuestionsDAO(connection);
            QuestionsDataSet question = questionsDAO.getRandom(type, askedQuestions);
            askedQuestions.add(question.getQuestionId());
            AnswersDAO answersDAO = new AnswersDAO(connection);

            String[] answers = null;
            String correctAnswer = null;

            if (question.getType() == Question.DEFAULT_QUESTION_TYPE) {

                List<String> answersList = new ArrayList<>();
                for (AnswersDataSet a : answersDAO.getByQuestionId(question.getQuestionId())) {
                    answersList.add(a.getText());
                    if (a.isCorrect()) {
                        correctAnswer = a.getText();
                    }
                }

                answers = new String[answersList.size()];
                answers = answersList.toArray(answers);

            } else if (question.getType() == Question.SPECIAL_QUESTION_TYPE) {

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
