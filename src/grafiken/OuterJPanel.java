package grafiken;

import helpers.PersonenManager;

import javax.swing.JPanel;
import java.awt.Color;

public abstract class OuterJPanel extends JPanel {
    private MainFrame parent;

    public OuterJPanel(MainFrame parent) {
        this.parent = parent;
        Color pc = parent.getContentPane().getBackground();
        this.setBackground(pc);
    }

    public MainFrame getParent() {
        return parent;
    }

    /** @return Zusammenhaengender PersonenManager des Parent-MainFrames. */
    public PersonenManager getPM() {
        return parent.getPersonenManager();
    }

    protected void config(Color background) {
        setBackground(background);
    }

}
