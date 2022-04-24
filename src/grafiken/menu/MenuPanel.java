package grafiken.menu;

import grafiken.MainFrame;
import grafiken.OuterJPanel;
import helpers.Profilliste;
import users.Personenbeschreibung;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;

public class MenuPanel extends OuterJPanel implements PersonenWahl{

    int seite = 1;

    public MenuPanel(MainFrame frame) {
        super(frame);
        configMenu();
        addBigPanel();
    }

    private void configMenu() {
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
        JPanel sides = getSidesPanels();

        panel.add(settingsPanel, BorderLayout.SOUTH);
        panel.add(sides, BorderLayout.CENTER);
        return panel;
    }

    private JPanel getSidesPanels() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JPanel switchPanel = getSwitchPanel();
        JPanel personenPanel = new PersonenPane(Profilliste.Sortierung.ABC, getPM().getAlleProfile(), this);
        JPanel p2 = new PersonenPane(Profilliste.Sortierung.ABC, getPM().getAlleProfile(), this);

        JSplitPane pane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        pane.setDividerSize(6);
        int min = pane.getMinimumDividerLocation();
        int max = pane.getMaximumDividerLocation();
        SwingUtilities.invokeLater(() -> pane.setResizeWeight(0.5));
        pane.add(personenPanel);
        pane.add(p2);

        panel.add(switchPanel, BorderLayout.SOUTH);
        panel.add(pane, BorderLayout.CENTER);
        return panel;
    }


    private JPanel getSwitchPanel() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(70,50));
        panel.setBorder(new MatteBorder(1,0,0,0,Color.BLACK));
        return panel;
    }

    private JPanel getSettingsPanel() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(70,90));
        panel.setBorder(new MatteBorder(1,0,0,0,Color.BLACK));
        return panel;
    }

    @Override
    public void waehle(Personenbeschreibung pb) {
        getFrame().showBearbeitenPanel(pb);
    }
}
