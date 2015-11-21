package database.data;

/**
 * Created by dima on 19.11.15.
 */
public class UsersDataSet {

    private final int userId;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String avatarUrl;
    private final int score;
    private final boolean isGuest;

    public UsersDataSet(int userId, String firstName, String lastName,
                        String email, String avatarUrl, int score, boolean isGuest) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.avatarUrl = avatarUrl;
        this.score = score;
        this.isGuest = isGuest;
    }

    @SuppressWarnings("unused")
    public int getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    @SuppressWarnings("unused")
    public int getScore() {
        return score;
    }

    public boolean isGuest() {
        return isGuest;
    }
}
