package utils;

import model.UserProfile;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * alex on 27.09.15.
 */
public class AccountServiceTest {
    private AccountService accountService;
    private UserProfile userProfile;

    @Before
    public void setUp() throws Exception {
        accountService = new AccountService();
        userProfile = new UserProfile("test", "Test", "test@email.com", "psw");
        accountService.signUp(userProfile);
    }

    @Test
    public void testSignUp() throws Exception {
        accountService.signUp(new UserProfile("qwe", "Qwe", "qwe@email.com", "psw"));
        UserProfile result = accountService.getUser("qwe@email.com");
        assertNotNull(result);
    }

    @Test
    public void testSignUpError() throws Exception {
        accountService.signUp(new UserProfile("qwe", "Qwe", "test@email.com", "psw"));
        UserProfile user = accountService.getUser("test@email.com");
        assertNotEquals(user.getFirstName(), "qwe");
    }

    @Test
    public void testIsLogged() throws Exception {
        String sessionId = "session";
        accountService.signIn(sessionId, userProfile);
        boolean result = accountService.isLogged(sessionId);
        assertTrue(result);
    }
}