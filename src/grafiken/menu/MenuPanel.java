package grafiken.menu;

import grafiken.MainFrame;
import grafiken.OuterJPanel;
import helpers.Profilliste;
import helpers.SaveAssistant;
import users.Personenbeschreibung;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.util.List;

public class MenuPanel extends OuterJPanel implements PersonenWahl{

    private int seite = 1;
    private PageCoordination pageCoordination;

    private boolean filtered = false;
    private Profilliste.Sortierung  sortierung = Profilliste.Sortierung.ABC;
    private JComponent personParent;
    private JLabel seitenAnzeige;

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

    private JComponent getNorth() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(0,55));
        panel.setBackground(Color.DARK_GRAY);
        panel.setLayout(new BorderLayout());

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

    private JComponent getWest() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(220,0));
        panel.setBackground(Color.LIGHT_GRAY);
        panel.setLayout(new BorderLayout());
        panel.setBorder(new MatteBorder(0,0,0,1, Color.BLACK));

        panel.add(getLetzteLabel(), BorderLayout.NORTH);
        panel.add(getLetzteField(), BorderLayout.CENTER);
        return panel;
    }

    private JComponent getLetzteField() {
        // TODO
        return new JPanel();
    }


    private JPanel getLetzteLabel() {
        JLabel label = new JLabel("Letzte:");
        JPanel inner = new JPanel();
        inner.add(label);
        label.setHorizontalTextPosition(SwingConstants.CENTER);
        return inner;
    }

    private JComponent getCenter() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

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

        JPanel switchPanel = getSwitchPanel();
        setzeSeite(seite);

        panel.add(switchPanel, BorderLayout.SOUTH);
        return panel;
    }


    private JPanel getSwitchPanel() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(70,50));
        panel.setBorder(new MatteBorder(1,0,0,0,Color.BLACK));

        JButton last = new JButton("last");
        last.addActionListener((e) -> setToPrev());
        JButton next = new JButton("next");
        next.addActionListener((e) -> setToNext());
        seitenAnzeige  = new JLabel();
        aktualisiereSeitenanzeige();

        panel.add(last);
        panel.add(seitenAnzeige);
        panel.add(next);
        return panel;
    }


    private JPanel getSettingsPanel() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(70,90));
        panel.setBorder(new MatteBorder(1,0,0,0,Color.BLACK));
        panel.setLayout(new BorderLayout());

        panel.add(getSettingButtons(), BorderLayout.EAST);
        return panel;
    }

    private Component getSettingButtons() {
        JPanel outer = new JPanel();
        outer.setLayout(new BoxLayout(outer, BoxLayout.PAGE_AXIS));
        outer.setPreferredSize(new Dimension(130, 130));

        JPanel filtering = new JPanel();
        JLabel filter = new JLabel("Filter:");
        filtering.add(filter);
        filtering.add(getFilterButton());

        JPanel sorting = new JPanel();
        JLabel sort = new JLabel("Nach:");
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

    @Override
    public void waehle(Personenbeschreibung pb) {
        getFrame().showBearbeitenPanel(pb);
    }

    private JPanel getSorryPanel() {
        JPanel panel = new JPanel();
        JLabel label = new JLabel("Keine Einträge :/");
        panel.add(label);
        return panel;
    }

    private void aktualisiereSeitenanzeige() {
        int seiten = (!filtered) ? pageCoordination.getSeitenAlle() : pageCoordination.getSeitenSchulden();
        if(seiten == 0)
            seitenAnzeige.setText("0/0");
        else {
            seitenAnzeige.setText(seite+"/"+seiten);
        }
    }


    // seite setzen


    public void setzeSeite(int seite) {
        setzeSeite(seite, sortierung);
    }

    private void setzeSeite(int seite, Profilliste.Sortierung sortierung) {
        int alle = (filtered) ? pageCoordination.getSeitenSchulden() : pageCoordination.getSeitenAlle();
        if(alle == 0) {
            ersetzeMitte(getSorryPanel());
        }
        else {
            if(seite > alle)
                seite = 1;
            this.seite = seite;
            ersetzeMitte(getSeite(seite, sortierung));
        }
        aktualisiereSeitenanzeige();
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


    // switch

    public void switchSortierung(Profilliste.Sortierung sortierung) {
        this.sortierung = sortierung;
        setzeSeite(1, sortierung);
    }

    public void switchStapel() {
        seite = 1;
        filtered = !filtered;
        setzeSeite(1);
    }



    // functionality


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



    private void loadBetraege(List<Personenbeschreibung> pbs, int seite, Profilliste.Sortierung sortierung) {
        for(Personenbeschreibung p : pbs) {
            if(getPM().getIDMap().containsKey(p.id)) continue;
            getPM().betragZuIDMap(p);
            ladeDieseUndUmliegende(pbs, seite, sortierung);
            return;
        }
    }

    private void ladeDieseUndUmliegende(List<Personenbeschreibung> pbs, int seite, Profilliste.Sortierung sortierung) {
        System.out.println("Here");
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
