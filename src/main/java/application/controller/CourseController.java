package application.controller;

import application.model.download.course.DownloadedCourse;
import application.model.entity.Document;
import application.model.entity.IEntity;
import application.model.entity.Organization;
import application.model.entity.course.Course;
import application.model.entity.course.CourseInfo;
import application.utils.Encrypter;
import application.utils.dao.CourseDAO;
import application.utils.dao.DocumentDAO;
import application.view.EntityChooserWindow;
import application.view.DocumentChangeWindow;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.io.FileUtils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.UUID;

import static com.sun.imageio.plugins.jpeg.JPEG.version;

public class CourseController extends DocumentController {

    public CourseController(MainController mainController) {
        super(mainController);
        dao = new CourseDAO();
    }

    @Override
    public void upload() throws SQLException, ConfigurationException {
        String uuid = "";
        long version = 1;
        int dialogResult = JOptionPane.showConfirmDialog(null, "Создать новую версию к существующему курсу?", "Информация", JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.YES_OPTION) {
            Course course = selectPreviousCourse();
            uuid = course.getUuid();
            version = course.getVersion() + 1;
            if (version != 1) {
                if (((DocumentDAO) dao).findMatches(uuid, version) != -1) {
                    JOptionPane.showMessageDialog(null, "Данная версия уже существует.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        } else {
            uuid = UUID.randomUUID().toString();
            version = 1;
        }
        CourseInfo courseInfo = getInfoFromJSON();
        if (courseInfo == null) {
            return;
        }
        Course course = new Course(courseInfo, uuid, version);
        dao.insert(course);
        update();
    }

    @Override
    public void download(Long id) throws SQLException, ConfigurationException {
        String stringKey = getKeyByOrg();
        DownloadedCourse course = ((DownloadedCourse) ((DocumentDAO) dao).getDownloadedObject(id));
        if (course == null) {
            JOptionPane.showMessageDialog(null, "Курс не найден", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }
        JFileChooser directoryChooser = new JFileChooser("C:\\");
        directoryChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        File directory;
        int ret = directoryChooser.showDialog(null, "Открыть файл");
        if (ret == JFileChooser.APPROVE_OPTION) {
            directory = directoryChooser.getSelectedFile();
        } else {
            return;
        }
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = null;
        try {
            jsonString = mapper.writeValueAsString(course);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        byte[] courseBytes = objToBytes(jsonString);
        byte[] decodedKey = Base64.getDecoder().decode(stringKey);
        SecretKey key = new SecretKeySpec(decodedKey, 0, decodedKey.length, "DESede");
        Encrypter encrypter = new Encrypter(key);
        byte[] result = encrypter.encrypt(courseBytes);
        try {
            FileUtils.writeByteArrayToFile(new File(directory + "\\course" + course.getId()+ "\\course.course"), result);
            FileUtils.writeStringToFile(new File(directory + "\\course" + course.getId()+ "\\key.txt"), stringKey);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void change(Long id) throws SQLException, ConfigurationException {
        JFrame frame = new JFrame();
        Course course = (Course) dao.getById(id);
        DocumentChangeWindow courseWindow = new DocumentChangeWindow(frame, course);
        Course newCourse = new Course(course);
        Document document = courseWindow.getDocument();
        newCourse.setUuid(document.getUuid());
        newCourse.setVersion(document.getVersion());
        newCourse.setName(document.getName());
        courseWindow.dispose();
        if (!course.equals(newCourse)) {
            ((DocumentDAO) dao).change(newCourse);
            update();
        }
    }

    @Override
    public void delete(Long id) throws SQLException, ConfigurationException {
        dao.delete(id);
        update();
    }

    @Override
    public void update() throws SQLException, ConfigurationException {
        DefaultTableModel model = (DefaultTableModel)table.getModel();
        model.setRowCount(0);
        ArrayList<Course> courses;
        courses = dao.getAll();
        Object[] row = new Object[4];
        for (Course course: courses) {
            row[0] = course.getId();
            row[1] = course.getUuid();
            row[2] = course.getVersion();
            row[3] = course.getName();
            model.addRow(row);
        }
    }

    @Override
    public void createTable() throws SQLException, ConfigurationException {
        table.setModel(new DefaultTableModel(
                new Object [][] {},
                new String [] {
                        "ID", "UUID", "Версия", "Имя"
                }
        )   {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(250);
        table.getColumnModel().getColumn(2).setPreferredWidth(75);
        table.getColumnModel().getColumn(3).setPreferredWidth(1500);
        update();
    }

    private CourseInfo getInfoFromJSON() {
        CourseInfo courseInfo;
        JFileChooser fileopen = new JFileChooser("C:\\");
        int ret = fileopen.showDialog(null, "Открыть файл");
        if (ret == JFileChooser.APPROVE_OPTION) {
            File file = fileopen.getSelectedFile();
            if (file == null) {
                return null;
            }
            ObjectMapper mapper = new ObjectMapper();
            try {
                courseInfo = mapper.readValue(file, CourseInfo.class);

            } catch(Exception e) {
                JOptionPane.showMessageDialog(null,"Неверный формат файла", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return null;
            }
            return courseInfo;
        } else {
            return null;
        }
    }

    private Course selectPreviousCourse() {
        Course course;
        JFrame frame = new JFrame();
        EntityChooserWindow tableWindow = new EntityChooserWindow(frame, new CourseController(mainController));
        course = (Course) tableWindow.getEntity();
        tableWindow.dispose();
        if (course == null) {
            course = new Course();
            course.setUuid(UUID.randomUUID().toString());
            course.setVersion(0);
        }
        return course;
    }

    private String getKeyByOrg() {
        JFrame frame = new JFrame();
        EntityChooserWindow tableWindow = new EntityChooserWindow(frame, new OrganizationController(mainController));
        Organization org = ((Organization) tableWindow.getEntity());
        tableWindow.dispose();
        return org.getKey();
    }



    private byte[] objToBytes(Object object) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(object);
            byte[] result = bos.toByteArray();
            if (out != null) {
                out.close();
            }
            bos.close();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
