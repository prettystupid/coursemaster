package databaseconnector;

import courses.*;
import download.DownloadedAnswer;
import download.DownloadedCourse;
import download.DownloadedQuestion;
import organization.Organization;

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

    private static PreparedStatement createPreparedStatement(Connection connection, String query) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return preparedStatement;
    }

    private static ResultSet getResultSet(PreparedStatement preparedStatement) {
        ResultSet resultSet = null;
        try {
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public static ArrayList<Course> getCourses() {
        ArrayList<Course> courses = new ArrayList<Course>();

        Connection connection = getConnection();
        String query = "SELECT * FROM `courses`";
        PreparedStatement statement = createPreparedStatement(connection, query);
        ResultSet resultSet = getResultSet(statement);

        try {
            Course course;
            while (resultSet.next()) {
                course = new Course(resultSet.getInt("ID"), resultSet.getString("UUID"), resultSet.getInt("VERSION"), resultSet.getString("NAME"), resultSet.getInt("MISTAKES_ALLOWED"));
                courses.add(course);
            }
            close(resultSet, statement, connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }

    private static void close(ResultSet resultSet, PreparedStatement statement, Connection connection) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static Course getCourse(String uuid, int version) {
        Course course = null;
        long id = getId(uuid, version);

        Connection connection = getConnection();
        String query = "SELECT * FROM `courses` WHERE ID = ?";
        PreparedStatement statement = createPreparedStatement(connection, query);

        try {
            statement.setLong(1, id);
            ResultSet resultSet = getResultSet(statement);
            if (resultSet.next()) {
                course = new Course(resultSet.getInt("ID"), resultSet.getString("UUID"), resultSet.getInt("VERSION"), resultSet.getString("NAME"), resultSet.getInt("MISTAKES_ALLOWED"));
            }
            close(resultSet, statement, connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return course;
    }

    public static void insertCourse(CourseInfo courseInfo, String uuid, int version) {
        try {
            Connection connection = getConnection();
            PreparedStatement statement = createPreparedStatement(connection, "INSERT INTO `courses`(`UUID`, `VERSION`, `NAME`, `MISTAKES_ALLOWED`) VALUES(?,?,?,?);");
            statement.setString(1, uuid);
            statement.setInt(2, version);
            statement.setString(3, courseInfo.getName());
            statement.setInt(4, courseInfo.getMistakesAllowed());
            statement.execute();

            PreparedStatement questionStatement = createPreparedStatement(connection, "INSERT INTO `questions`(`COURSE_ID`, `TICKET_NUMBER`, `QUESTION_NUMBER`, `QUESTION`) VALUES(?,?,?,?);");
            PreparedStatement answerStatement = createPreparedStatement(connection, "INSERT INTO `answers`(`COURSE_ID`, `QUESTION_NUMBER`, `TICKET_NUMBER`, `ANSWER`, `IS_CORRECT`) VALUES(?,?,?,?,?)");
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
            questionStatement.close();
            answerStatement.close();
            close(null, statement, connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static long getId(String uuid, int version) {
        Connection connection = getConnection();
        String query = "SELECT ID FROM `courses` where UUID = ? AND VERSION = ?;";
        PreparedStatement statement = createPreparedStatement(connection, query);
        try {
            statement.setString(1, uuid);
            statement.setInt(2, version);
            ResultSet result = getResultSet(statement);
            if (result.next()) {
                int id = result.getInt("ID");
                return id;
            }
            close(result, statement, connection);
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
        long id = getId(course.getUuid(), course.getVersion());
        if ((id != -1) && (id != course.getId())) {
            JOptionPane.showMessageDialog(null, "Данная версия уже существует.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Connection connection = getConnection();
        String query = "UPDATE `courses` SET UUID = ?, VERSION = ?, NAME = ? WHERE ID = ?";
        PreparedStatement statement = createPreparedStatement(connection, query);

        try {
            statement.setString(1, course.getUuid());
            statement.setInt(2, course.getVersion());
            statement.setString(3, course.getName());
            statement.setLong(4, course.getId());
            statement.execute();
            close(null, statement, connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static DownloadedCourse getDownloadedCourse(String uuid, int version) {
        DownloadedCourse course = null;
        long id = getId(uuid, version);

        Connection connection = getConnection();
        String courseQuery = "SELECT * FROM `courses` WHERE ID = ?";
        String questionQuery = "SELECT * FROM `questions` WHERE COURSE_ID = ?";
        String answerQuery = "SELECT * FROM `answers` WHERE COURSE_ID = ? AND QUESTION_NUMBER = ? AND TICKET_NUMBER = ?";
        PreparedStatement statement = createPreparedStatement(connection, courseQuery);

        try {
            statement.setLong(1, id);
            ResultSet resultSet = getResultSet(statement);
            if (resultSet.next()) {
                course = new DownloadedCourse(resultSet.getInt("ID"), resultSet.getString("UUID"), resultSet.getInt("VERSION"), resultSet.getString("NAME"), resultSet.getInt("MISTAKES_ALLOWED"));
            } else {
                return course;
            }

            ArrayList<DownloadedQuestion> questions = new ArrayList<DownloadedQuestion>();
            statement = createPreparedStatement(connection, questionQuery);
            statement.setLong(1, id);
            resultSet = getResultSet(statement);
            DownloadedQuestion question;
            PreparedStatement answerStatement = null;
            ResultSet answerResult = null;
            while (resultSet.next()) {
                question = new DownloadedQuestion(resultSet.getInt("COURSE_ID"), resultSet.getInt("TICKET_NUMBER"), resultSet.getInt("QUESTION_NUMBER"), resultSet.getString("QUESTION"));
                answerStatement = createPreparedStatement(connection, answerQuery);
                answerStatement.setLong(1, id);
                answerStatement.setLong(2, question.getId().getQuestionNumber());
                answerStatement.setLong(3, question.getId().getTicketNumber());
                answerResult = getResultSet(answerStatement);
                ArrayList<DownloadedAnswer> answers = new ArrayList<DownloadedAnswer>();
                DownloadedAnswer answer;
                while (answerResult.next()) {
                    answer = new DownloadedAnswer(answerResult.getString("ANSWER"), answerResult.getInt("IS_CORRECT"));
                    answers.add(answer);
                }
                question.setAnswers(answers);
                questions.add(question);
            }
            course.setQuestions(questions);
            close(answerResult, answerStatement, null);
            close(resultSet, statement, connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return course;
    }

    public static void createOrganization(Organization org) {
        Connection connection = getConnection();
        String query = "INSERT INTO `organizations`(`NAME`, `SECRET_KEY`) VALUES(?,?);";
        PreparedStatement statement = createPreparedStatement(connection, query);
        try {
            statement.setString(1, org.getName());
            statement.setString(2, org.getKey());
            statement.execute();
            close(null, statement, connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Organization> getOrganizations() {
        ArrayList<Organization> organizations = new ArrayList<Organization>();

        Connection connection = getConnection();
        String query = "SELECT * FROM `organizations`";
        PreparedStatement statement = createPreparedStatement(connection, query);
        ResultSet resultSet = getResultSet(statement);

        try {
            Organization organization;
            while (resultSet.next()) {
                organization = new Organization(resultSet.getString("NAME"), resultSet.getString("SECRET_KEY"));
                organizations.add(organization);
            }
            close(resultSet, statement, connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return organizations;
    }
}
