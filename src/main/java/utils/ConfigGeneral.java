package utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

/**
 * alex on 21.10.15.
 */
public class ConfigGeneral {

    private static final String SERVER_CONFIG_FILE = "configuration/cfg/server.properties";
    private static final String MECHANICS_CONFIG_FILE = "configuration/data/mechanics.xml";
    private static final String SOCIALS_CONFIG_FILE = "configuration/cfg/socials.properties";
    private static final String DATABASE_CONFIG_FILE = "configuration/cfg/database.properties";
    private static final int MS_IN_MINUTE = 60 * 1000;

    private static int port;
    private static String host;

    private static int roomsCount;
    private static int maxPlayersPerRoom;
    private static int minPlayersPerRoom;
    private static int pointsPerQuestion;
    private static int timePerQuestionMS;
    private static int timeForWaitingStartGameMS;
    private static int timeForWaitingNewRoundStartMS;
    private static int maxGameTimeMinutes;
    private static int minRoundsPerGameCount;
    private static int ratingUsersCount;
    private static int timeForShowingPlayersAnswsMS;

    private static String clientID = "";
    private static String clientSecret = "";
    private static String redirectUrl = "";
    private static String grantType = "";

    private static String dbDriver;
    private static String dbQuizUrl;
    private static String dbUsersUrl;
    private static String dbUsername;
    private static String dbPassword;
    private static String dbValidationQuery;
    private static int dbMaxActive;
    private static int dbInitialSize;

    public static void loadConfig() {
        Properties properties = new Properties();

        try (FileInputStream fileInputStream = new FileInputStream(SERVER_CONFIG_FILE)) {
            properties.load(fileInputStream);
            fileInputStream.close();
            port = Integer.valueOf(properties.getProperty("port", "80"));
            host = properties.getProperty("host", "127.0.0.1");
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + SERVER_CONFIG_FILE);
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        try (FileInputStream fileInputStream = new FileInputStream(MECHANICS_CONFIG_FILE)) {
            properties.loadFromXML(fileInputStream);
            fileInputStream.close();
            roomsCount = Integer.valueOf(properties.getProperty("roomsCount", "1"));
            maxPlayersPerRoom = Integer.valueOf(properties.getProperty("maxPlayersPerRoom", "2"));
            minPlayersPerRoom = Integer.valueOf(properties.getProperty("minPlayersPerRoom", "2"));
            pointsPerQuestion = Integer.valueOf(properties.getProperty("pointsPerQuestion", "1"));
            timePerQuestionMS = Integer.valueOf(properties.getProperty("timePerQuestionMS", "1000"));
            timeForWaitingStartGameMS = Integer.valueOf(properties.getProperty("timeForWaitingStartGameMS", "0"));
            timeForWaitingNewRoundStartMS = Integer.valueOf(properties.getProperty("timeForWaitingNewRoundStartMS", "0"));
            maxGameTimeMinutes = Integer.valueOf(properties.getProperty("maxGameTimeMinutes", "1"));
            minRoundsPerGameCount = Integer.valueOf(properties.getProperty("minRoundsPerGameCount", "1"));
            ratingUsersCount = Integer.valueOf(properties.getProperty("ratingUsersCount", "1"));
            timeForShowingPlayersAnswsMS = Integer.valueOf(properties.getProperty("timeForShowingPlayersAnswersMS", "0"));
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + MECHANICS_CONFIG_FILE);
            System.exit(1);
        } catch (InvalidPropertiesFormatException e) {
            System.out.println("Invalid properties in file: " + MECHANICS_CONFIG_FILE);
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        try (FileInputStream fileInputStream = new FileInputStream(SOCIALS_CONFIG_FILE)) {
            properties.load(fileInputStream);
            fileInputStream.close();
            clientID = properties.getProperty("clientID", "");
            clientSecret = properties.getProperty("clientSecret", "");
            redirectUrl = properties.getProperty("redirectUrl", "");
            grantType = properties.getProperty("grantType", "");
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + SOCIALS_CONFIG_FILE);
            System.out.println("So you can't use social login!");
            System.out.println("You must have next variables in this file:");
            System.out.println("clientID, clientSecret, redirectUrl, grantType");
        } catch (IOException e) {
            System.out.println("IO error occurred while reading file:" + SOCIALS_CONFIG_FILE);
            System.out.println("So you can't use social login!");
            e.printStackTrace();
        }

        try (FileInputStream fileInputStream = new FileInputStream(DATABASE_CONFIG_FILE)) {
            properties.load(fileInputStream);
            fileInputStream.close();
            dbDriver = properties.getProperty("dbDriver", "com.mysql.jdbc.Driver");
            dbQuizUrl = properties.getProperty("dbQuizUrl", "jdbc:mysql://localhost:3306/quiz_db?useUnicode=true&characterEncoding=utf8");
            dbUsersUrl = properties.getProperty("dbUsersUrl", "jdbc:mysql://localhost:3306/quiz_users_db?useUnicode=true&characterEncoding=utf8");
            dbValidationQuery = properties.getProperty("dbValidationQuery", "SELECT 1");
            dbMaxActive = Integer.parseInt(properties.getProperty("dbMaxActive", "1"));
            dbUsername = properties.getProperty("dbUsername", "quiz_user");
            dbPassword = properties.getProperty("dbPassword", "secret");
            dbInitialSize = Integer.parseInt(properties.getProperty("dbInitialSize", "0"));
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + DATABASE_CONFIG_FILE);
            System.out.println("You must have next variables in this file:");
            System.out.println("dbDriver, dbQuizUrl, dbUsersUrl, dbValidationQuery, dbMaxActive, dbInitialSize, dbUsername, dbPassword");
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }


    public static int getPort() {
        return port;
    }

    public static String getHost() {
        return host;
    }

    public static int getRoomsCount() {
        return roomsCount;
    }

    public static int getTimePerQuestionMS() {
        return timePerQuestionMS;
    }

    public static int getMaxPlayersPerRoom() {
        return maxPlayersPerRoom;
    }

    public static int getMinPlayersPerRoom() {
        return minPlayersPerRoom;
    }

    public static int getPointsPerQuestion() {
        return pointsPerQuestion;
    }

    public static int getTimeForWaitingStartGameMS() {
        return timeForWaitingStartGameMS;
    }

    public static int getTimeForWaitingNewRoundStartMS() {
        return timeForWaitingNewRoundStartMS;
    }

    public static int getMaxGameTimeMS() {
        return maxGameTimeMinutes * MS_IN_MINUTE;
    }

    public static int getMinRoundsPerGameCount() {
        return minRoundsPerGameCount;
    }

    public static int getRatingUsersCount() {
        return ratingUsersCount;
    }

    public static String getClientID() {
        return clientID;
    }

    public static String getClientSecret() {
        return clientSecret;
    }

    public static String getRedirectUrl() {
        return redirectUrl;
    }

    public static String getGrantType() {
        return grantType;
    }

    public static String getDbDriver() {
        return dbDriver;
    }

    public static String getDbQuizUrl() {
        return dbQuizUrl;
    }

    public static String getDbUsersUrl() {
        return dbUsersUrl;
    }

    public static String getDbValidationQuery() {
        return dbValidationQuery;
    }

    public static int getDbMaxActive() {
        return dbMaxActive;
    }

    public static String getDbUsername() {
        return dbUsername;
    }

    public static String getDbPassword() {
        return dbPassword;
    }

    public static int getTimeForShowingPlayersAnswsMS() {
        return timeForShowingPlayersAnswsMS;
    }

    public static int getDbInitialSize() {
        return dbInitialSize;
    }
}
