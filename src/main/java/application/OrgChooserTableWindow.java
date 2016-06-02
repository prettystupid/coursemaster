package application;

import databaseconnector.DBConnector;
import organization.Organization;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class OrgChooserTableWindow extends JDialog{

    private JTable table;
    private JPanel buttonPanel;
    private JButton okButton;
    private JButton cancelButton;

    private String key;

    public OrgChooserTableWindow(JFrame owner) {
        super(owner, "Выберите организацию", true);
        initComponents();
    }

    private void initComponents() {
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        table = new JTable();
        table.setModel(new DefaultTableModel(
                new Object [][] {},
                new String [] {
                        "SecretKey", "Name"
                    }
            )   {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            });
        showTable();
        final DefaultTableModel model = (DefaultTableModel) table.getModel();
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        table.getColumnModel().getColumn(0).setPreferredWidth(250);
        table.getColumnModel().getColumn(1).setPreferredWidth(500);

        JScrollPane scrollPane = new JScrollPane(table);
        getContentPane().add(scrollPane);


        okButton = new JButton("Ок");
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (table.getSelectedRow() != -1) {
                    key = (String) model.getValueAt(table.getSelectedRow(), 0);
                    close();
                } else {
                    JOptionPane.showMessageDialog(null, "Выберите курс.", "Информация", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        buttonPanel.add(okButton);

        cancelButton = new JButton("Отмена");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                key = null;
                close();
            }
        });
        buttonPanel.add(cancelButton);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setResizable(false);
        setPreferredSize(new Dimension(750, 450));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void showTable() {
        ArrayList<Organization> organizations = DBConnector.getOrganizations();
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        Object[] row = new Object[3];
        for (Organization organization: organizations) {
            row[0] = organization.getKey();
            row[1] = organization.getName();
            model.addRow(row);
        }
    }

    private void close() {
        setVisible(false);
    }

    public String getKey() {
        return key;
    }
}
