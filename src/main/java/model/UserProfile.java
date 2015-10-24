package model;

/**
 * alex on 18.09.15.
 */
public class UserProfile {
    private static final String ADMINISTATOR_EMAILS = "sashaudalv@gmail.com";

    private final String firstName;
    private final String lastName;
    private final String email;
    private final String avatarUrl;
    private int score = 0;
    private boolean isGuest = false;

    public UserProfile(String firstName, String lastName, String email, String avatarUrl) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.avatarUrl = avatarUrl;
    }

    public UserProfile(String firstName, String lastName, String email, String avatarUrl, boolean isGuest) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.avatarUrl = avatarUrl;
        this.isGuest = isGuest;
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

    public boolean isAdministrator() {
        return ADMINISTATOR_EMAILS.contains(email);
    }

    public boolean isGuest() {
        return isGuest;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }
}
