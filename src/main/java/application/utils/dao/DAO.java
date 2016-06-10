package application.utils.dao;

import application.model.entity.IEntity;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;

import java.sql.*;
import java.util.ArrayList;

public abstract class DAO<T extends IEntity> {

    protected Connection getConnection() throws ConfigurationException, SQLException {
        XMLConfiguration config = new XMLConfiguration("src\\main\\resources\\config.xml");
        StringBuilder url = new StringBuilder("jdbc:mysql://localhost:3306/");
        url.append(config.getString("db-name")).append("?useSSL=false&serverTimezone=UTC");
        Connection connection = DriverManager.getConnection(url.toString(), config.getString("username"), config.getString("password"));
        return connection;
    }

    protected PreparedStatement createPreparedStatement(Connection connection, String query) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        return preparedStatement;
    }

    protected ResultSet getResultSet(PreparedStatement preparedStatement) throws SQLException {
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet;
    }

    protected void close(ResultSet resultSet, PreparedStatement statement, Connection connection) throws SQLException {
        if (resultSet != null) {
            resultSet.close();
        }
        if (statement != null) {
            statement.close();
        }
        if (connection != null) {
            connection.close();
        }
    }

    public abstract ArrayList<T> getAll() throws SQLException, ConfigurationException;

    public abstract T getById(long id) throws SQLException, ConfigurationException;

    public abstract void insert(T entity) throws SQLException, ConfigurationException;

    public abstract void delete(Long id) throws SQLException, ConfigurationException;

    public abstract void change(T entity) throws SQLException, ConfigurationException;
}
