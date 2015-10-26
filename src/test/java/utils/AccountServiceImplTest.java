package utils;

import model.UserProfile;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.*;

/**
 * alex on 27.09.15.
 */
public class AccountServiceImplTest {
    private AccountService accountService;
    private UserProfile user;

    @Before
    public void setUp() throws Exception {
        accountService = new AccountServiceImpl();
        user = new UserProfile("test", "Test", "test@email.com", "avatarUrl");
        accountService.signUp(user);
        accountService.signIn("session", user);
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
        assertNotEquals("qwe", user.getFirstName());
    }

    @Test
    public void testIsLogged() throws Exception {
        String sessionId = "session";
        accountService.signIn(sessionId, user);
        boolean result = accountService.isLogged(sessionId);
        assertTrue(result);
    }

    @Test
    public void testLogout() throws Exception {
        UserProfile temp = new UserProfile("qwe", "Qwe", "qwe@email.com", "avatarUrl", true);
        accountService.signUp(temp);
        String sessionId = "session";
        accountService.signIn(sessionId, temp);
        accountService.logout(sessionId);
    }

    @Test
    public void testGetUserBySession() throws Exception {
        UserProfile user = accountService.getUserBySession("session");
        assertEquals("test@email.com", user.getEmail());
    }

    @Test
    public void testGetUsersCount() throws Exception {
        assertEquals(1, accountService.getUsersCount());
    }

    @Test
    public void testGetLoggedUsersCount() throws Exception {
        assertEquals(1, accountService.getLoggedUsersCount());
    }

    @Test
    public void testGetUsers() throws Exception {
        Collection<UserProfile> users = accountService.getUsers();
        assertEquals(accountService.getUsersCount(), users.size());
    }
}