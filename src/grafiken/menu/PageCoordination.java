package grafiken.menu;

import helpers.Profilliste;
import users.Personenbeschreibung;

import java.lang.reflect.Array;
import java.util.ArrayList;

class PageCoordination {

    private Profilliste alle;
    private Profilliste schulden;
    private final int proSeite = 16;

    PageCoordination(Profilliste alle, Profilliste schulden) {
        this.alle = alle;
        this.schulden = schulden;
    }

    public int getSeitenAlle() {
        if(alle.size() == 0)
            return 0;
        return 1 + ((alle.size()-1) / proSeite);
    }

    public int getSeitenSchulden() {
        if(schulden.size() == 0)
            return 0;
        return 1 + ((schulden.size()-1) / proSeite);
    }

    public ArrayList<Personenbeschreibung> getSeiteAlle(int seite, Profilliste.Sortierung sort) {
        if(seite <= 0 || seite > getSeitenAlle()) return null;
        ArrayList<Personenbeschreibung> liste = new ArrayList<>();
        for(int i=(seite-1)*16; i<alle.size() && i <seite*16; i++){
            Personenbeschreibung pb = alle.getPB(i, sort);
            liste.add(pb);
        }
        return liste;
    }

    public ArrayList<Personenbeschreibung> getSeiteSchulden(int seite, Profilliste.Sortierung sort) {
        if(seite <= 0 || seite > getSeitenSchulden()) return null;
        ArrayList<Personenbeschreibung> liste = new ArrayList<>();
        for(int i=(seite-1)*16; i<schulden.size() && i <seite*16; i++){
            Personenbeschreibung pb = schulden.getPB(i, sort);
            liste.add(pb);
        }
        return liste;
    }


}
