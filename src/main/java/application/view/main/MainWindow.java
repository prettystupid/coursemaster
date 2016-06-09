package application.view.main;

import application.controller.MainWindowController;
import application.utils.PanelFactory;
import application.view.EntityType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindow extends JFrame{

    JTabbedPane tabbedPane;
    MainWindowController controller;

    public MainWindow(MainWindowController controller) {
        super("Master");
        this.controller = controller;
        initComponents();
    }

    private void initComponents() {

        tabbedPane = new JTabbedPane();
        getContentPane().add(tabbedPane);

        JPanel coursePanel = PanelFactory.createPanel(EntityType.COURSE);
        tabbedPane.addTab("Курсы", coursePanel);

        JPanel ntdPanel = PanelFactory.createPanel(EntityType.NTD);
        tabbedPane.addTab("Нормативно-технические документы", ntdPanel);

        JMenuBar menuBar = new JMenuBar();
        JMenu mainMenu = new JMenu("Меню");
        menuBar.add(mainMenu);

        JMenuItem addOrgItem = new JMenuItem("Добавить организацию");
        addOrgItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.addOrganization();
            }
        });
        mainMenu.add(addOrgItem);

        JMenuItem dbSettingsItem = new JMenuItem("Настройки базы данных");
        addOrgItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.setDB();
            }
        });
        mainMenu.add(dbSettingsItem);

        setJMenuBar(menuBar);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setPreferredSize(new Dimension(1500, 450));
        pack();
        setLocationRelativeTo(null);
    }
}
