package application.utils.dao;

import application.model.download.course.DownloadedAnswer;
import application.model.download.course.DownloadedCourse;
import application.model.download.course.DownloadedQuestion;
import application.model.entity.course.Answer;
import application.model.entity.course.Course;
import application.model.entity.course.Question;
import org.apache.commons.configuration.ConfigurationException;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;

public class CourseDAO extends DocumentDAO<Course> {

    @Override
    public ArrayList<Course> getAll() throws SQLException, ConfigurationException {
        ArrayList<Course> courses = new ArrayList<>();
        Connection connection = getConnection();
        String query = "SELECT * FROM `courses`";
        PreparedStatement statement = createPreparedStatement(connection, query);
        ResultSet resultSet = getResultSet(statement);
        Course course;
        while (resultSet.next()) {
            course = new Course(resultSet.getInt("ID"), resultSet.getString("UUID"), resultSet.getInt("VERSION"), resultSet.getString("NAME"), resultSet.getInt("MISTAKES_ALLOWED"));
            courses.add(course);
        }
        close(resultSet, statement, connection);
        return courses;
    }

    @Override
    public Course getById(long id) throws SQLException, ConfigurationException {
        Connection connection = getConnection();
        String query = "SELECT * FROM `courses` WHERE ID = ?";
        PreparedStatement statement = createPreparedStatement(connection, query);
        Course course = null;
        try {
            statement.setLong(1, id);
            ResultSet resultSet = getResultSet(statement);
            if (resultSet.next()) {
                course = new Course(resultSet.getLong("ID"), resultSet.getString("UUID"), resultSet.getLong("VERSION"), resultSet.getString("NAME"), resultSet.getInt("MISTAKES_ALLOWED"));
            }
            close(resultSet, statement, connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return course;
    }

    @Override
    public void insert(Course entity) throws SQLException, ConfigurationException {
        Connection connection = getConnection();
        PreparedStatement statement = createPreparedStatement(connection, "INSERT INTO `courses`(`UUID`, `VERSION`, `NAME`, `MISTAKES_ALLOWED`) VALUES(?,?,?,?);");
        statement.setString(1, entity.getUuid());
        statement.setLong(2, entity.getVersion());
        statement.setString(3, entity.getName());
        statement.setInt(4, entity.getMistakesAllowed());
        statement.execute();

        PreparedStatement questionStatement = createPreparedStatement(connection, "INSERT INTO `questions`(`COURSE_ID`, `TICKET_NUMBER`, `QUESTION_NUMBER`, `QUESTION`) VALUES(?,?,?,?);");
        PreparedStatement answerStatement = createPreparedStatement(connection, "INSERT INTO `answers`(`COURSE_ID`, `QUESTION_NUMBER`, `TICKET_NUMBER`, `ANSWER`, `IS_CORRECT`) VALUES(?,?,?,?,?)");

        long courseId = findMatches(entity.getUuid(), entity.getVersion());
        int ticketNumber;
        int questionNumber;

        for (Question question : entity.getQuestions()) {
            String[] qid = question.getQid().split(" ");
            ticketNumber = Integer.parseInt(qid[0]);
            questionNumber = Integer.parseInt(qid[1]);
            questionStatement.setLong(1, courseId);
            questionStatement.setInt(2, ticketNumber);
            questionStatement.setInt(3, questionNumber);
            questionStatement.setString(4, question.getQuestion());
            questionStatement.execute();
            for (Answer answer : question.getAnswers()) {
                answerStatement.setLong(1, courseId);
                answerStatement.setInt(2, questionNumber);
                answerStatement.setInt(3, ticketNumber);
                answerStatement.setString(4, answer.getText());
                answerStatement.setString(5, (answer.isCorrect() ? "1" : "0"));
                answerStatement.execute();
            }
        }
        questionStatement.close();
        answerStatement.close();
        close(null, statement, connection);
    }

    @Override
    public void delete(Long id) throws SQLException, ConfigurationException {
        Connection connection = getConnection();
        Statement statement = connection.createStatement();
        String query = "DELETE FROM `questions` WHERE COURSE_ID = " + id;
        statement.execute(query);
        query = "DELETE FROM `answers` WHERE COURSE_ID = " + id;
        statement.execute(query);
        query = "DELETE FROM `courses` WHERE ID = " + id;
        statement.execute(query);
        statement.close();
        connection.close();
    }

    @Override
    public void change(Course course) throws SQLException, ConfigurationException {
        long id = findMatches(course.getUuid(), course.getVersion());
        if ((id != -1) && (id != course.getId())) {
            JOptionPane.showMessageDialog(null, "Данная версия уже существует.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Connection connection = getConnection();
        String query = "UPDATE `courses` SET UUID = ?, VERSION = ?, NAME = ? WHERE ID = ?";
        PreparedStatement statement = createPreparedStatement(connection, query);

        statement.setString(1, course.getUuid());
        statement.setLong(2, course.getVersion());
        statement.setString(3, course.getName());
        statement.setLong(4, course.getId());
        statement.execute();
        close(null, statement, connection);
    }

    @Override
    public Object getDownloadedObject(Long id) throws SQLException, ConfigurationException {
        DownloadedCourse course = null;

        Connection connection = getConnection();
        String courseQuery = "SELECT * FROM `courses` WHERE ID = ?";
        String questionQuery = "SELECT * FROM `questions` WHERE COURSE_ID = ?";
        String answerQuery = "SELECT * FROM `answers` WHERE COURSE_ID = ? AND QUESTION_NUMBER = ? AND TICKET_NUMBER = ?";
        PreparedStatement statement = createPreparedStatement(connection, courseQuery);

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
        return course;
    }

    public long findMatches(String uuid, long version) throws SQLException, ConfigurationException {
        Connection connection = getConnection();
        String query = "SELECT ID FROM `courses` where UUID = ? AND VERSION = ?;";
        PreparedStatement statement = createPreparedStatement(connection, query);
        statement.setString(1, uuid);
        statement.setLong(2, version);
        ResultSet resultSet = getResultSet(statement);
        long result = -1;
        if (resultSet.next()) {
            result = resultSet.getInt("ID");
        }
        close(resultSet, statement, connection);
        return result;
    }
}
