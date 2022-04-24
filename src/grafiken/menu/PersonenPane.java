package grafiken.menu;

import grafiken.GeldFormat;
import helpers.Profilliste;
import helpers.SaveAssistant;
import users.Personenbeschreibung;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PersonenPane extends JPanel {

    private PersonenWahl action;
    private static int rotGrenze = 1000;

    private PersonenPane(List<Personenbeschreibung> pbs, HashMap<Integer, Integer> betraege, PersonenWahl action) {
        this.action = action;
        setLayout(new BorderLayout());
        JPanel panel = getPanel(pbs, betraege);
        add(panel, BorderLayout.CENTER);
    }


    private JPanel getPanel(List<Personenbeschreibung> pbs, HashMap<Integer, Integer> betraege) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(8,1));
        for(int i=0; i< pbs.size(); i++) {
            Personenbeschreibung pb = pbs.get(i);
            JPanel person = createPerson(pb, betraege.get(pb.id));
            panel.add(person);
        }
        return panel;
    }

    private JPanel createPerson(Personenbeschreibung pb, int betrag) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JPanel name = getNamenPanel(pb);
        JPanel betragPanel = getBetrag(betrag, name);
        JPanel act = getActionButton(pb);

        panel.add(name, BorderLayout.WEST);
        panel.add(betragPanel, BorderLayout.CENTER);
        panel.add(act, BorderLayout.EAST);
        return panel;
    }


    private JPanel getBetrag(int schulden, JPanel name) {
        JLabel label = new JLabel(GeldFormat.geldToStr(schulden, true));
        if(schulden >= rotGrenze)
            label.setForeground(Color.RED);
        else if(schulden > 0)
            label.setForeground(Color.ORANGE);
        if(schulden < 0)
            label.setForeground(Color.decode("#32CD32"));

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
    
    
    static public JComponent create(List<Personenbeschreibung> pbs, HashMap<Integer, Integer> betraege, PersonenWahl action) {
        if(pbs.size() <= 8)
            return new PersonenPane(pbs, betraege, action);
        return getPane(pbs, betraege, action);
    }

    private static JSplitPane getPane(List<Personenbeschreibung> pbs, HashMap<Integer, Integer> betraege, PersonenWahl action) {
        JSplitPane pane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        int mid = (1+pbs.size())/2;
        List<Personenbeschreibung> half = pbs.subList(0,mid);
        List<Personenbeschreibung> last = pbs.subList(mid,pbs.size());
        PersonenPane left = new PersonenPane(half, betraege, action);
        PersonenPane right = new PersonenPane(last, betraege, action);

        pane.setDividerSize(6);
        SwingUtilities.invokeLater(() -> pane.setResizeWeight(0.5));
        pane.add(left);
        pane.add(right);
        return pane;
    }
}
