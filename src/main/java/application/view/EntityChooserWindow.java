package application.view;

import application.controller.entitycontroller.EntityController;
import application.model.entity.IEntity;
import org.apache.commons.configuration.ConfigurationException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class EntityChooserWindow extends JDialog {

    private JTable table;
    private JPanel buttonPanel;
    private JButton okButton;
    private JButton cancelButton;

    private EntityController controller;

    private IEntity entity;

    public EntityChooserWindow(JFrame owner, EntityController controller) {
        super(owner, "Выберите элемент", true);
        this.controller = controller;
        initComponents();
    }

    private void initComponents() {
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        table = new JTable();
        controller.setTable(table);
        try {
            controller.createTable();
        } catch (SQLException e) {
            close();
            controller.createSQLExceptionDialog();
            return;
        } catch (ConfigurationException e) {
            close();
            controller.createConfigurationExceptionDialog();
            return;
        }

        final DefaultTableModel model = (DefaultTableModel) table.getModel();

        JScrollPane scrollPane = new JScrollPane(table);
        getContentPane().add(scrollPane);


        okButton = new JButton("Ок");
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (table.getSelectedRow() != -1) {
                    try {
                        entity = controller.getDao().getById((Long) model.getValueAt(table.getSelectedRow(), 0));
                    } catch (SQLException | ConfigurationException ex) {
                        controller.createSQLExceptionDialog();
                        entity = null;
                        return;
                    } catch (Exception ex) {
                        controller.createConfigurationExceptionDialog();
                        entity = null;
                        return;
                    }
                    close();
                } else {
                    JOptionPane.showMessageDialog(null, "Выберите элемент", "Информация", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        buttonPanel.add(okButton);

        cancelButton = new JButton("Отмена");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                entity = null;
                close();
            }
        });
        buttonPanel.add(cancelButton);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setResizable(false);
        setPreferredSize(new Dimension(750, 450));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void close() {
        setVisible(false);
    }

    public IEntity getEntity() {
        return entity;
    }
}
