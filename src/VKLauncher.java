import grafiken.MainFrame;
import helpers.Query;

import javax.swing.SwingUtilities;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

/**
 * Der Vereinskassen-Launcher umfasst den Startpunkt des Programmes.
 */
public class VKLauncher {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            addExitListener(frame);
            try {
                Query.connect();
                frame.setVisible(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private static void addExitListener(MainFrame frame) {
        frame.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                try {
                    Query.disconnect();
                } catch (SQLException ex) {}
            }
        });
    }

}
