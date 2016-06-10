package application.utils.dao;

import application.model.entity.Document;
import application.model.entity.organization.Organization;
import org.apache.commons.configuration.ConfigurationException;

import java.sql.SQLException;
import java.util.ArrayList;

public class OrganizationDAO extends DAO<Organization> {

    @Override
    public ArrayList<Organization> getAll() throws SQLException, ConfigurationException {
        return null;
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
