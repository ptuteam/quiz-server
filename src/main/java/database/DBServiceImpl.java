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
import java.util.*;

/**
 * Created by dima on 20.11.15.
 */
public class DBServiceImpl implements DBService {

    private static final int DUPLICATE_ENTRY_ERROR_CODE = 1062;

    private final Connection connectionUsers = DatabaseConnection.getUsersConnection();
    private final Connection connectionQuiz = DatabaseConnection.getQuizConnection();

    @Override
    public void signUpUser(UserProfile user) {
        try {
            UsersDAO usersDAO = new UsersDAO(connectionUsers);
            usersDAO.signUpUser(user);
        } catch (SQLException e) {
            if (e.getErrorCode() != DUPLICATE_ENTRY_ERROR_CODE) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public UserProfile getUserByEmail(String email) {
        try {
            UsersDAO usersDAO = new UsersDAO(connectionUsers);
            UsersDataSet userDataSet = usersDAO.getByEmail(email);

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
            UsersDAO usersDAO = new UsersDAO(connectionUsers);
            return usersDAO.isUserExists(email);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public int getUsersCount() {
        try {
            UsersDAO usersDAO = new UsersDAO(connectionUsers);
            return usersDAO.getUsersCount();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public Collection<UserProfile> getAllUsers() {
        try {
            UsersDAO usersDAO = new UsersDAO(connectionUsers);
            return usersDAO.getAllUsers();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void updateUserScore(String email, int score) {
        try {
            UsersDAO usersDAO = new UsersDAO(connectionUsers);
            usersDAO.updateUserScore(email, score);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Question getRandomQuestion(int type, Set<Integer> askedQuestions) {
        try {
            QuestionsDAO questionsDAO = new QuestionsDAO(connectionQuiz);
            QuestionsDataSet question = questionsDAO.getRandom(type, askedQuestions);
            askedQuestions.add(question.getQuestionId());
            AnswersDAO answersDAO = new AnswersDAO(connectionQuiz);

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

            return new Question(question.getTitle(), question.getType(), answers, correctAnswer);
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
        }

        return null;
    }
}
