package application.view.main;

import application.controller.DocumentController;
import org.apache.commons.configuration.ConfigurationException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class DocumentPanel extends JPanel{

    private DocumentController controller;

    private JTable table;

    public DocumentPanel(DocumentController controller) {
        super();
        setLayout(new BorderLayout());
        this.controller = controller;
        initComponents();
    }

    private void initComponents() {

        table = new JTable();
        controller.setTable(table);
        try {
            controller.createTable();
        }  catch (Exception e) {
            controller.catchException(e);
        }
        final DefaultTableModel model = (DefaultTableModel) table.getModel();


        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        add(buttonPanel, BorderLayout.SOUTH);

        JButton uploadButton = new JButton("Загрузить");
        uploadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    controller.upload();
                }  catch (Exception ex) {
                    controller.catchException(ex);
                }
            }
        });
        buttonPanel.add(uploadButton);

        JButton downloadButton = new JButton("Выгрузить");
        downloadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (table.getSelectedRow() != -1) {
                    try {
                        controller.download((Long) model.getValueAt(table.getSelectedRow(), 0));
                    } catch (Exception ex) {
                        controller.catchException(ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Выберите элемент", "Информация", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        buttonPanel.add(downloadButton);

        final JButton changeButton = new JButton("Изменить");
        changeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (table.getSelectedRow() != -1) {
                    try {
                        controller.change((Long) model.getValueAt(table.getSelectedRow(), 0));
                    } catch (Exception ex) {
                        controller.catchException(ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Выберите элемент", "Информация", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        buttonPanel.add(changeButton);

        JButton deleteButton = new JButton("Удалить");
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (table.getSelectedRow() != -1) {
                    try {
                        controller.delete((Long) model.getValueAt(table.getSelectedRow(), 0));
                    } catch (Exception ex) {
                        controller.catchException(ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Выберите элемент", "Информация", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        buttonPanel.add(deleteButton);

        JButton updateButton = new JButton("Обновить");
        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    controller.update();
                } catch (Exception ex) {
                    controller.catchException(ex);
                }
            }
        });
        buttonPanel.add(updateButton);
    }

    public JTable getTable() {
        return table;
    }
}
