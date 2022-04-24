package grafiken;

import grafiken.bearbeiten.BPanel;
import grafiken.menu.MenuPanel;
import helpers.PersonenManager;
import helpers.Profilliste;
import users.Person;
import users.Personenbeschreibung;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainFrame extends JFrame {
    private enum Panel {
          ERSTELLEN, BEARBEITEN, MENU
    }

    private PersonenManager pm;
    private Panel active;
    private ArrayDeque<Personenbeschreibung> letzte;
    private MenuPanel menu;

    private final int startingWidth = 1000;
    private final int startingHeight = 750;
    private final boolean startInCenter = true;
    private final int startLocationX = 0;
    private final int getStartLocationY = 0;
    private final Color backgroundColor = Color.decode("#063970");
    private final String title = "Vereinskasse";


    public MainFrame() {
        pm = new PersonenManager();
        letzte = new ArrayDeque<>();
        configureFrame();
        SwingUtilities.invokeLater(() -> {
            initMenu();
        });
    }


    // =============================== public access ========================


    public void showMenuPanel() {
        if(active == Panel.MENU) return;
        removeCenterPiece();
        menu.reload();
        addPanel(menu);
    }

    public void showErstellPanel() {
        if(active == Panel.ERSTELLEN) return;
        removeCenterPiece();
        // TODO

    }

    public void showBearbeitenPanel(Personenbeschreibung pb) {
        if(active == Panel.BEARBEITEN) return;
        removeCenterPiece();
        BPanel panel = new BPanel(this, pb);
        addPanel(panel);
    }

    public PersonenManager getPersonenManager() {
        return pm;
    }

    public void addLetzteBearbeitet(Personenbeschreibung pb) {
        if(letzte.contains(pb))
            letzte.remove(pb);
        if(letzte.size() >= 10)
            letzte.removeLast();
        letzte.addFirst(pb);
    }

    public List<Personenbeschreibung> getLetzteBearbeitet() {
        letzte.removeIf((element) -> {
            int pos = getPersonenManager().getAlleProfile().findePositionNach(element, Profilliste.Sortierung.ID);
            return pos==-1;
        });
        int size = letzte.size();
        Personenbeschreibung[] arr = letzte.toArray(new Personenbeschreibung[size]);
        return new ArrayList<>(Arrays.asList(arr));
    }

    public void loescheAusLetzteBearbeitet(Personenbeschreibung pb) {
        letzte.remove(pb);
    }

    public Color getStandardColor() {
        return getContentPane().getBackground();
    }


    // ================================ privat access ==========================


    private void configureFrame() {
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setResizable(true);
        this.setLocation(startLocationX, getStartLocationY);
        this.setSize(startingWidth, startingHeight);
        if(startInCenter)
            this.setLocationRelativeTo(null);
        this.getContentPane().setBackground(backgroundColor);
        this.setTitle(title);
        // TODO set Icon
    }

    private void initMenu() {
        menu = new MenuPanel(this);
        addPanel(menu);
    }

    private void addPanel(JPanel panel) {
        add(panel, BorderLayout.CENTER);
        this.revalidate();
        this.repaint();
    }

    private void removeCenterPiece() {
        BorderLayout layout = (BorderLayout) getContentPane().getLayout();
        JPanel centerPanel = (JPanel) layout.getLayoutComponent(BorderLayout.CENTER);
        if(centerPanel == null) return;
        getContentPane().remove(centerPanel);
        revalidate();
        repaint();
    }

}
