package application;


import courses.Course;
import databaseconnector.DBConnector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class WindowApp extends JFrame {
    JTable table;
    JPanel buttonPanel;
    JButton uploadCourseButton;
    JButton downloadCourseButton;
    JButton deleteCourseButton;
    JButton updateCourseButton;

    public WindowApp() {
        super("CourseMaster");
        initComponents();
        showTable();
    }

    private void initComponents() {

        table = new JTable();
        table.setModel(new DefaultTableModel(
                new Object [][] {},
                new String [] {
                        "UUID", "Version", "Name"
                }
        )   {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        final DefaultTableModel model = (DefaultTableModel) table.getModel();
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        table.getColumnModel().getColumn(0).setPreferredWidth(250);
        table.getColumnModel().getColumn(1).setPreferredWidth(75);
        table.getColumnModel().getColumn(2).setPreferredWidth(1500);

        JScrollPane scrollPane = new JScrollPane(table);
        getContentPane().add(scrollPane);

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        uploadCourseButton = new JButton("Загрузить курс");
        uploadCourseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                uploadCourse();
                update();
            }
        });
        uploadCourseButton.setSize(150,50);
        uploadCourseButton.setLocation(25, 1090);
        buttonPanel.add(uploadCourseButton);

        downloadCourseButton = new JButton("Выгрузить курс");
        downloadCourseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                downloadCourse();
            }
        });
        downloadCourseButton.setSize(150,50);
        downloadCourseButton.setLocation(25, 1090);
        buttonPanel.add(downloadCourseButton);

        updateCourseButton = new JButton("Изменить курс");
        updateCourseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (table.getSelectedRow() != -1) {
                    updateCourse((String) model.getValueAt(table.getSelectedRow(), 0),  model.getValueAt(table.getSelectedRow(), 1).toString());
                    update();
                } else {
                    JOptionPane.showMessageDialog(null, "Выберите курс.", "Информация", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        updateCourseButton.setSize(150,50);
        updateCourseButton.setLocation(25, 1090);
        buttonPanel.add(updateCourseButton);

        deleteCourseButton = new JButton("Удалить курс");
        deleteCourseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (table.getSelectedRow() != -1) {
                    deleteCourse((String) model.getValueAt(table.getSelectedRow(), 0),  model.getValueAt(table.getSelectedRow(), 1).toString());
                    update();
                } else {
                    JOptionPane.showMessageDialog(null, "Выберите курс.", "Информация", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        deleteCourseButton.setSize(150,50);
        deleteCourseButton.setLocation(25, 1090);
        buttonPanel.add(deleteCourseButton);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setPreferredSize(new Dimension(1500, 350));
        pack();
        setLocationRelativeTo(null);
    }

    private void updateCourse(String uuid, String version) {
        AppController.updateCourse(uuid, version);
    }

    private void deleteCourse(String uuid, String version) {
        AppController.deleteCourse(uuid, version);
    }

    private void downloadCourse() {
        AppController.downloadCourse();
    }

    private void uploadCourse() {
        AppController.uploadCourse();
    }

    private void showTable() {
        ArrayList<Course> courses = DBConnector.getCourses();
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        Object[] row = new Object[3];
        for (Course course: courses) {
            row[0] = course.getUuid();
            row[1] = course.getVersion();
            row[2] = course.getName();
            model.addRow(row);
        }
    }

    private void update() {
        DefaultTableModel model = (DefaultTableModel)table.getModel();
        model.setRowCount(0);
        showTable();
    }

}
