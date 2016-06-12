package application.view.main;

import application.controller.MainController;
import application.model.entity.Organization;
import application.utils.PanelFactory;
import application.model.entity.DocumentType;
import application.view.OrganizationWindow;
import org.apache.commons.configuration.ConfigurationException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindow extends JFrame{

    private MainController controller;

    public MainWindow(MainController controller) {
        super("Master");
        this.controller = controller;
        initComponents();
    }

    private void initComponents() {

        JTabbedPane tabbedPane = new JTabbedPane();
        getContentPane().add(tabbedPane);

        JPanel coursePanel = PanelFactory.createPanel(DocumentType.COURSE, controller);
        tabbedPane.addTab("Курсы", coursePanel);

        JPanel ntdPanel = PanelFactory.createPanel(DocumentType.NTD, controller);
        tabbedPane.addTab("Нормативно-технические документы", ntdPanel);

        JMenuBar menuBar = new JMenuBar();
        JMenu mainMenu = new JMenu("Меню");
        menuBar.add(mainMenu);

        JMenuItem addOrgItem = new JMenuItem("Организации");
        addOrgItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initOrganizationWindow();
            }
        });
        mainMenu.add(addOrgItem);

        JMenuItem dbSettingsItem = new JMenuItem("Настройка базы данных");
        dbSettingsItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.setDB();
            }
        });
        mainMenu.add(dbSettingsItem);

        JMenuItem createDBItem = new JMenuItem("Создать базу данных");
        createDBItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.createDB();
            }
        });
        mainMenu.add(createDBItem);

        setJMenuBar(menuBar);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1500, 450));
        pack();
        setLocationRelativeTo(null);
    }

    private void initOrganizationWindow() {
        OrganizationWindow window = new OrganizationWindow(controller);
    }
}
