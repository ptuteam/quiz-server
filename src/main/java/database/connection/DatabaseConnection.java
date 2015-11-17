package database.connection;

import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;
import utils.ConfigGeneral;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by dima on 12.11.15.
 */
public class DatabaseConnection {
    public static Connection getConnection() {
        try {
            DriverManager.registerDriver((Driver) Class.forName("com.mysql.jdbc.Driver").newInstance());

            return DriverManager.getConnection(
                    ConfigGeneral.getDbType() +
                            ConfigGeneral.getDbHostName() +
                            ConfigGeneral.getDbPort() +
                            ConfigGeneral.getDbName() +
                            ConfigGeneral.getDbLogin() +
                            ConfigGeneral.getDbPassword());
        } catch (CommunicationsException e) {
            System.out.println("Communication to mysql occurred. Maybe you have not installed mysql server.");
            e.printStackTrace();
        } catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            System.out.println("Error while contecting to database was occurred.");
            System.out.println("Check your database server settings and database configs.");
            e.printStackTrace();
        }
        return null;
    }
}
