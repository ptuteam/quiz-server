package database;

import game.Question;
import model.UserProfile;

import java.util.Collection;

/**
 * Created by dima on 19.11.15.
 */
public interface DBService {

    void signUpUser(UserProfile user);
    UserProfile getUserByEmail(String email);
    boolean isUserExist(String email);
    int getUsersCount();
    Collection<UserProfile> getAllUsers();
    void updateUserScore(String email, int score);

    Question getRandomQuestion();
}
