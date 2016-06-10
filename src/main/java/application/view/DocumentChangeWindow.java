package application.view;

import application.model.entity.Document;
import application.model.entity.course.Course;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DocumentChangeWindow extends JDialog{

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

    private Document document;

    public DocumentChangeWindow(JFrame owner, Document document) {
        super(owner, "Изменить", true);
        this.document = new Document(document);
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

        uuidTField = new JTextField(document.getUuid());
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 0;
        infoPanel.add(uuidTField, c);

        versionLabel = new JLabel("Версия: ");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        infoPanel.add(versionLabel, c);

        versionTField = new JTextField("" + document.getVersion());
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 1;
        infoPanel.add(versionTField, c);

        nameLabel = new JLabel("Наименование: ");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 2;
        infoPanel.add(nameLabel,c);

        nameTField = new JTextField(document.getName());
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
                document.setUuid(uuidTField.getText());
                document.setName(nameTField.getText());
                try {
                    document.setVersion(Integer.parseInt(versionTField.getText()));
                } catch (NumberFormatException ex) {
                    JOptionPane.showConfirmDialog(null, "Версия должна быть натуральным числом", "Ошибка", JOptionPane.CLOSED_OPTION);
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

    public Document getDocument() {
        return document;
    }
}
