package database.data;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by dima on 24.11.15.
 */
@SuppressWarnings("unused")
public class UsersDataSetTest {

    private UsersDataSet user;

    @Before
    public void setUp() throws Exception {
        user = new UsersDataSet(3, "first", "last", "email", "avatar", 5);
    }

    @Test
    public void testGetUserId() throws Exception {
        assertEquals(3, user.getUserId());
    }

    @Test
    public void testGetFirstName() throws Exception {
        assertEquals("first", user.getFirstName());
    }

    @Test
    public void testGetLastName() throws Exception {
        assertEquals("last", user.getLastName());
    }

    @Test
    public void testGetEmail() throws Exception {
        assertEquals("email", user.getEmail());
    }

    @Test
    public void testGetAvatarUrl() throws Exception {
        assertEquals("avatar", user.getAvatarUrl());
    }

    @Test
    public void testGetScore() throws Exception {
        assertEquals(5, user.getScore());
    }
}