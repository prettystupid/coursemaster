package application.utils.dao;

import application.model.entity.Document;
import org.apache.commons.configuration.ConfigurationException;

import java.sql.SQLException;

public abstract class DocumentDAO<T extends Document> extends DAO<T> {

    public abstract long findMatches(String uuid, long version) throws SQLException, ConfigurationException;
}
