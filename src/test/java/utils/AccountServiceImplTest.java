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
        UserProfile result = accountService.getUser("test@email.com");
        assertNotNull(result);
        assertEquals(accountService.getUser("test@email.com").getFirstName(), "test");
    }

    @Test
    public void testSignUpError() throws Exception {
        assertFalse(accountService.signUp(new UserProfile("qwe", "Qwe", "test@email.com", "avatarUrl")));
        UserProfile userProfile = accountService.getUser("test@email.com");
        assertNotEquals("qwe", userProfile.getFirstName());
    }

    @Test
    public void testIsLogged() throws Exception {
        boolean result = accountService.isLogged("session");
        assertTrue(result);
    }

    @Test
    public void testLogout() throws Exception {
        accountService.logout("session");
        assertFalse(accountService.isLogged("session"));
    }

    @Test
    public void testGetUserBySession() throws Exception {
        UserProfile userProfile = accountService.getUserBySession("session");
        assertEquals("test@email.com", userProfile.getEmail());
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
        assertTrue(users.contains(user));
    }
}