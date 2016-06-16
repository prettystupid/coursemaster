package application.utils.dao;

import application.model.entity.Organization;
import application.model.entity.course.Course;
import org.apache.commons.configuration.ConfigurationException;

import java.sql.*;
import java.util.ArrayList;

public class OrganizationDAO extends DAO<Organization> {

    @Override
    public ArrayList<Organization> getAll() throws SQLException, ConfigurationException {
        ArrayList<Organization> organizations = new ArrayList<Organization>();

        Connection connection = getConnection();
        String query = "SELECT * FROM `organizations`";
        PreparedStatement statement = createPreparedStatement(connection, query);
        ResultSet resultSet = getResultSet(statement);

        Organization organization;
        while (resultSet.next()) {
            organization = new Organization(resultSet.getLong("ID"), resultSet.getString("NAME"), resultSet.getString("SECRET_KEY"));
            organizations.add(organization);
        }
        close(resultSet, statement, connection);
        return organizations;
    }

    @Override
    public Organization getById(long id) throws SQLException, ConfigurationException {
        Connection connection = getConnection();
        String query = "SELECT * FROM `organizations` WHERE ID = ?";
        PreparedStatement statement = createPreparedStatement(connection, query);
        Organization organization = null;
        try {
            statement.setLong(1, id);
            ResultSet resultSet = getResultSet(statement);
            if (resultSet.next()) {
                organization = new Organization(resultSet.getLong("ID"), resultSet.getString("NAME"), resultSet.getString("SECRET_NAME"));
            }
            close(resultSet, statement, connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return organization;
    }

    @Override
    public void insert(Organization entity) throws SQLException, ConfigurationException {
        Connection connection = getConnection();
        PreparedStatement statement = createPreparedStatement(connection, "INSERT INTO `organizations`(`NAME`, `SECRET_KEY`) VALUES(?,?);");
        statement.setString(1, entity.getName());
        statement.setString(2, entity.getKey());
        statement.execute();
        close(null, statement, connection);
    }

    @Override
    public void delete(Long id) throws SQLException, ConfigurationException {
        Connection connection = getConnection();
        Statement statement = connection.createStatement();
        String query = "DELETE FROM `organizations` WHERE ID = " + id;
        statement.execute(query);
        statement.close();
        connection.close();
    }
}
