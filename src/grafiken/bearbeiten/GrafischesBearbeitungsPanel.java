package grafiken.bearbeiten;

import grafiken.MainFrame;
import grafiken.bearbeiten.FunktionalesBearbeitungsPanel;
import users.Personenbeschreibung;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;


public abstract class GrafischesBearbeitungsPanel extends FunktionalesBearbeitungsPanel {

    private JLabel gesammelt;
    private JTextArea naechste;
    private JTextArea gesamt;
    private JTextField auffuellenText;
    private JButton fuellenButton;
    private JButton ergaenzenButton;

    private JButton switchPanel;
    private JButton add;
    private JButton save;

    private JButton back;
    private JButton delete;
    private JTextField vorname;
    private JTextField nachname;
    private JButton edit;
    private JButton reset;
    private JTextArea info;

    private JPanel east;

    public GrafischesBearbeitungsPanel(MainFrame parent, Personenbeschreibung pb) {
        super(parent, pb);
        this.setLayout(new BorderLayout());
        JPanel center = getCenterPanel();
        east = getEastPanel();

        add(center, BorderLayout.CENTER);
        add(east, BorderLayout.EAST);
    }



    // ============= Center >

    private JPanel getCenterPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.DARK_GRAY);
        panel.setOpaque(false);

        JPanel north = getNorthBack(Color.GRAY);
        JPanel center = getNameGrid();
        JPanel south = getDeletePanel();

        panel.add(center, BorderLayout.CENTER);
        panel.add(north, BorderLayout.NORTH);
        panel.add(south, BorderLayout.SOUTH);
        return panel;
    }

    // ================================ Back-Button >

    private JPanel getNorthBack(Color c) {
        JPanel panel = new JPanel();
        panel.setBackground(c);
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(80,40));
        panel.setOpaque(false);

        JPanel nw = getNW(Color.LIGHT_GRAY);
        panel.add(nw, BorderLayout.WEST);
        return panel;
    }

    private JPanel getNW(Color c) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(100,0));

        JButton back = getBackButton();
        panel.add(back);
        return panel;
    }

    private JButton getBackButton() {
        back = new JButton();
        back.setBounds(5,5,70,30);
        back.setMargin(new Insets(0,0,0,0));
        back.setFocusable(false);
        back.setBorder(new EmptyBorder(0,4,4,4));
        back.setText("Zurück");
        return back;
    }

    // <============================= Back Button

    // =============================> Name ändern

    private JPanel getNameGrid() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setOpaque(false);

        JLabel label = new JLabel();
        vorname = new JTextField();
        nachname = new JTextField();
        edit = new JButton("edit");
        reset = new JButton("reset");
        info = new JTextArea();

        editAddLabel(label, panel);
        editAddName(vorname, nachname, panel);
        editAddButton(edit, reset, panel);
        editAddInfo(info, panel);
        return panel;
    }

    private void editAddInfo(JTextArea info, JPanel panel) {
        info.setPreferredSize(new Dimension(200, 100));
        info.setForeground(Color.RED);
        info.setText("hier könnte Info stehen");
        info.setOpaque(false);
        info.setLineWrap(true);
        info.setWrapStyleWord(true);
        info.setEditable(false);

        GridBagConstraints c = getConstraints();
        c.gridheight = 2;
        c.gridy = 4;
        c.anchor = GridBagConstraints.NORTH;
        panel.add(info, c);
    }

    private void editAddButton(JButton back, JButton reset, JPanel panel) {
        JPanel outer = new JPanel();
        outer.setPreferredSize(new Dimension(200,20));
        outer.setLayout(new BorderLayout());
        outer.setOpaque(false);
        outer.add(back, BorderLayout.WEST);
        outer.add(reset, BorderLayout.EAST);

        GridBagConstraints c = getConstraints();
        c.gridy = 3;
        panel.add(outer, c);
    }

    private GridBagConstraints getConstraints() {
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 0.4;
        c.weighty = 0;
        c.gridwidth = 4;
        c.gridheight = 1;
        c.gridx = 0;
        c. insets = new Insets(5,5,5,0);
        return c;
    }
    private void editAddName(JTextField vor, JTextField nach, JPanel panel) {
        vor.setPreferredSize(new Dimension(200, 30));
        nach.setPreferredSize(new Dimension(200, 30));
        vor.setMinimumSize(new Dimension(100,30));
        nach.setMinimumSize(new Dimension(100,30));
        GridBagConstraints c = getConstraints();
        c.gridy = 1;
        panel.add(nach, c);
        c.insets = new Insets(30,5,5,0);
        c.gridy = 0;
        panel.add(vor, c);
    }

    private void editAddLabel(JLabel label, JPanel panel) {
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 0.4;
        c.weighty = 0.4;
        c.gridx = 4;
        c.gridy = 4;
        panel.add(label, c);
    }

    // <============================= Name ändern

    // =============================> Löschen

    private JPanel getDeletePanel() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(60,45));
        panel.setLayout(null);
        panel.setOpaque(false);

        delete = new JButton("löschen");
        delete.setBounds(5,5,60,30);
        delete.setMargin(new Insets(0,0,0,0));
        delete.setBackground(Color.RED);

        panel.add(delete, BorderLayout.WEST);
        return panel;
    }

    // <============================= Löschen

    // <========== Center

    // ===========> East

    private JPanel getEastPanel() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(750, 0));
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.GRAY);

        JPanel north = getInfoPanel();
        JPanel south = getUntereLeiste();

        panel.add(north, BorderLayout.NORTH);
        panel.add(south, BorderLayout.SOUTH);
        return panel;
    }

    // ==========================> NorthEast

    private JPanel getInfoPanel() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(0,180));
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.setBorder(new LineBorder(Color.BLACK, 1));
        panel.setOpaque(false);

        JPanel konto = getKonto();
        JPanel schuld = getSchuldPanel();
        JPanel gesamt = getGesamtPanel();

        panel.add(konto);
        panel.add(schuld);
        panel.add(gesamt);
        return panel;
    }

    private JPanel getKonto() {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(0,100));
        panel.setOpaque(false);

        gesammelt = new JLabel("Gesammelt: xx.xx€");
        gesammelt.setBounds(20,16,130,30);

        auffuellenText = new JTextField();
        auffuellenText.setBounds(220, 16, 70, 30);

        fuellenButton = new JButton("Auffüllen");
        fuellenButton.setBounds(300, 16, 70, 30);
        fuellenButton.setMargin(new Insets(0,0,0,0));

        panel.add(gesammelt);
        panel.add(auffuellenText);
        panel.add(fuellenButton);
        return panel;
    }

    private JPanel getSchuldPanel() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(0,100));
        panel.setLayout(null);
        panel.setOpaque(false);

        naechste = new JTextArea("Geld bis nächste \nAbzahlung: xx.xx€");
        naechste.setEditable(false);
        naechste.setBounds(20,15,130,50);
        naechste.setOpaque(false);
        naechste.setFont(new JLabel().getFont());

        ergaenzenButton = new JButton("ergänzen");
        ergaenzenButton.setBounds(300, 16, 70, 30);
        ergaenzenButton.setMargin(new Insets(0,0,0,0));

        panel.add(naechste);
        panel.add(ergaenzenButton);
        return panel;
    }

    private JPanel getGesamtPanel() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(0,100));
        panel.setLayout(null);
        panel.setOpaque(false);

        gesamt = new JTextArea("Gesamtschulden: xx.xx€");
        gesamt.setEditable(false);
        gesamt.setBounds(20,20,180,50);
        gesamt.setOpaque(false);
        gesamt.setFont(new JLabel().getFont());

        panel.add(gesamt);
        return panel;
    }

    // <========================== NorthEast

    // ==========================> SouthEast

    private JPanel getUntereLeiste() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(0, 80));
        panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
        panel.setOpaque(false);
        panel.setBorder(new LineBorder(Color.BLACK, 1));

        switchPanel = new JButton("Siehe Log");
        add = new JButton("+");
        save = new JButton("sichern & zurück");


        panel.add(Box.createRigidArea(new Dimension(30,1)));
        panel.add(switchPanel);
        panel.add(Box.createRigidArea(new Dimension(210,1)));
        panel.add(add);
        panel.add(Box.createRigidArea(new Dimension(185,1)));
        panel.add(save);
        return panel;
    }

    // <========================== SouthEast

    // ==========================> SouthMid

    public void switchPanels(JComponent component) {
        BorderLayout layout = (BorderLayout) east.getLayout();
        JComponent center = (JComponent) layout.getLayoutComponent(BorderLayout.CENTER);
        if(center != null)
            east.remove(center);
        east.add(component);
        east.revalidate();
        east.repaint();
    }

    // =========================== Getters

    protected JLabel getGesammelt() {
        return gesammelt;
    }
    protected JTextArea getNaechste() {
        return naechste;
    }
    protected JTextArea getGesamtSchulden() {
        return gesamt;
    }
    protected JTextField getAuffuellenFeld() {
        return auffuellenText;
    }
    protected JButton getFuellenButton() {
        return fuellenButton;
    }
    protected JButton getErgaenzenButton() {
        return  ergaenzenButton;
    }
    protected JButton getSwitchButton() {
        return switchPanel;
    }
    protected JButton getAdd() {
        return add;
    }
    protected JButton getSave() {
        return save;
    }
    protected JButton getBack() {
        return back;
    }
    protected JButton getDelete() {
        return delete;
    }
    protected JButton getEdit() {
        return edit;
    }
    protected JButton getReset() {
        return reset;
    }
    protected JTextField getVorname() {
        return vorname;
    }
    protected JTextField getNachname() {
        return nachname;
    }
    protected JTextArea getInfo() {
        return info;
    }
}
