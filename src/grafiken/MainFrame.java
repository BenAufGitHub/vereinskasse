package grafiken;

import helpers.PersonenManager;
import users.Person;
import users.Personenbeschreibung;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayDeque;

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
        if(letzte.size() >= 5)
            letzte.removeLast();
        letzte.addFirst(pb);
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
        this.setTitle("Vereinskasse");
        // TODO set Icon
    }

}
