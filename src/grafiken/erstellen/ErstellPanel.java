package grafiken.erstellen;

import grafiken.MainFrame;
import grafiken.OuterJPanel;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

public class ErstellPanel extends OuterJPanel {

    private InputFormPanel form = new InputFormPanel();

    public ErstellPanel(MainFrame frame) {
        super(frame);
        config();
        addOuterPanels();
    }

    private void addOuterPanels() {
        this.add(getCenterPanel(), BorderLayout.CENTER);
        this.add(getEastPanel(), BorderLayout.EAST);
    }

    private JPanel getEastPanel() {
        JPanel panel = new JPanel();
        Border border = BorderFactory.createMatteBorder(0,2,0,0,Color.BLACK);

        panel.setPreferredSize(new Dimension(200,0));
        panel.setBorder(border);
        panel.setBackground(Color.lightGray);
        return panel;
    }

    private JPanel getCenterPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BorderLayout());

        panel.add(form, BorderLayout.CENTER);
        panel.add(getBackPanel(), BorderLayout.SOUTH);
        return panel;
    }

    private JPanel getBackPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(0,70));
        panel.setBorder(new MatteBorder(0,1,0,0, Color.BLACK));
        //TODO implement back button
        return panel;
    }

    private void config() {
        setLayout(new BorderLayout());
    }
}
