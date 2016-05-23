package application;

import courses.Course;
import databaseconnector.DBConnector;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class CourseChooserTableWindow extends JDialog{

    String[] columnNames = {
            "UUID",
            "Version",
            "Name"
    };

    JTable table;
    JPanel buttonPanel;
    JButton okButton;
    JButton cancelButton;

    ArrayList<String> uuidAndVersion = new ArrayList<String>();

    public CourseChooserTableWindow(JFrame owner) {
        super(owner, "Выберите курс", true);
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
                        "UUID", "Version", "Name"
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
        table.getColumnModel().getColumn(1).setPreferredWidth(75);
        table.getColumnModel().getColumn(2).setPreferredWidth(1500);

        JScrollPane scrollPane = new JScrollPane(table);
        getContentPane().add(scrollPane);


        okButton = new JButton("Ок");
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (table.getSelectedRow() != -1) {
                    uuidAndVersion.add(0, (String) model.getValueAt(table.getSelectedRow(), 0));
                    uuidAndVersion.add(1,  model.getValueAt(table.getSelectedRow(), 1).toString());
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
                uuidAndVersion.add(0, UUID.randomUUID().toString());
                uuidAndVersion.add(1, "1");
                close();
            }
        });
        buttonPanel.add(cancelButton);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setResizable(false);
        setPreferredSize(new Dimension(800, 450));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
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

    private void close() {
        setVisible(false);
    }

    public ArrayList<String> getUuidAndVersion() {
        return uuidAndVersion;
    }
}
