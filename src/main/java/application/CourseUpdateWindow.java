package application;

import courses.Course;
import databaseconnector.DBConnector;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.UUID;

public class CourseUpdateWindow extends JDialog{

    JPanel infoPanel;
    JLabel uuidLabel;
    JLabel versionLabel;
    JLabel nameLabel;
    JTextField uuidTField;
    JTextField versionTField;
    JTextField nameTField;
    JPanel buttonPanel;
    JButton okButton;
    JButton cancelButton;

    Course course;

    public CourseUpdateWindow(JFrame owner, Course course) {
        super(owner, "Курс", true);
        this.course = new Course(course);
        initComponents();
    }

    private void initComponents() {
        infoPanel = new JPanel();
        infoPanel.setLayout(new GridBagLayout());
        getContentPane().add(infoPanel, BorderLayout.CENTER);

        GridBagConstraints c = new GridBagConstraints();

        uuidLabel = new JLabel("UUID: ");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        infoPanel.add(uuidLabel, c);

        uuidTField = new JTextField(course.getUuid());
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 0;
        infoPanel.add(uuidTField, c);

        versionLabel = new JLabel("Версия: ");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        infoPanel.add(versionLabel, c);

        versionTField = new JTextField("" + course.getVersion());
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 1;
        infoPanel.add(versionTField, c);

        nameLabel = new JLabel("Наименование: ");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 2;
        infoPanel.add(nameLabel,c);

        nameTField = new JTextField(course.getName());
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 2;
        infoPanel.add(nameTField,c);

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        okButton = new JButton("Ок");
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                course.setUuid(uuidTField.getText());
                try {
                    course.setVersion(Integer.parseInt(versionTField.getText()));
                } catch (NumberFormatException ex) {
                    JOptionPane.showConfirmDialog(null, "Версия должны быть натуральным числом", "Ошибка", JOptionPane.CLOSED_OPTION);
                    return;
                }
                close();
            }
        });
        buttonPanel.add(okButton);

        cancelButton = new JButton("Отмена");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                close();
            }
        });
        buttonPanel.add(cancelButton);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }


    private void close() {
        setVisible(false);
    }

    public Course getCourse() {
        return course;
    }
}
