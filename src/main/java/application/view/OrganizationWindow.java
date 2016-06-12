package application.view;

import application.controller.MainController;
import application.controller.OrganizationController;
import application.model.entity.Organization;

import javax.crypto.KeyGenerator;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Base64;

public class OrganizationWindow extends JFrame{

    private JTable table;

    private OrganizationController controller;

    public OrganizationWindow(MainController controller) {
        super("Организации");
        this.controller = new OrganizationController(controller);
        initComponents();
    }

    private void initComponents() {

        table = new JTable();
        controller.setTable(table);
        try {
            controller.createTable();
        } catch (SQLException e) {
            controller.createSQLExceptionDialog();
        } catch (Exception e) {
            controller.createConfigurationExceptionDialog();
        }
        final DefaultTableModel model = (DefaultTableModel) table.getModel();


        JScrollPane scrollPane = new JScrollPane(table);
        getContentPane().add(scrollPane);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        JButton createButton = new JButton("Создать");
        createButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                  //  controller.create();
                } catch (Exception e1) {
                    controller.createConfigurationExceptionDialog();
                }
            }
        });
        buttonPanel.add(createButton);

        JButton deleteButton = new JButton("Удалить");
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (table.getSelectedRow() != -1) {
                    try {
                        controller.delete((Long) model.getValueAt(table.getSelectedRow(), 0));
                    } catch (SQLException e1) {
                        controller.createSQLExceptionDialog();
                    } catch (Exception e1) {
                        controller.createConfigurationExceptionDialog();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Выберите элемент", "Информация", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        buttonPanel.add(deleteButton);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(700, 400));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /*private createOrganization(String name) {
        Key key = null;
        try {
            key = KeyGenerator.getInstance("DESede").generateKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return;
        }
        byte[] array = key.getEncoded();
        String sKey = Base64.getEncoder().encodeToString(array);
        return new Organization(name, sKey);
    }*/
}