package application.controller.entitycontroller;

import application.controller.MainController;
import application.model.entity.organization.Organization;
import org.apache.commons.configuration.ConfigurationException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrganizationController extends EntityController {

    public OrganizationController(MainController mainController) {
        super(mainController);
    }

    @Override
    public void upload() {

    }

    @Override
    public void download() {

    }

    @Override
    public void change(Long id) {

    }

    @Override
    public void delete(Long id) throws SQLException, ConfigurationException {

    }

    @Override
    public void update() {
        DefaultTableModel model = (DefaultTableModel)table.getModel();
        model.setRowCount(0);
        ArrayList<Organization> organizations = null;
        try {
            organizations = dao.getAll();
        } catch (SQLException e) {
            createSQLExceptionDialog();
            return;
        } catch (Exception e) {
            createConfigurationExceptionDialog();
            return;
        }
        Object[] row = new Object[3];
        for (Organization organization: organizations) {
            row[0] = organization.getId();
            row[1] = organization.getKey();
            row[2] = organization.getName();
            model.addRow(row);
        }
    }

    @Override
    public void createTable() {
        table.setModel(new DefaultTableModel(
                new Object [][] {},
                new String [] {
                        "ID", "Ключ", "Наименование"
                }
        )   {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(250);
        table.getColumnModel().getColumn(2).setPreferredWidth(500);
    }
}
