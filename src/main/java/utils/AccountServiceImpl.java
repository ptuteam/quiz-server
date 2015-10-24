package utils;

import model.UserProfile;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * alex on 25.09.15.
 */
public class AccountServiceImpl implements AccountService {

    private final Map<String, UserProfile> usersMap = new HashMap<>();
    private final Map<String, UserProfile> sessionsMap = new HashMap<>();

    @Override
    public void signIn(String sessionId, UserProfile user) {
        if (sessionsMap.containsKey(sessionId)) {
            sessionsMap.remove(sessionId);
        }
        sessionsMap.put(sessionId, user);
    }

    @Override
    public boolean signUp(UserProfile user) {
        if (isUserExist(user.getEmail())) {
            return false;
        }

        usersMap.put(user.getEmail(), user);
        return true;
    }

    @Override
    public void logout(String sessionId) {
        UserProfile user = sessionsMap.get(sessionId);
        if (user.isGuest()) {
            usersMap.remove(user.getEmail());
        }
        sessionsMap.remove(sessionId);
    }

    @Override
    public boolean isLogged(String sessionId) {
        return sessionsMap.containsKey(sessionId);
    }

    @Override
    public UserProfile getUser(String email) {
        return usersMap.get(email);
    }

    @Override
    public boolean isUserExist(String email) {
        return usersMap.containsKey(email);
    }

    @Override
    public UserProfile getUserBySession(String sessionId) {
        return sessionsMap.get(sessionId);
    }

    @Override
    public int getUsersCount() {
        return usersMap.size();
    }

    @Override
    public int getLoggedUsersCount() {
        return sessionsMap.size();
    }

    @Override
    public Collection<UserProfile> getUsers() {
        return usersMap.values();
    }
}
