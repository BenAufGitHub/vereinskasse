package grafiken.erstellen;

import com.sun.java.accessibility.util.SwingEventMonitor;
import grafiken.MainFrame;
import grafiken.OuterJPanel;
import users.Person;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ErstellPanel extends OuterJPanel implements FormListener{

    private InputFormPanel form = new InputFormPanel();
    private JButton bearbeiten;
    private JButton speichern;
    private JButton back;

    public ErstellPanel(MainFrame frame) {
        super(frame);
        config();
        addOuterPanels();
    }

    private void config() {
        setLayout(new BorderLayout());
        addBackFuctionality(this);
        addBackFuctionality(form.getVorTextfield());
        addBackFuctionality(form.getNachTextfield());
        form.setFormLister(this);
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

        bearbeiten = getBearbeitenButton();
        speichern = getSpeichernButton();

        panel.add(bearbeiten);
        panel.add(speichern);
        return panel;
    }

    private JButton getBearbeitenButton() {
        JButton button = new JButton("Speichern & Bearbeiten");
        addBackFuctionality(button);
        button.addActionListener((e) -> {
            if(!form.isRegExApproved()){
                sendNameError();
                return;
            }
            Person p = createNewPerson();
            getPM().save(p, null);
            getFrame().showBearbeitenPanel(p.getBeschreibung());
        });
        button.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if( e.getKeyCode() == KeyEvent.VK_ENTER)
                    button.doClick();
                if( e.getKeyCode() == KeyEvent.VK_DOWN)
                    SwingUtilities.invokeLater(() -> speichern.requestFocusInWindow());
                if( e.getKeyCode() == KeyEvent.VK_ESCAPE)
                    getBack().doClick();
            }
        });
        return button;
    }

    private JButton getSpeichernButton() {
        JButton button = new JButton("Speichern");
        addBackFuctionality(button);
        button.addActionListener((e) -> {
            if(!form.isRegExApproved()){
                sendNameError();
                return;
            }
            Person p = createNewPerson();
            getPM().save(p, null);
            getFrame().addLetzteBearbeitet(p.getBeschreibung());
            getFrame().showMenuPanel();
        });
        button.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if( e.getKeyCode() == KeyEvent.VK_ENTER)
                    button.doClick();
                if( e.getKeyCode() == KeyEvent.VK_UP)
                    SwingUtilities.invokeLater(() -> bearbeiten.requestFocusInWindow());
            }
        });
        return button;
    }

    /** Only call if name is approved, since it claims a new ID */
    private Person createNewPerson() {
        String vor = form.getVorname();
        String nach = form.getNachname();
        return new Person(vor, nach, getPM().getNewID());
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
        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(0,70));
        panel.setBorder(new MatteBorder(0,1,0,0, Color.BLACK));

        back = createBackButton();
        panel.add(back);
        return panel;
    }

    private JButton createBackButton() {
        JButton button = new JButton("Zurück");
        button.setBounds(13,20,70,30);
        button.setMargin(new Insets(0,0,0,0));
        button.addActionListener((e) -> {
                getFrame().showMenuPanel();
        });
        button.setFocusable(false);
        return button;
    }

    @Override
    public void onFormFinish() {
        SwingUtilities.invokeLater(() -> bearbeiten.requestFocusInWindow());
        // TODO send on focus
    }

    private void sendNameError() {
    }

    protected JButton getBack() {
        return back;
    }

    private void addBackFuctionality(JComponent comp) {
        comp.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if( e.getKeyCode() == KeyEvent.VK_ESCAPE)
                    getBack().doClick();
            }
        });
    }
}
