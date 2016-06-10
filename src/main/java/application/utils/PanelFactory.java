package application.utils;

import application.controller.entitycontroller.EntityController;
import application.controller.entitycontroller.CourseController;
import application.controller.MainController;
import application.controller.entitycontroller.NTDController;
import application.model.entity.DocumentType;
import application.view.main.DocumentPanel;

public class PanelFactory {

    public static DocumentPanel createPanel(DocumentType entityType, MainController mainController) {

        DocumentPanel panel;
        EntityController controller;

        switch (entityType) {
            case COURSE:
                controller = new CourseController(mainController);
                break;
            case NTD:
                controller = new NTDController(mainController);
                break;
            default:
                throw new RuntimeException("DocumentType " + entityType.toString() +
                    " not supported by current version");
        }
        panel = new DocumentPanel(controller);
        controller.setTable(panel.getTable());
        return panel;
    }
}
