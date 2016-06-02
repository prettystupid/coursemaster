package application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import courses.Course;
import courses.CourseInfo;
import databaseconnector.DBConnector;
import download.DownloadedCourse;
import encrypt.Encrypter;
import org.apache.commons.io.FileUtils;
import organization.Organization;

import javax.activation.MimetypesFileTypeMap;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Key;
import java.util.ArrayList;
import java.util.Base64;
import java.util.UUID;

class AppController {

    static void uploadCourse() {
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

    private static CourseInfo getInfoFromJSON() {
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

    static void deleteCourse(String uuid, String version) {
        int ver = Integer.parseInt(version);
        DBConnector.delete(uuid, ver);
    }

    static void downloadCourse(String uuid, String version) {
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
        byte[] courseBytes = Serializer.objToBytes(jsonString);
        Encrypter encrypter = new Encrypter();
        byte[] decodedKey = Base64.getDecoder().decode(stringKey);
        SecretKey key = new SecretKeySpec(decodedKey, 0, decodedKey.length, "DESede");
        encrypter.setKey(key);
        byte[] result = encrypter.encrypt(courseBytes);
        try {
            FileUtils.writeByteArrayToFile(new File(directory + "\\course" + course.getId()+ "\\course.course"), result);
            FileUtils.writeStringToFile(new File(directory + "\\course" + course.getId()+ "\\key.txt"), stringKey);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getKeyByOrg() {
        JFrame frame = new JFrame();
        OrgChooserTableWindow tableWindow = new OrgChooserTableWindow(frame);
        String key = tableWindow.getKey();
        tableWindow.dispose();
        return key;
    }

    private static ArrayList<String> selectPreviousCourse() {
        ArrayList<String> result;
        JFrame frame = new JFrame();
        CourseChooserTableWindow tableWindow = new CourseChooserTableWindow(frame);
        result = tableWindow.getUuidAndVersion();
        tableWindow.dispose();
        return result;
    }

    static void updateCourse(String uuid, String version) {
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

    public static void addOrganization() {
        JFrame frame = new JFrame();
        AddOrganizationWindow window = new AddOrganizationWindow(frame);
        Organization org = window.getOrganization();
        window.dispose();
        if (org == null) {
            return;
        }
        DBConnector.createOrganization(org);
    }
}
