package grafiken.menu;

import grafiken.MainFrame;
import grafiken.OuterJPanel;
import helpers.Profilliste;
import helpers.SaveAssistant;
import users.Personenbeschreibung;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.util.List;

public class MenuPanel extends OuterJPanel implements PersonenWahl{

    private int seite = 1;
    private PageCoordination pageCoordination;
    private static ImageIcon jump = null;

    private boolean filtered = false;
    private Profilliste.Sortierung  sortierung = Profilliste.Sortierung.ABC;
    private JComponent personParent;
    private JLabel seitenAnzeige;
    private JPanel westPanel;
    private JTextPane factArea;



    public MenuPanel(MainFrame frame) {
        super(frame);
        configMenu();
        addBigPanel();
    }

    private void configMenu() {
        pageCoordination = new PageCoordination(getPM().getAlleProfile(), getPM().getSchuldhafteProfile());
        setLayout(new BorderLayout());
    }

    private void addBigPanel() {
        add(getNorth(), BorderLayout.NORTH);
        add(getWest(), BorderLayout.WEST);
        add(getCenter(), BorderLayout.CENTER);
    }


    // =================================== Public Access ==========================

    public void reload() {
        setzeSeite(seite);
        updateLetzte();
        reloadFacts();
    }

    @Override
    public void waehle(Personenbeschreibung pb) {
        getFrame().showBearbeitenPanel(pb);
    }

    // =================================== Obere Leiste ============================



