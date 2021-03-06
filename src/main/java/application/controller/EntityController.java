package application.controller;

import application.utils.dao.DAO;
import org.apache.commons.configuration.ConfigurationException;

import javax.swing.*;
import java.sql.SQLException;

public abstract class EntityController {

    protected JTable table;
    protected DAO dao;
    protected MainController mainController;

    public EntityController(MainController mainController) {
        this.mainController = mainController;
    }

    public abstract void delete(Long id) throws SQLException, ConfigurationException;

    public abstract void update() throws SQLException, ConfigurationException;

    public abstract void createTable() throws SQLException, ConfigurationException;

    public void catchException(Exception e) {
        if (e instanceof SQLException) {
            createSQLExceptionDialog();
        } else if ((e instanceof NullPointerException)||(e instanceof ConfigurationException)) {
            createConfigurationExceptionDialog();
        } else {
            throw new RuntimeException();
        }
    }

    public void createSQLExceptionDialog() {
        int dialogResult = JOptionPane.showConfirmDialog(null, "Соединение с базой данных отсутствует. Изменить настройки базы данных?", "Ошибка", JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.YES_OPTION) {
            mainController.setDB();
        } else {
            return;
        }
    }

    public void createConfigurationExceptionDialog() {
        int dialogResult = JOptionPane.showConfirmDialog(null, "Конфигурационный файл отсутствует или работает неправильно. Исправить ошибку?", "Ошибка", JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.YES_OPTION) {
            mainController.createConfig();
        } else {
            return;
        }
    }

    public void setTable(JTable table) {
        this.table = table;
    }

    public DAO getDao() {
        return dao;
    }
}
