package application.controller;

import application.model.entity.Organization;
import application.utils.dao.OrganizationDAO;
import org.apache.commons.configuration.ConfigurationException;

import javax.crypto.KeyGenerator;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;

public class OrganizationController extends EntityController {

    public OrganizationController(MainController mainController) {
        super(mainController);
        dao = new OrganizationDAO();
    }

    public void create(String name) throws SQLException, ConfigurationException {
        Organization organization = createOrganization(name);
        dao.insert(organization);
        update();
    }

    public void delete(Long id) throws SQLException, ConfigurationException {
        dao.delete(id);
        update();
    }

    public void update() throws SQLException, ConfigurationException {
        DefaultTableModel model = (DefaultTableModel)table.getModel();
        model.setRowCount(0);
        ArrayList<Organization> organizations = dao.getAll();
        Object[] row = new Object[3];
        for (Organization organization: organizations) {
            row[0] = organization.getId();
            row[1] = organization.getKey();
            row[2] = organization.getName();
            model.addRow(row);
        }
    }

    public void createTable() throws SQLException, ConfigurationException {

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
        table.setRowSelectionAllowed(true);
        table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(250);
        table.getColumnModel().getColumn(2).setPreferredWidth(500);
        update();
    }

    private Organization createOrganization(String name) {
        Key key = null;
        try {
            key = KeyGenerator.getInstance("DESede").generateKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        byte[] array = key.getEncoded();
        String sKey = Base64.getEncoder().encodeToString(array);
        return new Organization(name, sKey);
    }
}
