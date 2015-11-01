package utils;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by dima on 01.11.15.
 */
public class ConfigGeneralTest {

    @BeforeClass
    public static void setUp() throws Exception {
        ConfigGeneral.loadConfig();
    }

    @Test
    public void testGetPort() throws Exception {
        assertEquals(8081, ConfigGeneral.getPort());
    }

    @Test
    public void testGetHost() throws Exception {
        assertEquals("127.0.0.1", ConfigGeneral.getHost());
    }

    @Test
    public void testGetRoomsCount() throws Exception {
        assertEquals(10, ConfigGeneral.getRoomsCount());
    }

    @Test
    public void testGetTimePerQuestionMS() throws Exception {
        assertEquals(30000, ConfigGeneral.getTimePerQuestionMS());
    }

    @Test
    public void testGetMaxPlayersPerRoom() throws Exception {
        assertEquals(2, ConfigGeneral.getMaxPlayersPerRoom());
    }

    @Test
    public void testGetMinPlayers() throws Exception {
        assertEquals(2, ConfigGeneral.getMinPlayers());
    }

    @Test
    public void testGetPointsPerQuestion() throws Exception {
        assertEquals(1, ConfigGeneral.getPointsPerQuestion());
    }

    @Test
    public void testGetClientID() throws Exception {
        assertEquals("clientID", ConfigGeneral.getClientID());
    }

    @Test
    public void testGetClientSecret() throws Exception {
        assertEquals("clientSecret", ConfigGeneral.getClientSecret());
    }

    @Test
    public void testGetRedirectUrl() throws Exception {
        assertEquals("redirectUrl", ConfigGeneral.getRedirectUrl());
    }

    @Test
    public void testGetGrantType() throws Exception {
        assertEquals("grantType", ConfigGeneral.getGrantType());
    }
}