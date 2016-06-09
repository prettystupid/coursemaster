package application.utils;

import application.controller.Controller;
import application.controller.CourseController;
import application.controller.NTDController;
import application.view.EntityType;
import application.view.main.ShowPanel;

import javax.swing.*;

public class PanelFactory {

    public static ShowPanel createPanel(EntityType entityType) {

        ShowPanel panel;
        Controller controller;

        switch (entityType) {
            case COURSE:
                controller = new CourseController();
                panel = new ShowPanel(controller);
                controller.setView(panel);
                break;
            case NTD:
                controller = new NTDController();
                panel = new ShowPanel(controller);
                controller.setView(panel);
                break;
            default:
                throw new RuntimeException("EntityType " + entityType.toString() +
                    " not supported by current version");
        }
        return panel;
    }
}
