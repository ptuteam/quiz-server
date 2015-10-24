package utils;

import model.UserProfile;

import java.util.Collection;

/**
 * alex on 24.10.15.
 */
public interface AccountService {

    void signIn(String sessionId, UserProfile user);
    boolean signUp(UserProfile user);
    void logout(String sessionId);
    boolean isLogged(String sessionId);
    UserProfile getUser(String email);
    boolean isUserExist(String email);
    UserProfile getUserBySession(String sessionId);
    int getUsersCount();
    int getLoggedUsersCount();
    Collection<UserProfile> getUsers();

}
