package model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

@SuppressWarnings("unused")
public class UserProfileTest {

    private UserProfile user1;
    private UserProfile user2;

    @Before
    public void setUp() {
        user1 = new UserProfile("first1", "last1", "sashaudalv@gmail.com", "avatar1");
        user2 = new UserProfile("first2", "last2", "email2", "avatar2", true);
    }

    @Test
    public void testGetFirstName() throws Exception {
        String first1 = user1.getFirstName();
        String first2 = user2.getFirstName();
        assertEquals("first1", first1);
        assertEquals("first2", first2);
    }

    @Test
    public void testGetLastName() throws Exception {
        String last1 = user1.getLastName();
        String last2 = user2.getLastName();
        assertEquals("last1", last1);
        assertEquals("last2", last2);
    }

    @Test
    public void testGetEmail() throws Exception {
        String email1 = user1.getEmail();
        String email2 = user2.getEmail();
        assertEquals("sashaudalv@gmail.com", email1);
        assertEquals("email2", email2);
    }

    @Test
    public void testIsAdministrator() throws Exception {
        boolean isAdmin1 = user1.isAdministrator();
        boolean isAdmin2 = user2.isAdministrator();
        assertEquals(true, isAdmin1);
        assertEquals(false, isAdmin2);
    }

    @Test
    public void testIsGuest() throws Exception {
        boolean isGuest1 = user1.isGuest();
        boolean isGuest2 = user2.isGuest();
        assertEquals(false, isGuest1);
        assertEquals(true, isGuest2);
    }

    @Test
    public void testGetScore() throws Exception {
        int score = user1.getScore();
        assertEquals(0, score);
    }

    @Test
    public void testSetScore() throws Exception {
        user1.setScore(5);
        assertEquals(5, user1.getScore());
    }

    @Test
    public void testGetAvatarUrl() throws Exception {
        assertEquals("avatar1", user1.getAvatarUrl());
    }
}