package application.controller;

import application.utils.DBCreator;
import application.view.DBChangeWindow;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;

import javax.swing.*;

public class MainController {

    private JFrame view;

    public void setDB() {
        DBChangeWindow window = null;
        try {
            window = new DBChangeWindow(view);
        } catch (ConfigurationException e) {
            createConfig();
        } finally {
            XMLConfiguration config = window.getConfig();
            window.dispose();
            try {
                config.save();
            } catch (ConfigurationException e) {
                e.printStackTrace();
            }
        }
    }

    public void createConfig() {
        XMLConfiguration config = new XMLConfiguration();
        config.setFileName("src\\main\\resources\\config.xml");
        config.addProperty("username", "javauser");
        config.addProperty("password", "javauser");
        config.addProperty("db-name", "coursemaster");
        try {
            config.save();
            setDB();
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void createDB() {
        try {
            DBCreator.create();
        } catch (ConfigurationException e) {
            createConfig();
        }
    }

    public void setView(JFrame view) {
        this.view = view;
    }
}
