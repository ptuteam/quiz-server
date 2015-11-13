package utils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * alex on 05.11.15.
 */
@SuppressWarnings({"MagicNumber", "unused"})
@RunWith(PowerMockRunner.class)
@PrepareForTest({ConfigGeneral.class})
public class ConfigGeneralTest {

    @Mock
    private Properties properties;

    private static final int MS_IN_MINUTE = 60 * 1000;

    @SuppressWarnings("OverlyBroadThrowsClause")
    @Before
    public void setUp() throws Exception {
        PowerMockito.whenNew(Properties.class).withNoArguments().thenReturn(properties);
        when(properties.getProperty("port", "80")).thenReturn("1");
        when(properties.getProperty("host", "127.0.0.1")).thenReturn("2");
        when(properties.getProperty("roomsCount", "1")).thenReturn("3");
        when(properties.getProperty("timePerQuestionMS", "1000")).thenReturn("4");
        when(properties.getProperty("maxPlayersPerRoom", "2")).thenReturn("5");
        when(properties.getProperty("minPlayersPerRoom", "2")).thenReturn("6");
        when(properties.getProperty("pointsPerQuestion", "1")).thenReturn("7");
        when(properties.getProperty("timeForWaitingStartGameMS", "0")).thenReturn("8");
        when(properties.getProperty("timeForWaitingNewRoundStartMS", "0")).thenReturn("9");
        when(properties.getProperty("maxGameTimeMinutes", "1")).thenReturn("10");
        when(properties.getProperty("minRoundsPerGameCount", "1")).thenReturn("11");
        when(properties.getProperty("clientID", "")).thenReturn("12");
        when(properties.getProperty("clientSecret", "")).thenReturn("13");
        when(properties.getProperty("redirectUrl", "")).thenReturn("14");
        when(properties.getProperty("grantType", "")).thenReturn("15");
        when(properties.getProperty("ratingUsersCount", "1")).thenReturn("16");

        ConfigGeneral.loadConfig();
    }

    @SuppressWarnings("OverlyBroadThrowsClause")
    @Test
    public void testLoadConfig() throws IOException {
        verify(properties, atLeastOnce()).load(any(FileInputStream.class));
        verify(properties, atLeastOnce()).loadFromXML(any(FileInputStream.class));
        verify(properties, atLeastOnce()).getProperty(anyString(), anyString());
    }

    @Test
    public void testGetPort() throws Exception {
        assertEquals(ConfigGeneral.getPort(), 1);
    }

    @Test
    public void testGetHost() throws Exception {
        assertEquals(ConfigGeneral.getHost(), "2");
    }

    @Test
    public void testGetRoomsCount() throws Exception {
        assertEquals(ConfigGeneral.getRoomsCount(), 3);
    }

    @Test
    public void testGetTimePerQuestionMS() throws Exception {
        assertEquals(ConfigGeneral.getTimePerQuestionMS(), 4);
    }

    @Test
    public void testGetMaxPlayersPerRoom() throws Exception {
        assertEquals(ConfigGeneral.getMaxPlayersPerRoom(), 5);
    }

    @Test
    public void testGetMinPlayersPerRoom() throws Exception {
        assertEquals(ConfigGeneral.getMinPlayersPerRoom(), 6);
    }

    @Test
    public void testGetPointsPerQuestion() throws Exception {
        assertEquals(ConfigGeneral.getPointsPerQuestion(), 7);
    }

    @Test
    public void testGetTimeForWaitingStartGameMS() throws Exception {
        assertEquals(ConfigGeneral.getTimeForWaitingStartGameMS(), 8);
    }

    @Test
    public void testGetTForWaitNewRoundStartMS() throws Exception {
        assertEquals(ConfigGeneral.getTimeForWaitingNewRoundStartMS(), 9);
    }

    @Test
    public void testGetMaxGameTimeMS() throws Exception {
        assertEquals(ConfigGeneral.getMaxGameTimeMS(), 10 * MS_IN_MINUTE);
    }

    @Test
    public void testGetMinRoundsPerGameCount() throws Exception {
        assertEquals(ConfigGeneral.getMinRoundsPerGameCount(), 11);
    }

    @Test
    public void testGetClientID() throws Exception {
        assertTrue(ConfigGeneral.getClientID().equals("12") || ConfigGeneral.getClientID().isEmpty());
    }

    @Test
    public void testGetClientSecret() throws Exception {
        assertTrue(ConfigGeneral.getClientSecret().equals("13") || ConfigGeneral.getClientSecret().isEmpty());
    }

    @Test
    public void testGetRedirectUrl() throws Exception {
        assertTrue(ConfigGeneral.getRedirectUrl().equals("14") || ConfigGeneral.getRedirectUrl().isEmpty());
    }

    @Test
    public void testGetGrantType() throws Exception {
        assertTrue(ConfigGeneral.getGrantType().equals("15") || ConfigGeneral.getGrantType().isEmpty());
    }

    @Test
    public void testGetRatingUsersCount() throws Exception {
        assertEquals(ConfigGeneral.getRatingUsersCount(), 16);
    }
}