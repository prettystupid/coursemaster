package application.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBCreator {

    final static String username = "javauser";
    final static String password = "javauser";

    public static void main(String[] args) {
        try {
            String url = "jdbc:mysql://localhost:3306/mysql?zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=UTC";
            Connection connection = DriverManager.getConnection(url, username, password);
            String query = "CREATE SCHEMA " + "coursemaster";
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
            statement.close();
            connection.close();

            url = "jdbc:mysql://localhost:3306/coursemaster?useSSL=false&serverTimezone=UTC";
            connection = DriverManager.getConnection(url, username, password);
            query = "CREATE TABLE coursemaster.courses\n" +
                    "(\n" +
                    "    ID INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,\n" +
                    "    UUID VARCHAR(50) DEFAULT \"\",\n" +
                    "    VERSION INT(11) DEFAULT 1,\n" +
                    "    NAME VARCHAR(255) DEFAULT \"\",\n" +
                    "    MISTAKES_ALLOWED INT(11) DEFAULT 0\n" +
                    ");";
            statement = connection.createStatement();
            statement.executeUpdate(query);

            query = "CREATE TABLE coursemaster.answers\n" +
                    "(\n" +
                    "    ID INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,\n" +
                    "    COURSE_ID INT(11),\n" +
                    "    QUESTION_NUMBER INT(11),\n" +
                    "    TICKET_NUMBER INT(11),\n" +
                    "    ANSWER VARCHAR(1024) DEFAULT \"\",\n" +
                    "    IS_CORRECT TINYINT(4)\n" +
                    ");";
            statement.executeUpdate(query);

            query = "CREATE TABLE coursemaster.questions\n" +
                    "(\n" +
                    "    COURSE_ID INT(11),\n" +
                    "    QUESTION_NUMBER INT(11),\n" +
                    "    TICKET_NUMBER INT(11),\n" +
                    "    QUESTION VARCHAR(1024),\n" +
                    "  PRIMARY KEY (COURSE_ID, TICKET_NUMBER, QUESTION_NUMBER)\n" +
                    ");";
            statement.executeUpdate(query);

            query = "CREATE TABLE coursemaster.organizations\n" +
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
