package application.utils;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBCreator {

    public static void create() throws ConfigurationException {

        XMLConfiguration config = new XMLConfiguration("src\\main\\resources\\config.xml");
        final String username = config.getString("username");
        final String password = config.getString("password");
        final String dbName = config.getString("db-name");
        final String url = new StringBuilder("jdbc:mysql://localhost:3306/").append(dbName).append("?useSSL=false&serverTimezone=UTC").toString();

        try {
            String urlToSchema = "jdbc:mysql://localhost:3306/mysql?zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=UTC";
            Connection connection = DriverManager.getConnection(urlToSchema, username, password);
            String query = "CREATE SCHEMA " + dbName;
            Statement statement = connection.createStatement();
            try {
                statement.executeUpdate(query);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "База данных с таким именем уже существует", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }

            statement.close();
            connection.close();

            connection = DriverManager.getConnection(url, username, password);
            query = "CREATE TABLE " + dbName + ".courses\n" +
                    "(\n" +
                    "    ID INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,\n" +
                    "    UUID VARCHAR(50) DEFAULT \"\",\n" +
                    "    VERSION INT(11) DEFAULT 1,\n" +
                    "    NAME VARCHAR(255) DEFAULT \"\",\n" +
                    "    MISTAKES_ALLOWED INT(11) DEFAULT 0\n" +
                    ");";
            statement = connection.createStatement();
            statement.executeUpdate(query);

            query = "CREATE TABLE " + dbName + ".questions\n" +
                    "(\n" +
                    "    COURSE_ID INT(11),\n" +
                    "    QUESTION_NUMBER INT(11),\n" +
                    "    TICKET_NUMBER INT(11),\n" +
                    "    QUESTION VARCHAR(1024),\n" +
                    "    PRIMARY KEY (COURSE_ID, TICKET_NUMBER, QUESTION_NUMBER)\n" +
                    ");";
            statement.executeUpdate(query);

            query = "CREATE TABLE " + dbName + ".answers\n" +
                    "(\n" +
                    "    ID INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,\n" +
                    "    COURSE_ID INT(11),\n" +
                    "    QUESTION_NUMBER INT(11),\n" +
                    "    TICKET_NUMBER INT(11),\n" +
                    "    ANSWER VARCHAR(1024) DEFAULT \"\",\n" +
                    "    IS_CORRECT TINYINT(4)\n" +
                    ");";
            statement.executeUpdate(query);

            query = "CREATE TABLE " + dbName + ".organizations\n" +
                    "(\n" +
                    "    ID INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,\n" +
                    "    NAME VARCHAR (255),\n" +
                    "    SECRET_KEY VARCHAR(50)\n" +
                    ");";
            statement.executeUpdate(query);

            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
