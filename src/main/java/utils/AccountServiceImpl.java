package utils;

import database.DBService;
import database.DBServiceImpl;
import model.UserProfile;

import java.util.*;
import java.util.stream.Collectors;

/**
 * alex on 25.09.15.
 */
public class AccountServiceImpl implements AccountService {

    private final Map<String, UserProfile> guestsMap = new HashMap<>();
    private final Map<String, UserProfile> sessionsMap = new HashMap<>();
    private final DBService dbService = new DBServiceImpl();

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

        if (user.isGuest()) {
            guestsMap.put(user.getEmail(), user);
        } else {
            dbService.signUpUser(user);
        }
        return true;
    }

    @Override
    public void logout(String sessionId) {
        UserProfile user = sessionsMap.get(sessionId);
        if (user.isGuest()) {
            guestsMap.remove(user.getEmail());
        }
        sessionsMap.remove(sessionId);
    }

    @Override
    public boolean isLogged(String sessionId) {
        return sessionsMap.containsKey(sessionId);
    }

    @Override
    public UserProfile getUser(String email) {
        if (dbService.isUserExist(email)){
            return dbService.getUserByEmail(email);
        } else {
            return guestsMap.get(email);
        }
    }

    @Override
    public boolean isUserExist(String email) {
        return (dbService.isUserExist(email) || guestsMap.containsKey(email));
    }

    @Override
    public UserProfile getUserBySession(String sessionId) {
        return sessionsMap.get(sessionId);
    }

    @Override
    public int getUsersCount() {
        return (dbService.getUsersCount() + guestsMap.size());
    }

    @Override
    public int getLoggedUsersCount() {
        return sessionsMap.size();
    }

    @Override
    public Collection<UserProfile> getUsers() {
        Collection<UserProfile> users = dbService.getAllUsers();
        users.addAll(guestsMap.values());
        return users;
    }

    @Override
    public List<UserProfile> getTopUsers(int count) {
        List<UserProfile> topUsers = new ArrayList<>(dbService.getTopUsers(count));
        topUsers.addAll(guestsMap.values().stream().filter(guest -> guest.getScore() > 0).collect(Collectors.toList()));
        Collections.sort(topUsers, (o1, o2) -> -Integer.valueOf(o1.getScore()).compareTo(o2.getScore()));
        return topUsers.isEmpty() ? topUsers : topUsers.subList(0, topUsers.size() > count ? count : topUsers.size());
    }

    @Override
    public void updateUserScore(UserProfile user, int score) {
        user.setScore(score);
        if (!user.isGuest()) {
            dbService.updateUserScore(user.getEmail(), score);
        }
    }
}
