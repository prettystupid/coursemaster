package application.view;

import application.controller.MainController;
import application.controller.OrganizationController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class OrganizationWindow extends JFrame{

    private JTable table;
    private JLabel nameLabel;
    private JTextField nameTField;

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
        } catch (Exception e) {
            controller.catchException(e);
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
                    String name = JOptionPane.showInputDialog(null, "Введите название организации", "Создать", JOptionPane.INFORMATION_MESSAGE);
                    if (name == null) {
                        return;
                    } else if ("".equals(name)) {
                        JOptionPane.showMessageDialog(null, "Название организации не может быть пустым", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    } else {
                        controller.create(name);
                    }
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
                    }  catch (Exception ex) {
                        controller.catchException(ex);
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
}