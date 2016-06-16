package application.controller;

import application.utils.dao.NTDDAO;
import org.apache.commons.configuration.ConfigurationException;

import java.sql.SQLException;

public class NTDController extends DocumentController {

    public NTDController(MainController mainController) {
        super(mainController);
        dao = new NTDDAO();
    }

    @Override
    public void upload() {

    }

    @Override
    public void download(Long id) {

    }

    @Override
    public void change(Long id) {

    }

    @Override
    public void delete(Long id) throws SQLException, ConfigurationException {

    }

    @Override
    public void update() {

    }

    @Override
    public void createTable() {

    }
}
