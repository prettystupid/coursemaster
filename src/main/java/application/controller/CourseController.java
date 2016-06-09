package application.controller;

import application.model.courses.Course;
import application.model.courses.CourseInfo;
import application.model.download.DownloadedCourse;
import application.utils.DBConnector;
import application.utils.Encrypter;
import application.utils.dao.CourseDAO;
import application.view.chooser.CourseChooserTableWindow;
import application.view.chooser.OrgChooserTableWindow;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.UUID;

public class CourseController extends Controller {

    public CourseController() {
        dao = new CourseDAO();
    }

    @Override
    public void upload() {
        String uuid = "";
        int version = 1;
        int dialogResult = JOptionPane.showConfirmDialog(null, "Создать новую версию к существующему курсу?", "Информация", JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.YES_OPTION) {
            ArrayList<String> uuidAndVersion = selectPreviousCourse();
            try {
                uuid = uuidAndVersion.get(0);
                version = Integer.parseInt(uuidAndVersion.get(1)) + 1;
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (version != 1) {
                if (DBConnector.getId(uuid, version) != -1) {
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
        DBConnector.insertCourse(courseInfo, uuid, version);
    }

    @Override
    public void download() {

    }

    @Override
    public void change() {

    }

    @Override
    public void delete() {

    }

    @Override
    public void update() {
        JTable table = view.getTable();
        DefaultTableModel model = (DefaultTableModel)table.getModel();
        model.setRowCount(0);
        ArrayList<Course> courses = DBConnector.getCourses();
        Object[] row = new Object[4];
        for (Course course: courses) {
            row[0] = course.getId();
            row[1] = course.getUuid();
            row[2] = course.getVersion();
            row[3] = course.getName();
            model.addRow(row);
        }
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

    public void deleteCourse(String uuid, String version) {
        int ver = Integer.parseInt(version);
        DBConnector.delete(uuid, ver);
    }

    public void downloadCourse(String uuid, String version) {
        String stringKey = getKeyByOrg();
        int ver = Integer.parseInt(version);
        DownloadedCourse course = DBConnector.getDownloadedCourse(uuid, ver);
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

    private String getKeyByOrg() {
        JFrame frame = new JFrame();
        OrgChooserTableWindow tableWindow = new OrgChooserTableWindow(frame);
        String key = tableWindow.getKey();
        tableWindow.dispose();
        return key;
    }

    private ArrayList<String> selectPreviousCourse() {
        ArrayList<String> result;
        JFrame frame = new JFrame();
        CourseChooserTableWindow tableWindow = new CourseChooserTableWindow(frame);
        result = tableWindow.getUuidAndVersion();
        tableWindow.dispose();
        return result;
    }

    /*public void updateCourse(String uuid, String version) {
        JFrame frame = new JFrame();
        int ver = Integer.parseInt(version);
        Course course = DBConnector.getCourse(uuid, ver);
        CourseUpdateWindow courseWindow = new CourseUpdateWindow(frame, course);
        Course newCourse = courseWindow.getCourse();
        courseWindow.dispose();
        if (!course.equals(newCourse)) {
            DBConnector.updateCourse(newCourse);
        }
    }

    public void addOrganization() {
        JFrame frame = new JFrame();
        AddOrganizationWindow window = new AddOrganizationWindow(frame);
        Organization org = window.getOrganization();
        window.dispose();
        if (org == null) {
            return;
        }
        DBConnector.createOrganization(org);
    }*/

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
