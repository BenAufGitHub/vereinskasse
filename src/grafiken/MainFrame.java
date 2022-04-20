package grafiken;

import helpers.PersonenManager;
import users.Person;
import users.Personenbeschreibung;

import javax.swing.JFrame;
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

    private final int startingWidth = 1000;
    private final int startingHeight = 750;
    private final boolean startInCenter = true;
    private final int startLocationX = 0;
    private final int getStartLocationY = 0;
    private final Color backgroundColor = Color.LIGHT_GRAY;
    private final String title = "Vereinskasse";


    public MainFrame() {
        pm = new PersonenManager();
        letzte = new ArrayDeque<>();
        configureFrame();
    }


    // =============================== public access ========================


    public void showMenuPanel() {
        if(active == Panel.MENU) return;
        // TODO

    }

    public void showErstellPanel() {
        if(active == Panel.ERSTELLEN) return;
        // TODO

    }

    public void showBearbeitenPanel(Personenbeschreibung pb) {
        if(active == Panel.BEARBEITEN) return;
        // TODO
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
        int size = letzte.size();
        Personenbeschreibung[] arr = letzte.toArray(new Personenbeschreibung[size]);
        return new ArrayList<>(Arrays.asList(arr));
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

}
