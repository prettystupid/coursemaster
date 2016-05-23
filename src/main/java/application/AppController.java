package application;

import com.fasterxml.jackson.databind.ObjectMapper;
import courses.CourseInfo;
import databaseconnector.DBConnector;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

public class AppController {

    public static void uploadCourse() {
        String uuid = "";
        int version = 1;
        int dialogResult = JOptionPane.showConfirmDialog (null, "Создать новую версию к существующему курсу?", "Информация", JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.YES_OPTION) {
            ArrayList<String> uuidAndVersion = selectPreviousCourse();
            try {
                uuid = uuidAndVersion.get(0);
                version = Integer.parseInt(uuidAndVersion.get(1)) + 1;
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (version != 1) {
                if (DBConnector.findMatches(uuid, version) != 0) {
                    JOptionPane.showMessageDialog(null, "Данная версия уже существует.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        } else {
            uuid = UUID.randomUUID().toString();
            version = 1;
        }
        CourseInfo courseInfo = getInfoFromFile();
        if (courseInfo == null) {
            return;
        }
        DBConnector.insertCourse(courseInfo, uuid, version);
    }

    private static CourseInfo getInfoFromFile() {
        CourseInfo courseInfo = new CourseInfo();
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
                e.printStackTrace();
            }
            return courseInfo;
        } else {
            return null;
        }
    }

    public static void deleteCourse(String uuid, String version) {
        int ver = Integer.parseInt(version);
        DBConnector.delete(uuid, ver);
    }

    public static void downloadCourse() {
    }

    private static ArrayList<String> selectPreviousCourse() {
        ArrayList<String> result;
        JFrame frame = new JFrame();
        CourseChooserTableWindow tableWindow = new CourseChooserTableWindow(frame);
        result = tableWindow.getUuidAndVersion();
        tableWindow.dispose();
        return result;
    }

    public static void updateCourse(String uuid, String version, String name) {

    }
}
