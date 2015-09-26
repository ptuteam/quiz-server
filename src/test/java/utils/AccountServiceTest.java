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
        boolean result = accountService.signUp(new UserProfile("qwe", "Qwe", "qwe@email.com", "psw"));
        assertTrue(result);
    }

    @Test
    public void testSignUpError() throws Exception {
        boolean result = accountService.signUp(new UserProfile("qwe", "Qwe", "test@email.com", "psw"));
        assertFalse(result);
    }

    @Test
    public void testIsLogged() throws Exception {
        String sessionId = "session";
        accountService.signIn(sessionId, userProfile);
        boolean result = accountService.isLogged(sessionId);
        assertTrue(result);
    }

    @Test
    public void testIsNotLogged() throws Exception {
        String sessionId = "session";
        boolean result = accountService.isLogged(sessionId);
        assertFalse(result);
    }
}