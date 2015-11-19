package database;

import model.UserProfile;

import java.util.Collection;

/**
 * Created by dima on 19.11.15.
 */
public interface DBService {

    boolean userSignUp(UserProfile user);
    void deleteUser(String email);
    UserProfile userGetByEmail(String email);
    boolean userIsExist(String email);
    int userGetCount();
    Collection<UserProfile> userGetAll();
}
