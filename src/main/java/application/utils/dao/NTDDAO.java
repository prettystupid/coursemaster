package application.utils.dao;

import application.model.entity.Document;
import application.model.entity.IEntity;
import org.apache.commons.configuration.ConfigurationException;

import java.sql.SQLException;
import java.util.ArrayList;

public class NTDDAO extends DocumentDAO {

    @Override
    public ArrayList getAll() {
        return null;
    }

    @Override
    public IEntity getById(long id) {
        return null;
    }

    @Override
    public void insert(IEntity entity) {

    }

    @Override
    public void delete(Long id) throws SQLException, ConfigurationException {

    }


    @Override
    public long findMatches(String uuid, long version) throws SQLException, ConfigurationException {
        return 0;
    }

    @Override
    public void change(Document entity) throws SQLException, ConfigurationException {

    }
}
