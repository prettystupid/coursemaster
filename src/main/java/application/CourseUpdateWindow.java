package application;

import courses.Course;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class CourseUpdateWindow extends JDialog{

    private JPanel infoPanel;
    private JLabel uuidLabel;
    private JLabel versionLabel;
    private JLabel nameLabel;
    private JTextField uuidTField;
    private JTextField versionTField;
    private JTextField nameTField;
    private JPanel buttonPanel;
    private JButton okButton;
    private JButton cancelButton;

    private Course course;

    CourseUpdateWindow(JFrame owner, Course course) {
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
                course.setName(nameTField.getText());
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

    Course getCourse() {
        return course;
    }
}
