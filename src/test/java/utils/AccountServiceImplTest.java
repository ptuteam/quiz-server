package utils;

import model.UserProfile;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * alex on 27.09.15.
 */
public class AccountServiceImplTest {
    private AccountServiceImpl accountService;
    private UserProfile userProfile;

    @Before
    public void setUp() throws Exception {
        accountService = new AccountServiceImpl();
        userProfile = new UserProfile("test", "Test", "test@email.com", "avatarUrl");
        accountService.signUp(userProfile);
    }

    @Test
    public void testSignUp() throws Exception {
        accountService.signUp(new UserProfile("qwe", "Qwe", "qwe@email.com", "avatarUrl"));
        UserProfile result = accountService.getUser("qwe@email.com");
        assertNotNull(result);
    }

    @Test
    public void testSignUpError() throws Exception {
        accountService.signUp(new UserProfile("qwe", "Qwe", "test@email.com", "avatarUrl"));
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