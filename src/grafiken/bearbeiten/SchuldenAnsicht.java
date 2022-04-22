package grafiken.bearbeiten;

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
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Insets;
import java.io.BufferedWriter;
import java.util.ArrayList;


public class SchuldenAnsicht {


    public static JScrollPane createAnsicht(ArrayList<Verschuldung> arr, int breite) {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));
        addChildren(arr, breite, container);
        JScrollPane scroll = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getViewport().add(container);
        return scroll;
    }


    private static void addChildren(ArrayList<Verschuldung> arr, int breite, JPanel container) {
        int height = 0;
        JPanel legende = getLegende(breite);
        height += 5 + legende.getPreferredSize().height;
        container.add(Box.createRigidArea(new Dimension(5,5)));
        container.add(legende);

        for(Verschuldung v : arr) {
            container.add(Box.createRigidArea(new Dimension(5,5)));
            JPanel panel = new SchuldPanel(v, breite);
            height += panel.getPreferredSize().getHeight()+5;
            container.add(panel);
        }
        container.setSize(breite, height);
    }


    private static JPanel getLegende(int breite) {
        System.out.println(breite);
        JLabel label = new JLabel();
        label.setText("Verwerfen - Grund - Betrag - Seit x Tagen - Zinssatz - Kosten");
        if(breite > 400)
            label.setText("Verwerfen   -    Grund       -       Betrag    -    Seit x Tagen    -    Zinssatz      -     Kosten");
        JPanel panel = new JPanel();
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

            JTextArea grund = getGrund();
            JButton button = getButton();
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
            String geld = geldToStr(schuld.berechneZuBezahlen(), false);
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
            String geld = geldToStr(schuld.getBetrag(), false);
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

        private JButton getButton() {
            button = new JButton();
            button.setFocusable(false);
            button.setMargin(new Insets(0,0,0,0));
            button.setBorder(null);
            button.setContentAreaFilled(false);
            ImageIcon icon = new ImageIcon("resources/images/eimer.png");
            Image img = icon.getImage().getScaledInstance(20,20, java.awt.Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(img));
            return button;
        }


        public Verschuldung getVerschuldung() {
            return schuld;
        }
    }


    private static String geldToStr(int betrag, boolean mitEuro) {
        int euros = Math.abs(betrag / 100);
        int cents = Math.abs(betrag % 100);
        String c = (cents >= 10) ? cents + "" : "0" + cents;
        String er = (betrag < 0) ?  "-" +euros : ""+euros;
        if(mitEuro)
            return er+","+c+"€";
        return er+","+c;
    }
}
