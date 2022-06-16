import grafiken.MainFrame;

import javax.swing.SwingUtilities;

/**
 * Der Vereinskassen-Launcher umfasst den Startpunkt des Programmes.
 */
public class VKLauncher {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }

}
