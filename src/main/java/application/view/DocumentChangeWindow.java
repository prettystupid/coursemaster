package application.view;

import application.model.entity.Document;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DocumentChangeWindow extends JDialog{

    private JTextField uuidTField;
    private JTextField versionTField;
    private JTextField nameTField;

    private Document document;

    public DocumentChangeWindow(JFrame owner, Document document) {
        super(owner, "Изменить", true);
        this.document = new Document(document);
        initComponents();
    }

    private void initComponents() {
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridBagLayout());
        getContentPane().add(infoPanel, BorderLayout.CENTER);

        GridBagConstraints c = new GridBagConstraints();

        JLabel uuidLabel = new JLabel("UUID: ");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        infoPanel.add(uuidLabel, c);

        uuidTField = new JTextField(document.getUuid());
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 0;
        infoPanel.add(uuidTField, c);

        JLabel versionLabel = new JLabel("Версия: ");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        infoPanel.add(versionLabel, c);

        versionTField = new JTextField("" + document.getVersion());
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 1;
        infoPanel.add(versionTField, c);

        JLabel nameLabel = new JLabel("Наименование: ");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 2;
        infoPanel.add(nameLabel,c);

        nameTField = new JTextField(document.getName());
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 2;
        infoPanel.add(nameTField,c);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        JButton okButton = new JButton("Ок");
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                document.setUuid(uuidTField.getText());
                document.setName(nameTField.getText());
                try {
                    document.setVersion(Integer.parseInt(versionTField.getText()));
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Версия должна быть натуральным числом", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    return;
                }
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
