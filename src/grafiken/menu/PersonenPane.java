package grafiken.menu;

import grafiken.GeldFormat;
import helpers.Profilliste;
import helpers.SaveAssistant;
import users.Personenbeschreibung;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

public class PersonenPane extends JPanel {

    private PersonenWahl action;
    private static int rotGrenze = 1000;

    public PersonenPane(Profilliste.Sortierung sortierung, Profilliste pf, PersonenWahl action) {
        this.action = action;
        setLayout(new BorderLayout());
        JPanel panel = getPanel(sortierung, pf);
        add(panel, BorderLayout.CENTER);
    }


    private JPanel getPanel(Profilliste.Sortierung sortierung, Profilliste pf) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(8,1));
        for(int i=0; i<pf.size(); i++) {
            Personenbeschreibung pb = pf.getPB(i, sortierung);
            JPanel person = createPerson(pb);
            panel.add(person);
        }
        return panel;
    }

    private JPanel createPerson(Personenbeschreibung pb) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JPanel name = getNamenPanel(pb);
        JPanel betrag = getBetrag(pb, name);
        JPanel act = getActionButton(pb);

        panel.add(name, BorderLayout.WEST);
        panel.add(betrag, BorderLayout.CENTER);
        panel.add(act, BorderLayout.EAST);
        return panel;
    }


    private JPanel getBetrag(Personenbeschreibung pb, JPanel name) {
        int schulden = SaveAssistant.greifeSchuldenBetrag(pb);
        JLabel label = new JLabel(GeldFormat.geldToStr(schulden, true));
        if(schulden >= rotGrenze)
            label.setForeground(Color.RED);
        else if(schulden > 0)
            label.setForeground(Color.ORANGE);

        JPanel betragsPanel = new JPanel();
        betragsPanel.setLayout(new FlowLayout(FlowLayout.TRAILING, 9,20));

        betragsPanel.add(label);
        return betragsPanel;
    }

    private JPanel getActionButton(Personenbeschreibung pb) {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 15));
        panel.setPreferredSize(new Dimension(100,0));

        JButton button = new JButton("bearbeiten");
        button.addActionListener((e) -> action.waehle(pb));
        panel.add(button);
        return panel;
    }

    private JPanel getNamenPanel(Personenbeschreibung pb) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        JLabel label = new JLabel(pb.vorname +" "+pb.nachname);
        JLabel id = new JLabel("#"+pb.id);
        id.setFont(new Font("arial", Font.ITALIC, 11));
        panel.setPreferredSize(new Dimension(label.getPreferredSize().width + 10, 0));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(label);
        panel.add(id);

        Dimension outerDim = new Dimension(100,100);
        JPanel outer = new JPanel();
        outer.setPreferredSize(panel.getPreferredSize());
        // outer.setLayout(new FlowLayout(FlowLayout.CENTER, 10,2));
        panel.setBorder(new EmptyBorder(1,5,1,0));
        return panel;
    }
}
