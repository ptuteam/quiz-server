package utils;

import model.UserProfile;

import java.util.HashMap;
import java.util.Map;

/**
 * alex on 25.09.15.
 */
public class AccountService {

    Map<String, UserProfile> usersMap = new HashMap<>();
    Map<String, UserProfile> sessionsMap = new HashMap<>();

    public void signIn(String sessionId, UserProfile user) {
        if (sessionsMap.containsKey(sessionId)) {
            sessionsMap.remove(sessionId);
        }
        sessionsMap.put(sessionId, user);
    }

    public boolean signUp(UserProfile user) {
        if (usersMap.containsKey(user.getEmail())) {
            return false;
        }

        usersMap.put(user.getEmail(), user);
        return true;
    }

    public void logout(String sessionId) {
        sessionsMap.remove(sessionId);
    }

    public boolean isLogged(String sessionId) {
        return sessionsMap.containsKey(sessionId);
    }

    public UserProfile getUser(String email) {
        return usersMap.get(email);
    }

    public UserProfile getSession(String sessionId) {
        return sessionsMap.get(sessionId);
    }

    public int getUsersCount() {
        return usersMap.size();
    }

    public int getLoggedUsersCount() {
        return sessionsMap.size();
    }
}
