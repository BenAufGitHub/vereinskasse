package grafiken;

import helpers.PersonenManager;

import javax.swing.JPanel;
import java.awt.Color;

public abstract class OuterJPanel extends JPanel {
    private MainFrame frame;

    public OuterJPanel(MainFrame frame) {
        this.frame = frame;
        Color pc = frame.getContentPane().getBackground();
        this.setBackground(pc);
    }

    public MainFrame getFrame() {
        return frame;
    }

    /** @return Zusammenhaengender PersonenManager des Parent-MainFrames. */
    public PersonenManager getPM() {
        return frame.getPersonenManager();
    }

    protected void config(Color background) {
        setBackground(background);
    }

}
