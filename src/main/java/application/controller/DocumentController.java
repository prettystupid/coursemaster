package application.controller;

import org.apache.commons.configuration.ConfigurationException;

import java.sql.SQLException;

public abstract class DocumentController extends EntityController {

    public DocumentController(MainController mainController) {
        super(mainController);
    }

    public abstract void change(Long id) throws SQLException, ConfigurationException;

    public abstract void upload() throws SQLException, ConfigurationException;

    public abstract void download(Long id) throws SQLException, ConfigurationException;

}
