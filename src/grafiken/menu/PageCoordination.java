package grafiken.menu;

import helpers.PersonenListe;
import helpers.Profilliste;
import users.Personenbeschreibung;

import java.lang.reflect.Array;
import java.sql.SQLException;
import java.util.ArrayList;

class PageCoordination {

    private ArrayList<Personenbeschreibung> pListe;
    private ArrayList<Personenbeschreibung> pSchulden;
    private final int proSeite = 16;

    PageCoordination(ArrayList<Personenbeschreibung> pliste) {
        this.pListe = pliste;
        this.pSchulden = new ArrayList<>(pListe);
        this.pSchulden.removeIf(content -> content.kontostand <= 0);
    }

    public int getSeitenAlle() {
        if(pListe.size() == 0)
            return 0;
        return 1 + ((pListe.size()-1) / proSeite);
    }

    public int getSeitenSchulden() {
        if(pSchulden.size() == 0)
            return 0;
        return 1 + ((pSchulden.size()-1) / proSeite);
    }

    public ArrayList<Personenbeschreibung> getSeiteAlle(int seite) {
        if(seite <= 0 || seite > getSeitenAlle()) return null;
        ArrayList<Personenbeschreibung> liste = new ArrayList<>();
        for(int i=(seite-1)*16; i<pListe.size() && i <seite*16; i++){
            Personenbeschreibung pb = pListe.get(i);
            liste.add(pb);
        }
        return liste;
    }

    public ArrayList<Personenbeschreibung> getSeiteSchulden(int seite) {
        if(seite <= 0 || seite > getSeitenSchulden()) return null;
        ArrayList<Personenbeschreibung> liste = new ArrayList<>();
        for(int i=(seite-1)*16; i<pSchulden.size() && i <seite*16; i++){
            Personenbeschreibung pb = pSchulden.get(i);
            liste.add(pb);
        }
        return liste;
    }


}
