package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * alex on 21.10.15.
 */
public class ConfigGeneral {

    public static final String SERVER_CONFIG_FILE = "cfg/server.properties";
    public static final String MECHANICS_CONFIG_FILE = "data/mechanics.xml";
    public static final String SOCIALS_CONFIG_FILE = "cfg/socials.properties";

    private static int s_port;
    private static String s_host;

    private static int s_roomsCount;
    private static int s_maxPlayersPerRoom;
    private static int s_minPlayers;
    private static int s_pointsPerQuestion;
    private static int s_timePerQuestionMS;

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
        s_minPlayers = Integer.valueOf(properties.getProperty("minPlayers"));
        s_pointsPerQuestion = Integer.valueOf(properties.getProperty("pointsPerQuestion"));
        s_timePerQuestionMS = Integer.valueOf(properties.getProperty("timePerQuestionMS"));

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

    public static int getMinPlayers() {
        return s_minPlayers;
    }

    public static int getPointsPerQuestion() {
        return s_pointsPerQuestion;
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
