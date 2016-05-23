import application.WindowApp;

public class CourseMaster {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                WindowApp app = new WindowApp();
                app.setVisible(true);
            }
        });
    }
}