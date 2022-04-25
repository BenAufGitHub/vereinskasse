package grafiken.bearbeiten;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;

public class SpeicherPopUp extends JDialog{

    private boolean result = false;

    public SpeicherPopUp(JFrame parent) {
        setModal(true);
        setLocationRelativeTo(parent);
        setTitle("Warnung");
        setLayout(new BorderLayout());
        JPanel panel = new JPanel();
        addComponents(panel);
        add(panel, BorderLayout.CENTER);
        add(getLabel(), BorderLayout.NORTH);
        pack();
        setVisible(true);
    }

    private void addComponents(JPanel panel) {
        JButton nein = new JButton("Nein");
        nein.setBackground(Color.ORANGE);
        nein.setFocusable(false);
        JButton ja = new JButton("Ja");
        ja.setFocusable(false);

        nein.addActionListener((e) -> end(false));
        ja.addActionListener((e) -> end(true));

        panel.add(nein);
        panel.add(ja);
    }

    private void end(boolean b) {
        result = b;
        setVisible(false);
    }

    private JComponent getLabel() {
        JLabel label = new JLabel("Veränderungen speichern?");
        label.setHorizontalTextPosition(SwingConstants.CENTER);
        JPanel panel = new JPanel();
        panel.add(label);
        return panel;
    }


    public boolean getResult() {
        return result;
    }

}
