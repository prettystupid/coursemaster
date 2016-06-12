package application.view;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DBChangeWindow extends JDialog {

    private JTextField usernameTField;
    private JTextField passwordTField;
    private JTextField dbNameTField;

    private XMLConfiguration config;

    public DBChangeWindow(JFrame owner) throws ConfigurationException {
        super(owner, "Настройки", true);
        config = new XMLConfiguration("src\\main\\resources\\config.xml");
        initComponents();
    }

    private void initComponents() {
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridBagLayout());
        getContentPane().add(infoPanel, BorderLayout.CENTER);

        GridBagConstraints c = new GridBagConstraints();

        JLabel usernameLabel = new JLabel("Имя пользователя: ");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        infoPanel.add(usernameLabel, c);

        usernameTField = new JTextField(config.getString("username"));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 0;
        infoPanel.add(usernameTField, c);

        JLabel passwordLabel = new JLabel("Пароль: ");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        infoPanel.add(passwordLabel, c);

        passwordTField = new JPasswordField(config.getString("password"));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 1;
        infoPanel.add(passwordTField, c);

        JLabel dbNameLabel = new JLabel("Наименование БД: ");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 2;
        infoPanel.add(dbNameLabel, c);

        dbNameTField = new JTextField(config.getString("db-name"));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 2;
        infoPanel.add(dbNameTField, c);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        JButton okButton = new JButton("Ок");
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                config.setProperty("username", usernameTField.getText());
                config.setProperty("password", passwordTField.getText());
                config.setProperty("db-name", dbNameTField.getText());
                close();
            }
        });
        buttonPanel.add(okButton);

        JButton cancelButton = new JButton("Отмена");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                close();
            }
        });
        buttonPanel.add(cancelButton);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setPreferredSize(new Dimension(400, 200));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }


    private void close() {
        setVisible(false);
    }

    public XMLConfiguration getConfig() {
        return config;
    }
}