package grafiken.bearbeiten;

import grafiken.GeldFormat;
import users.Verschuldung;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Insets;
import java.io.BufferedWriter;
import java.util.ArrayList;


public class SchuldenAnsicht extends JScrollPane{

    private static ImageIcon trash = null;
    private ArrayList<SchuldPanel> array = new ArrayList<>();

    public ArrayList<SchuldPanel> getPanels() { return array; }

    private static ImageIcon getTrash () {
        if(trash != null) return trash;
        ImageIcon icon = new ImageIcon("resources/images/redcross.png");
        Image img = icon.getImage().getScaledInstance(13,13, java.awt.Image.SCALE_SMOOTH);
        trash = new ImageIcon(img);
        return trash;
    }

    private SchuldenAnsicht() {
        super(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    }


    public static SchuldenAnsicht createAnsicht(ArrayList<Verschuldung> arr, int breite) {
        JPanel container = new JPanel();
        container.setBackground(Color.GRAY);
        container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));
        SchuldenAnsicht scroll = new SchuldenAnsicht();
        if(!arr.isEmpty())
            addChildren(arr, breite, container, scroll);
        else
            addEmpty(container, scroll);
        scroll.getViewport().add(container);
        scroll.setBorder(null);
        return scroll;
    }

    private static void addEmpty(JPanel container, JScrollPane pane) {
        JLabel label = new JLabel();
        label.setText("Es ist still, zu still...");
        label.setHorizontalAlignment(SwingConstants.CENTER);

        container.setBackground(Color.lightGray);
        container.setLayout(new BorderLayout());
        container.add(label, BorderLayout.CENTER);
    }


    private static void addChildren(ArrayList<Verschuldung> arr, int breite, JPanel container, SchuldenAnsicht ansicht) {
        int height = 0;
        JPanel legende = getLegende(breite);
        height += 5 + legende.getPreferredSize().height;
        container.add(getSpace());
        container.add(legende);

        for(int i=0; i<arr.size(); i++) {
            Verschuldung v = arr.get(i);
            SchuldPanel panel = new SchuldPanel(v, breite);
            ansicht.array.add(panel);
            height += panel.getPreferredSize().getHeight()+5;
            container.add(panel);
            if(i+1<arr.size() && arr.size() >= 8){
                container.add(getSpace());
            }
        }
        container.setSize(breite, height);
    }

    private static Component getSpace() {
        Component c = Box.createRigidArea(new Dimension(5,5));
        c.setBackground(Color.RED);
        c.setForeground(Color.RED);
        return c;
    }


    private static JPanel getLegende(int breite) {
        JLabel label = new JLabel();
        label.setText("Verwerfen - Grund - Betrag - Seit x Tagen - Zinssatz - Kosten");
        if(breite > 400)
            label.setText("Verwerfen   -    Grund       -       Betrag    -    Seit x Tagen    -    Zinssatz      -     Kosten");
        JPanel panel = new JPanel();
        panel.setBackground(Color.LIGHT_GRAY);
        panel.setSize(breite, 23);
        label.setHorizontalTextPosition(SwingConstants.LEADING);
        panel.add(label);
        return panel;
    }


    public static class SchuldPanel extends JPanel{
        private Verschuldung schuld;
        private int breite;
        private int height;
        private int currentX = 0;
        private JButton button;

        public SchuldPanel(Verschuldung schuld, int breite){
            this.schuld = schuld;
            this.breite = breite;
            this.setLayout(null);
            this.setBackground(Color.lightGray);

            JTextArea grund = getGrund();
            JPanel button = getButton();
            JLabel betrag = getBetrag();
            JLabel seit = getSeit();
            JLabel zins = getZins();
            JLabel kosten = getKosten();

            setSize(breite, height);
            setPreferredSize(new Dimension(breite, height));
            int currX = 0;
            button.setBounds(0,0, breite / 6, height);
            currX += breite / 6;
            grund.setBounds(currX,0,breite / 4, height);
            currX += breite / 4;
            betrag.setBounds(currX,0,breite / 6, height);
            currX += breite / 6;
            seit.setBounds(currX, 0, breite / 10, height);
            currX += breite / 10;
            zins.setBounds(currX, 0, (breite) / 10, height);
            currX += (breite) / 10;
            kosten.setBounds(currX,0,breite / 8, height);

            addR(grund).addR(button).addR(betrag).addR(seit).addR(zins).addR(kosten);
        }

        private SchuldPanel addR(Component c) {
            add(c);
            return this;
        }

        private JLabel getKosten() {
            JLabel kosten = new JLabel();
            kosten.setOpaque(false);
            String geld = GeldFormat.geldToStr(schuld.berechneZuBezahlen(), false);
            kosten.setText(geld);
            return kosten;
        }

        private JLabel getZins() {
            JLabel zins = new JLabel();
            zins.setOpaque(false);
            zins.setText(schuld.getZinssatz() + "");
            return zins;
        }


        private JLabel getSeit() {
            JLabel label = new JLabel(schuld.getTageVerstrichen() + "");
            label.setOpaque(false);
            return label;
        }

        private JLabel getBetrag() {
            JLabel label = new JLabel();
            String geld = GeldFormat.geldToStr(schuld.getBetrag(), false);
            label.setText(geld);
            label.setOpaque(false);
            return label;
        }

        private JTextArea getGrund() {
            String grund = schuld.getGrund();
            JTextArea area = new JTextArea();
            area.setLineWrap(true);
            area.setWrapStyleWord(true);
            area.setText(grund);
            area.setOpaque(false);
            area.setEnabled(false);
            area.setDisabledTextColor(Color.BLACK);

            int width = breite / 6;
            area.setSize(width, 10);
            height = area.getPreferredScrollableViewportSize().height;
            if(height > 60)
                height = (int) (height * 0.75);
            return area;
        }

        private JPanel getButton() {
            JPanel outer = new JPanel();
            outer.setLayout(null);
            outer.setOpaque(false);

            button = new JButton();
            button.setFocusable(false);
            button.setMargin(new Insets(0,0,0,0));
            button.setBorder(null);
            button.setToolTipText("Verwerfen");
            int buttonX = (breite / 12) -10;
            int buttonY = (height / 2) -11;
            button.setBounds(buttonX, buttonY, 22,22);
            button.setContentAreaFilled(false);
            button.setIcon(getTrash());
            outer.add(button);
            return outer;
        }


        public Verschuldung getVerschuldung() {
            return schuld;
        }
        public JButton getTrashButton() { return button; }
    }
}