    private JComponent getNorth() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(0,55));
        panel.setOpaque(false);
        panel.setLayout(new BorderLayout());
        panel.setBorder(new MatteBorder(0,0,1,0, Color.BLACK));

        panel.add(getAddButton(), BorderLayout.EAST);
        panel.add(getSuchen(), BorderLayout.CENTER);
        return panel;
    }

    private JPanel getSuchen() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new FlowLayout(FlowLayout.CENTER,10,13));
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(200,30));
        panel.setAlignmentY(Component.CENTER_ALIGNMENT);
        JButton button = new JButton("Suchen");
        panel.add(field);
        panel.add(button);
        return panel;
    }

    private JPanel getAddButton() {
        JPanel container = new JPanel();
        container.setOpaque(false);
        container.setLayout(null);
        container.setPreferredSize(new Dimension(60,50));
        JButton button = new JButton("+");
        button.setMargin(new Insets(0,0,0,0));
        button.setFocusable(false);
        button.setBounds(5,10,35,35);
        container.add(button);
        return container;
    }



    // ================================== WESTPANEL =================================



    private JComponent getWest() {
        JPanel panel = new JPanel();
        westPanel = panel;
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(160,0));
        panel.setLayout(new BorderLayout());
        panel.setBorder(new MatteBorder(0,0,0,1, Color.BLACK));

        panel.add(getLetzteLabel(), BorderLayout.NORTH);
        panel.add(getLetzteField(), BorderLayout.CENTER);
        return panel;
    }

    private JPanel getLetzteLabel() {
        JLabel label = new JLabel("Letzte:");
        label.setFont(new Font("arial", Font.ITALIC, 16));
        label.setForeground(Color.WHITE);
        JPanel inner = new JPanel();
        inner.setOpaque(false);
        inner.add(label);
        label.setHorizontalTextPosition(SwingConstants.CENTER);
        return inner;
    }

    private JComponent getLetzteField() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.DARK_GRAY);
        panel.setLayout(new GridLayout(8,1));
        placeLetzte(panel);
        return panel;
    }

    private void placeLetzte(JPanel panel) {
        List<Personenbeschreibung> letzte = getFrame().getLetzteBearbeitet();
        for(int i=0; i<8 && i < letzte.size(); i++) {
            Personenbeschreibung pb = letzte.get(i);
            panel.add(getNamenPanel(pb));
        }
        if(letzte.isEmpty())
            addEmptySchrift(panel);
    }

    private JPanel getNamenPanel(Personenbeschreibung pb) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BorderLayout());
        Border inner = new EmptyBorder(1,10,1,0);
        Border outer = new MatteBorder(1,0, 1,0, Color.GRAY);
        panel.setBorder(new CompoundBorder(outer, inner));

        panel.add(getPersonDescriptionPanel(pb), BorderLayout.CENTER);
        panel.add(getJumpButton(pb), BorderLayout.EAST);

        return panel;
    }

    private JPanel getPersonDescriptionPanel(Personenbeschreibung pb) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);

        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        JLabel label = new JLabel(pb.vorname +" "+pb.nachname);
        label.setForeground(Color.WHITE);
        JLabel id = new JLabel("#"+pb.id);
        id.setFont(new Font("arial", Font.ITALIC, 11));
        id.setForeground(Color.GRAY);

        panel.setPreferredSize(new Dimension(label.getPreferredSize().width + 10, 0));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(label);
        panel.add(id);

        return panel;
    }

    private JComponent getJumpButton(Personenbeschreibung pb) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(30,20));

        JButton button = new JButton();
        button.setPreferredSize(new Dimension(20,20));
        button.addActionListener((e) -> waehle(pb));

        button.setMargin(new Insets(0,0,0,0));
        button.setContentAreaFilled(false);
        button.setIcon(getJumpIcon());
        button.setToolTipText("bearbeiten");
        button.setBorder(null);
        panel.add(button);
        return panel;
    }


    private void updateLetzte() {
        BorderLayout layout = (BorderLayout) westPanel.getLayout();
        JComponent center = (JComponent) layout.getLayoutComponent(BorderLayout.CENTER);
        if(center != null){
            westPanel.remove(center);
            layout.removeLayoutComponent(center);
        }
        westPanel.add(getLetzteField(), BorderLayout.CENTER);
        westPanel.revalidate();
        westPanel.repaint();
    }

    private static ImageIcon getJumpIcon() {
        if(jump != null) return jump;
        ImageIcon icon = new ImageIcon("resources/images/goto.jpg");
        Image img = icon.getImage().getScaledInstance(20,20, java.awt.Image.SCALE_SMOOTH);
        jump = new ImageIcon(img);
        return jump;
    }



    // ========================== Rechte Seite ===========================



    private JComponent getCenter() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setOpaque(false);

        JPanel settingsPanel = getSettingsPanel();
        JPanel sides = getSidesPanels(seite, sortierung);

        panel.add(settingsPanel, BorderLayout.SOUTH);
        panel.add(sides, BorderLayout.CENTER);
        return panel;
    }

    private JPanel getSidesPanels(int seite, Profilliste.Sortierung sortierung) {
        JPanel panel = new JPanel();
        personParent = panel;
        panel.setLayout(new BorderLayout());
        panel.setOpaque(false);

        JPanel switchPanel = getSwitchPanel();
        setzeSeite(seite);

        panel.add(switchPanel, BorderLayout.SOUTH);
        return panel;
    }


    private JPanel getSwitchPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.DARK_GRAY);
        panel.setPreferredSize(new Dimension(70,50));
        panel.setBorder(new MatteBorder(1,0,0,0,Color.BLACK));
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 20,12));
        panel.setAlignmentY(Component.BOTTOM_ALIGNMENT);

        JButton last = new JButton("<=");
        last.addActionListener((e) -> setToPrev());
        JButton next = new JButton("=>");
        last.setFocusable(false);
        next.setFocusable(false);
        next.addActionListener((e) -> setToNext());
        seitenAnzeige = new JLabel();
        seitenAnzeige.setForeground(Color.WHITE);
        aktualisiereSeitenanzeige();

        panel.add(last);
        panel.add(seitenAnzeige);
        panel.add(next);
        return panel;
    }


    private JPanel getSettingsPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.DARK_GRAY);
        panel.setPreferredSize(new Dimension(70,90));
        panel.setLayout(new BorderLayout());

        panel.add(getSettingButtons(), BorderLayout.EAST);
        panel.add(getFactsPanel(panel), BorderLayout.CENTER);
        return panel;
    }

    private Component getSettingButtons() {
        JPanel outer = new JPanel();
        outer.setOpaque(false);
        outer.setLayout(new BoxLayout(outer, BoxLayout.PAGE_AXIS));
        outer.setPreferredSize(new Dimension(130, 130));

        JPanel filtering = new JPanel();
        filtering.setOpaque(false);
        JLabel filter = new JLabel("Filter:");
        filter.setForeground(Color.WHITE);
        filtering.add(filter);
        filtering.add(getFilterButton());

        JPanel sorting = new JPanel();
        sorting.setOpaque(false);
        JLabel sort = new JLabel("Nach:");
        sort.setForeground(Color.WHITE);
        sorting.add(sort);
        sorting.add(getSortierButton());

        outer.add(filtering);
        outer.add(sorting);
        return outer;
    }

    private Component getSortierButton() {
        JButton button = new JButton("ABC");
        button.setPreferredSize(new Dimension(50,28));
        button.setMargin(new Insets(0,0,0,0));
        button.setFocusable(false);
        button.addActionListener((e) -> {
            boolean b = sortierung == Profilliste.Sortierung.ABC;
            switchSortierung((!b) ? Profilliste.Sortierung.ABC : Profilliste.Sortierung.ID);
            if(!b)
                button.setText("ABC");
            else
                button.setText("ID");
        });
        return button;
    }

    private Component getFilterButton() {
        JButton switchFilter = new JButton("Alle");
        switchFilter.setPreferredSize(new Dimension(50,28));
        switchFilter.setMargin(new Insets(0,0,0,0));
        switchFilter.setFocusable(false);
        switchFilter.addActionListener((e) -> {
            String text = (!switchFilter.getText().equals("Alle")) ? "Alle" : "Minus";
            switchFilter.setText(text);
            switchStapel();
        });
        return switchFilter;
    }



    // ============================ extra Panels ========================


    private JPanel getSorryPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.GRAY);
        panel.setLayout(new BorderLayout());
        JLabel label = new JLabel("Keine Einträge :/ ");
        panel.add(label, BorderLayout.CENTER);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return panel;
    }

    private void addEmptySchrift(JPanel panel) {
        panel.add(new JLabel());
        panel.add(new JLabel());
        panel.add(new JLabel());
        JLabel label = new JLabel("Sehr ruhig hier oO");
        label.setForeground(Color.GRAY);
        label.setFont(new Font("Dialog",Font.ITALIC, 12));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(label);
    }

    private JPanel getFactsPanel(JPanel parent) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 0,10));

        factArea = new JTextPane();
        factArea.setOpaque(false);
        factArea.setForeground(Color.BLACK);
        centerFactArea(factArea);
        SwingUtilities.invokeLater(() -> sizeFactAreaAccordingly(factArea, parent));
        SwingUtilities.invokeLater(() -> reloadFacts());

        panel.add(factArea);
        return panel;
    }

    private void centerFactArea(JTextPane textPane) {
        StyledDocument doc = textPane.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
    }

    private void reloadFacts() {
        factArea.setText(new FactSpitter().spit());
    }

    private void sizeFactAreaAccordingly(JTextPane factArea, JPanel parent) {
        int midX = parent.getWidth()/2;
        int midY = parent.getHeight()/2;
        factArea.setPreferredSize(new Dimension(525,50));
        factArea.setMargin(new Insets(0,125,0,0));
    }


    // ================================= Seiten Laden ======================



    private void aktualisiereSeitenanzeige() {
        int seiten = (!filtered) ? pageCoordination.getSeitenAlle() : pageCoordination.getSeitenSchulden();
        if(seiten == 0)
            seitenAnzeige.setText("0/0");
        else {
            seitenAnzeige.setText(seite+"/"+seiten);
        }
    }


    // ================ Veränderungen ausgehend von diesen Methoden: ============

    public void setzeSeite(int seite) {
        setzeSeite(seite, sortierung);
    }

    public void switchSortierung(Profilliste.Sortierung sortierung) {
        this.sortierung = sortierung;
        setzeSeite(1, sortierung);
    }

    public void switchStapel() {
        seite = 1;
        filtered = !filtered;
        setzeSeite(1);
    }

    public void setToNext() {
        int seiten = (!filtered) ? pageCoordination.getSeitenAlle() : pageCoordination.getSeitenSchulden();
        if(seiten < 2) return;
        if(seiten == seite)
            seite = 0;
        setzeSeite(++seite);
    }

    public void setToPrev() {
        int seiten = (!filtered) ? pageCoordination.getSeitenAlle() : pageCoordination.getSeitenSchulden();
        if(seiten < 2) return;
        if(seite == 1)
            seite += seiten;
        setzeSeite(--seite);
    }



    // ============================ Implementierung Seiten verändern ================



    private void setzeSeite(int seite, Profilliste.Sortierung sortierung) {
        int alle = (filtered) ? pageCoordination.getSeitenSchulden() : pageCoordination.getSeitenAlle();
        if(alle == 0) {
            ersetzeMitte(getSorryPanel());
        }
        else {
            koordiniereSeite(alle, seite, sortierung);
        }
        aktualisiereSeitenanzeige();
    }

    private void koordiniereSeite(int gesamt, int seite, Profilliste.Sortierung sortierung) {
        if(seite > gesamt)
            seite = 1;
        this.seite = seite;
        JComponent c = getSeite(seite, sortierung);
        invokeSizing(c);
        ersetzeMitte(c);
    }


    private void invokeSizing(JComponent c) {
        if(!(c instanceof JSplitPane)) return;
        JSplitPane pane = (JSplitPane) c;
        SwingUtilities.invokeLater(() -> {
            pane.setOneTouchExpandable(true);
            pane.setResizeWeight(0.5);
            pane.setDividerLocation(personParent.getWidth()/2);
            if(personParent.getWidth() == 0)
                pane.setDividerLocation(383);
        });
    }

    private void ersetzeMitte(JComponent c) {
        BorderLayout layout = (BorderLayout) personParent.getLayout();
        JComponent center = (JComponent) layout.getLayoutComponent(BorderLayout.CENTER);
        if(center != null){
            personParent.remove(center);
            layout.removeLayoutComponent(center);
        }
        personParent.add(c);
        personParent.revalidate();
        personParent.repaint();
    }

    private JComponent getSeite(int seite, Profilliste.Sortierung sortierung) {
        List<Personenbeschreibung> pbs = (!filtered) ? pageCoordination.getSeiteAlle(seite, sortierung) :
                pageCoordination.getSeiteSchulden(seite, sortierung);
        loadBetraege(pbs, seite, sortierung);
        return PersonenPane.create(pbs, getPM().getIDMap(), this);
    }



    // =============================== lade Geldbetraege dynamisch ====================


    private void loadBetraege(List<Personenbeschreibung> pbs, int seite, Profilliste.Sortierung sortierung) {
        for(Personenbeschreibung p : pbs) {
            if(getPM().getIDMap().containsKey(p.id)) continue;
            getPM().betragZuIDMap(p);
            ladeDieseUndUmliegende(pbs, seite, sortierung);
            return;
        }
    }

    private void ladeDieseUndUmliegende(List<Personenbeschreibung> pbs, int seite, Profilliste.Sortierung sortierung) {
        for(Personenbeschreibung p : pbs) {
            getPM().betragZuIDMap(p);
        }
        if(!filtered)
            ladeInternalUngefiltert(pbs, seite, sortierung);
        else
            ladeInternalGefiltert(pbs, seite, sortierung);
    }

    private void ladeInternalGefiltert(List<Personenbeschreibung> pbs, int seite, Profilliste.Sortierung sortierung) {
        for(int i=-3; i<4; i++) {
            if (i==0) continue;
            if(seite+i <= 0 || seite+i > pageCoordination.getSeitenSchulden())
                continue;
            for(Personenbeschreibung p : pageCoordination.getSeiteSchulden(seite+i, sortierung))
                getPM().betragZuIDMap(p);
        }
    }

    private void ladeInternalUngefiltert(List<Personenbeschreibung> pbs, int seite, Profilliste.Sortierung sortierung) {
        for(int i=-3; i<4; i++) {
            if (i==0) continue;
            if(seite+i <= 0 || seite+i > pageCoordination.getSeitenAlle())
                continue;
            for(Personenbeschreibung p : pageCoordination.getSeiteAlle(seite+i, sortierung))
                getPM().betragZuIDMap(p);
        }
    }
}
