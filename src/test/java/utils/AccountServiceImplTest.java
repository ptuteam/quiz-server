package utils;

import model.UserProfile;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * alex on 27.09.15.
 */
@SuppressWarnings({"unused", "Magic number"})
@RunWith(PowerMockRunner.class)
@PrepareForTest({ConfigGeneral.class})
public class AccountServiceImplTest {
    public static final int SCORE = 20;
    private AccountService accountService;

    @Before
    public void setUp() {
        PowerMockito.mockStatic(ConfigGeneral.class);
        when(ConfigGeneral.getDbType()).thenReturn("jdbc:mysql://");
        when(ConfigGeneral.getDbHostName()).thenReturn("localhost:");
        when(ConfigGeneral.getDbPort()).thenReturn("3306/");
        when(ConfigGeneral.getDbNameQuiz()).thenReturn("test_quiz_db?");
        when(ConfigGeneral.getDbNameUsers()).thenReturn("test_quiz_users_db?");
        when(ConfigGeneral.getDbLogin()).thenReturn("user=test_quiz_user&");
        when(ConfigGeneral.getDbPassword()).thenReturn("password=secret");
        ConfigGeneral.loadConfig();

        accountService = new AccountServiceImpl();
        UserProfile user = new UserProfile("test", "Test", "test@email.com", "avatarUrl");
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
        assertTrue(accountService.getUsersCount() >= 1);
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

    @Test
    public void testGetTopUsers() throws Exception {
        UserProfile user1 = new UserProfile("first1", "last1", "email1", "avatar1");
        user1.setScore(SCORE + 1);
        UserProfile user2 = new UserProfile("first2", "last2", "email2", "avatar2");
        user2.setScore(SCORE + 2);
        UserProfile user3 = new UserProfile("first3", "last3", "email3", "avatar3");
        user3.setScore(SCORE);
        UserProfile user4 = new UserProfile("first4", "last4", "email4", "avatar4");
        user4.setScore(SCORE + 3);
        UserProfile user5 = new UserProfile("first5", "last5", "email5", "avatar5");
        user5.setScore(SCORE + 4);

        accountService.signUp(user1);
        accountService.signUp(user2);
        accountService.signUp(user3);
        accountService.signUp(user4);
        accountService.signUp(user5);

        List<UserProfile> actual =  accountService.getTopUsers(3);
        List<String> actualEmails = new ArrayList<>();
        actualEmails.add(actual.get(0).getEmail());
        actualEmails.add(actual.get(1).getEmail());
        actualEmails.add(actual.get(2).getEmail());

        List<String> expected = new ArrayList<>();
        expected.add(user5.getEmail());
        expected.add(user4.getEmail());
        expected.add(user2.getEmail());

        assertEquals(expected, actualEmails);
    }
}