package grafiken;

import grafiken.bearbeiten.FunktionalesBearbeitungsPanel;
import users.Personenbeschreibung;

import javax.swing.*;
import java.awt.*;

public class BearbeitungsPanel extends FunktionalesBearbeitungsPanel {

    public BearbeitungsPanel(MainFrame parent, Personenbeschreibung pb) {
        super(parent, pb);
        setBackground(Color.GREEN);
        initPanels();
    }


    // =============================== Overriding ===============================

    @Override
    protected void warneSpeichernFehlgeschlagen() {

    }

    @Override
    protected boolean validateNames() {
        return false;
    }

    @Override
    protected void saveNames() {

    }

    // =============================== Gestaltung ===============================

    private void initPanels() {
        this.setLayout(new BorderLayout());
        JPanel centerPanel = new JPanel();
        initCenterPanel(centerPanel);
        JPanel eastPanel = new JPanel();
        initEastPanel(eastPanel);
        add(centerPanel, BorderLayout.CENTER);
        add(eastPanel, BorderLayout.EAST);
    }

    private void initCenterPanel(JPanel centerPanel) {
        centerPanel.setLayout(new BorderLayout());
        centerPanel.setBackground(getFrame().getStandardColor());

        JPanel upperleft = getBackPanel();
        JPanel midLeft = getNamePanel();
        JPanel bottomLeft = getDeletePanel();

        centerPanel.add(upperleft, BorderLayout.NORTH);
        centerPanel.add(midLeft, BorderLayout.CENTER);
        centerPanel.add(bottomLeft, BorderLayout.SOUTH);
    }


    private JPanel getBackPanel() {
        JPanel panel = new JPanel();
        JPanel inner = new JPanel();
        JButton back = new JButton();

        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(0,50));
        panel.setBackground(getFrame().getStandardColor());

        inner.setPreferredSize(new Dimension(100,0));
        inner.setBackground(getFrame().getStandardColor());
        panel.add(inner, BorderLayout.WEST);

        back.setText("zurück");
        back.addActionListener((e) -> resetUndBack());
        back.setMargin(new Insets(0,0,0,0));
        back.setPreferredSize(new Dimension(80,40));
        back.setBackground(Color.GRAY);
        back.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        back.setFocusable(false);

        inner.add(back);
        return panel;
    }

    private JPanel getNamePanel() {
        JPanel panel = new JPanel();
        JTextField vorname = new JTextField();
        JTextField nachname = new JTextField();
        JButton edit = new JButton();
        JButton reset = new JButton();
        JTextArea infos = new JTextArea();
        return panel;
    }

    private JPanel getDeletePanel() {
        return new JPanel();
    }

    private void initEastPanel(JPanel eastPanel) {
        eastPanel.setBackground(Color.ORANGE);
        // eastPanel.setBackground(getFrame().getStandardColor());
    }
}
