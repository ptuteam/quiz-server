package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * alex on 21.10.15.
 */
public class ConfigGeneral {

    public static final String SERVER_CONFIG_FILE = "configuration/cfg/server.properties";
    public static final String MECHANICS_CONFIG_FILE = "configuration/data/mechanics.xml";
    public static final String SOCIALS_CONFIG_FILE = "configuration/cfg/socials.properties";
    public static final int MS_IN_MINUTE = 60 * 1000;

    private static int s_port;
    private static String s_host;

    private static int s_roomsCount;
    private static int s_maxPlayersPerRoom;
    private static int s_minPlayersPerRoom;
    private static int s_pointsPerQuestion;
    private static int s_timePerQuestionMS;
    private static int s_timeForWaitingStartGame;
    private static int s_timeForWaitingNewRoundStart;
    private static int s_maxGameTimeMinutes;
    private static int s_minRoundsPerGameCount;

    private static String s_clientID;
    private static String s_clientSecret;
    private static String s_redirectUrl;
    private static String s_grantType;

    @SuppressWarnings("OverlyBroadThrowsClause")
    public static void loadConfig() throws IOException {
        FileInputStream fileInputStream = new FileInputStream(SERVER_CONFIG_FILE);
        Properties properties = new Properties();
        properties.load(fileInputStream);
        fileInputStream.close();
        s_port = Integer.valueOf(properties.getProperty("port"));
        s_host = properties.getProperty("host");

        fileInputStream = new FileInputStream(MECHANICS_CONFIG_FILE);
        properties.loadFromXML(fileInputStream);
        fileInputStream.close();
        s_roomsCount = Integer.valueOf(properties.getProperty("roomsCount"));
        s_maxPlayersPerRoom = Integer.valueOf(properties.getProperty("maxPlayersPerRoom"));
        s_minPlayersPerRoom = Integer.valueOf(properties.getProperty("minPlayersPerRoom"));
        s_pointsPerQuestion = Integer.valueOf(properties.getProperty("pointsPerQuestion"));
        s_timePerQuestionMS = Integer.valueOf(properties.getProperty("timePerQuestionMS"));
        s_timeForWaitingStartGame = Integer.valueOf(properties.getProperty("timeForWaitingStartGame"));
        s_timeForWaitingNewRoundStart = Integer.valueOf(properties.getProperty("timeForWaitingNewRoundStart"));
        s_maxGameTimeMinutes = Integer.valueOf(properties.getProperty("maxGameTimeMinutes"));
        s_minRoundsPerGameCount = Integer.valueOf(properties.getProperty("minRoundsPerGameCount"));

        fileInputStream = new FileInputStream(SOCIALS_CONFIG_FILE);
        properties.load(fileInputStream);
        fileInputStream.close();
        s_clientID = properties.getProperty("clientID");
        s_clientSecret = properties.getProperty("clientSecret");
        s_redirectUrl = properties.getProperty("redirectUrl");
        s_grantType = properties.getProperty("grantType");
    }


    public static int getPort() {
        return s_port;
    }

    public static String getHost() {
        return s_host;
    }

    public static int getRoomsCount() {
        return s_roomsCount;
    }

    public static int getTimePerQuestionMS() {
        return s_timePerQuestionMS;
    }

    public static int getMaxPlayersPerRoom() {
        return s_maxPlayersPerRoom;
    }

    public static int getMinPlayersPerRoom() {
        return s_minPlayersPerRoom;
    }

    public static int getPointsPerQuestion() {
        return s_pointsPerQuestion;
    }

    public static int getTimeForWaitingStartGame() {
        return s_timeForWaitingStartGame;
    }

    public static int getTimeForWaitingNewRoundStart() {
        return s_timeForWaitingNewRoundStart;
    }

    public static int getMaxGameTimeMS() {
        return s_maxGameTimeMinutes * MS_IN_MINUTE;
    }

    public static int getMinRoundsPerGameCount() {
        return s_minRoundsPerGameCount;
    }

    public static String getClientID() {
        return s_clientID;
    }

    public static String getClientSecret() {
        return s_clientSecret;
    }

    public static String getRedirectUrl() {
        return s_redirectUrl;
    }

    public static String getGrantType() {
        return s_grantType;
    }
}
