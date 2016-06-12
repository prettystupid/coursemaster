package application.utils.dao;

import application.model.entity.Organization;
import org.apache.commons.configuration.ConfigurationException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    public Organization getById(long id) throws SQLException {
        return null;
    }

    @Override
    public void insert(Organization entity) throws SQLException {

    }

    @Override
    public void delete(Long id) throws SQLException, ConfigurationException {

    }

    @Override
    public void change(Organization entity) throws SQLException {

    }
}
