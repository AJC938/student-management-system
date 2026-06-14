package app;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import ui.StudentDashboard;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Using default look and feel.");
        }

        SwingUtilities.invokeLater(() -> new StudentDashboard().setVisible(true));
    }
}
