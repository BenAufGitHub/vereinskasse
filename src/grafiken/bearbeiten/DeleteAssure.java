package grafiken.bearbeiten;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class DeleteAssure extends JDialog {

    private boolean result;

    public DeleteAssure (JFrame frame) {
        setSize(170,120);
        setModal(true);
        setTitle("Person entfernen");
        setLayout(new BorderLayout());
        setLocationRelativeTo(frame);

        JLabel label = new JLabel("Wirklich entfernen?", SwingConstants.CENTER);
        JButton ja = new JButton("Ja");
        JButton nein = new JButton("Nein");

        ja.setBackground(Color.RED);
        ja.setFocusable(false);
        nein.setOpaque(false);
        nein.setFocusable(false);

        addActions(ja, nein);
        JPanel buttonPanel = getButtonPanel(ja, nein);
        buttonPanel.setPreferredSize(new Dimension(80,40));

        add(label, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addActions(JButton ja, JButton nein) {
        ja.addActionListener((e) -> {
            result = true;
            setVisible(false);
        });
        nein.addActionListener((e) -> {
            result = false;
            setVisible(false);
        });
    }

    private JPanel getButtonPanel(JButton ja, JButton nein) {
        JPanel panel = new JPanel();
        panel.add(nein);
        panel.add(ja);
        return panel;
    }

    public boolean getResult() {
        return result;
    }
}
