package application.view.main;

import application.controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ShowPanel extends JPanel{

    private Controller controller;

    private JTable table;

    public ShowPanel(Controller controller) {
        super();
        setLayout(new BorderLayout());
        this.controller = controller;
        initComponents();
    }

    private void initComponents() {

        table = new JTable();

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        add(buttonPanel, BorderLayout.SOUTH);

        JButton uploadButton = new JButton("Загрузить");
        uploadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.upload();
            }
        });
        uploadButton.setSize(150,50);
        buttonPanel.add(uploadButton);

        JButton downloadButton = new JButton("Выгрузить");
        downloadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (table.getSelectedRow() != -1) {
                    controller.download();
                } else {
                    JOptionPane.showMessageDialog(null, "Выберите элемент", "Информация", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        downloadButton.setSize(150,50);
        buttonPanel.add(downloadButton);

        JButton changeButton = new JButton("Изменить");
        changeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (table.getSelectedRow() != -1) {
                    controller.change();
                } else {
                    JOptionPane.showMessageDialog(null, "Выберите элемент", "Информация", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        changeButton.setSize(150,50);
        buttonPanel.add(changeButton);

        JButton deleteButton = new JButton("Удалить");
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (table.getSelectedRow() != -1) {
                    controller.delete();
                } else {
                    JOptionPane.showMessageDialog(null, "Выберите элемент", "Информация", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        deleteButton.setSize(150,50);
        buttonPanel.add(deleteButton);

        JButton updateButton = new JButton("Обновить");
        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.update();
            }
        });
        deleteButton.setSize(150,50);
        buttonPanel.add(updateButton);
    }

    public JTable getTable() {
        return table;
    }
}
