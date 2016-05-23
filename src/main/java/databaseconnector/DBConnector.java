package databaseconnector;

import courses.*;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;


public class DBConnector {

    final private static String URL = "jdbc:mysql://localhost:3306/coursemaster?useSSL=false&serverTimezone=UTC";
    final private static String NAME = "javauser";
    final private static String PASSWORD = "javauser";

    private static Connection getConnection() {
        Connection connection;
        try {
            connection = DriverManager.getConnection(URL, NAME, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return connection;
    }

    public static ArrayList<Course> getCourses() {
        Connection connection = getConnection();

        ArrayList<Course> courses = new ArrayList<Course>();
        String query = "SELECT * FROM `courses`";
        Statement statement;
        ResultSet resultSet;

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            Course course;
            while (resultSet.next()) {
                course = new Course(resultSet.getInt("ID"), resultSet.getString("UUID"), resultSet.getInt("VERSION"), resultSet.getString("NAME"), resultSet.getInt("MISTAKES_ALLOWED"));
                courses.add(course);
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }

    public static Course getCourse(String uuid, int version) {
        Connection connection = getConnection();

        long id = getId(uuid, version);
        Course course = null;
        String query = "SELECT * FROM `courses` WHERE ID =" + id;
        Statement statement;
        ResultSet resultSet;

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                course = new Course(resultSet.getInt("ID"), resultSet.getString("UUID"), resultSet.getInt("VERSION"), resultSet.getString("NAME"), resultSet.getInt("MISTAKES_ALLOWED"));
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return course;
    }

    public static void insertCourse(CourseInfo courseInfo, String uuid, int version) {
        try {
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO `courses`(`UUID`, `VERSION`, `NAME`, `MISTAKES_ALLOWED`) VALUES(?,?,?,?);");
            statement.setString(1, uuid);
            statement.setInt(2, version);
            statement.setString(3, courseInfo.getName());
            statement.setInt(4, courseInfo.getMistakesAllowed());
            statement.execute();

            PreparedStatement questionStatement = connection.prepareStatement("INSERT INTO `questions`(`COURSE_ID`, `TICKET_NUMBER`, `QUESTION_NUMBER`, `QUESTION`) VALUES(?,?,?,?);");
            PreparedStatement answerStatement = connection.prepareStatement("INSERT INTO `answers`(`COURSE_ID`, `QUESTION_NUMBER`, `TICKET_NUMBER`, `ANSWER`, `IS_CORRECT`) VALUES(?,?,?,?,?)");
            long courseId = getId(uuid, version);
            for (Ticket ticket : courseInfo.getTickets()) {
                int questionNumber = 1;
                for (Question question : ticket.getQuestions()) {
                    questionStatement.setLong(1, courseId);
                    questionStatement.setInt(2, ticket.getNumber());
                    questionStatement.setInt(3, questionNumber);
                    questionStatement.setString(4, question.getQuestion());
                    questionStatement.execute();
                    for (Answer answer : question.getAnswers()) {
                        answerStatement.setLong(1, courseId);
                        answerStatement.setInt(2, questionNumber);
                        answerStatement.setInt(3, ticket.getNumber());
                        answerStatement.setString(4, answer.getText());
                        answerStatement.setString(5, (answer.isCorrect() ? "1" : "0"));
                        answerStatement.execute();
                    }
                    questionNumber++;
                }
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int findMatches(String uuid, int version) {
        String query = "SELECT ID FROM `courses` where UUID='" + uuid + "' AND VERSION=" + version;
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            ResultSet result =  statement.executeQuery(query);
            if (result.next())
                return 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private static long getId(String uuid, int version) {
        String query = "SELECT ID FROM `courses` where UUID='" + uuid + "' AND VERSION=" + version + ";";
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            ResultSet result =  statement.executeQuery(query);
            if (result.next()) {
                int id = result.getInt("ID");
                connection.close();
                return id;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static void delete(String uuid, int version) {
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            long id = getId(uuid, version);
            if (id == -1) {
                JOptionPane.showMessageDialog(null, "Курс не найден.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String query = "DELETE FROM `questions` WHERE COURSE_ID = " + id;
            statement.execute(query);
            query = "DELETE FROM `courses` WHERE ID = " + id;
            statement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateCourse(Course course) {
        if (findMatches(course.getUuid(), course.getVersion()) != 0) {
            JOptionPane.showMessageDialog(null, "Данная версия уже существует.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Connection connection = getConnection();
        try {
            String query = "UPDATE `courses` SET UUID = ?, VERSION = ?, NAME = ? WHERE ID = " +course.getId();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, course.getUuid());
            statement.setInt(2, course.getVersion());
            statement.setString(3, course.getName());
            statement.execute();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
