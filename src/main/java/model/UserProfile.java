package model;

/**
 * alex on 18.09.15.
 */
public class UserProfile {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private boolean isAdministrator = false;

    public UserProfile(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public UserProfile(String firstName, String lastName, String email, String password, boolean isAdministrator) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.isAdministrator = isAdministrator;
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

    public String getPassword() {
        return password;
    }

    public boolean isAdministrator() {
        return isAdministrator;
    }
}
