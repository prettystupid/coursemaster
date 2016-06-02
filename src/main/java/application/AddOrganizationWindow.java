package application;

import organization.Organization;

import javax.crypto.KeyGenerator;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

class AddOrganizationWindow extends JDialog{

    private JPanel infoPanel;
    private JLabel nameLabel;
    private JTextField nameTField;
    private JPanel buttonPanel;
    private JButton okButton;
    private JButton cancelButton;

    private Organization org;

    AddOrganizationWindow(JFrame owner) {
        super(owner, "Организация", true);
        initComponents();
    }

    private void initComponents() {
        infoPanel = new JPanel();
        infoPanel.setLayout(new BorderLayout());
        getContentPane().add(infoPanel, BorderLayout.CENTER);


        nameLabel = new JLabel("Введите название организации: ");
        infoPanel.add(nameLabel, BorderLayout.NORTH);

        nameTField = new JTextField();
        infoPanel.add(nameTField, BorderLayout.SOUTH);

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        okButton = new JButton("Ок");
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createOrganization(nameTField.getText());
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
        setPreferredSize(new Dimension(600, 100));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void createOrganization(String name) {
        Key key = null;
        try {
            key = KeyGenerator.getInstance("DESede").generateKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return;
        }
        byte[] array = key.getEncoded();
        String sKey = Base64.getEncoder().encodeToString(array);
        org = new Organization(name, sKey);
    }

    public Organization getOrganization() {
        return org;
    }

    private void close() {
        setVisible(false);
    }
}