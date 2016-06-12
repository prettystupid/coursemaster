package application.utils.dao;

import application.model.entity.IEntity;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;

import java.sql.*;
import java.util.ArrayList;

public abstract class DAO<T extends IEntity> {

    protected Connection getConnection() throws ConfigurationException, SQLException {
        XMLConfiguration config = new XMLConfiguration("src\\main\\resources\\config.xml");
        String url = new StringBuilder("jdbc:mysql://localhost:3306/").append(config.getString("db-name")).append("?useSSL=false&serverTimezone=UTC").toString();
        return DriverManager.getConnection(url, config.getString("username"), config.getString("password"));
    }

    protected PreparedStatement createPreparedStatement(Connection connection, String query) throws SQLException {
        return connection.prepareStatement(query);
    }

    protected ResultSet getResultSet(PreparedStatement preparedStatement) throws SQLException {
        return preparedStatement.executeQuery();
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
