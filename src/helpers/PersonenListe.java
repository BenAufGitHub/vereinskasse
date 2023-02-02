package helpers;

import users.Personenbeschreibung;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;


/**
 * Lokale Synchronisation mit DB bzw. die cached Version (mit voller sync. nach 'minutesUntilFullSync' Minuten).
 */
public class PersonenListe {


    private ArrayList<Personenbeschreibung> data = null;
    private ArrayList<Integer> abcSort = null;
    private LocalDateTime lastRefresh = null;

    private final int minutesUntilFullSync = 10;


    // ------------- Timing ------------------->


    private boolean shouldRefresh () {
        if(lastRefresh == null) return true;
        return LocalDateTime.now().minusMinutes(minutesUntilFullSync).compareTo(lastRefresh) >= 0;
    }

    private void registerRefresh () {
        lastRefresh = LocalDateTime.now();
    }


    // ------------ Sortierung ---------------->


    private Sortierung sort = Sortierung.ID;


    public enum Sortierung {
        ABC, ID
    }


    public void sortiere(Sortierung sort) throws SQLException {
        if(this.sort == sort) return;
        this.sort = sort;
        if(sort == Sortierung.ID)
            sortiereDataNachID();
        else
            sortiereDataAlphabetisch();
        return;
    }


    public Sortierung getSortierung() {
        return sort;
    }


    /** @return ArrayList<Integer> from query with sorted ids ordered alphabetically. */
    private ArrayList<Integer> getAbcSort (boolean neuLaden) throws SQLException {
        if(abcSort != null && !neuLaden) return abcSort;
        abcSort = new ArrayList<>();

        String qry = QueryStrings.GET_ABC_SORTED_IDS;
        ResultSet rs = Query.getSet(qry);
        while(rs.next()) {
            abcSort.add(rs.getInt("user_id"));
        }
        return abcSort;
    }


    // ------------ laden ------------------------>


    public ArrayList<Personenbeschreibung> getOld() {
        return data;
    }

    public ArrayList<Personenbeschreibung> ladePersonenbeschreibungen() throws SQLException {
        if(data!=null && !shouldRefresh()) return data;
        data = fetchPersonenbeschreibungen();
        this.sort = Sortierung.ID;
        return data;
    }


    private ArrayList<Personenbeschreibung> fetchUndSortBerschreibung() throws SQLException {
        data = fetchPersonenbeschreibungen();
        registerRefresh();
        if(sort==Sortierung.ID)
            return data;
        sortiereDataAlphabetisch();
        return data;
    }


    public ArrayList<Personenbeschreibung> ladeMitUpdate(int id) throws SQLException {
        if(shouldRefresh())
            return fetchPersonenbeschreibungen();
        return updateList(id);
    }


    private ArrayList<Personenbeschreibung> updateList(int id) throws SQLException {
        loesche(id);

        String pbQry = QueryStrings.getProfil(id);
        ResultSet rs = Query.getSet(pbQry);
        rs.next();

        Personenbeschreibung pb = new Personenbeschreibung(rs.getString("user_name"), id,  rs.getInt("kontostand"));
        einsortieren(pb);
        return data;
    }


    public void loesche(int id) {
        data.removeIf(content -> content.id == id);
    }


    private ArrayList<Personenbeschreibung> fetchPersonenbeschreibungen() throws SQLException {
        String qry = QueryStrings.GET_PROFILE;
        ArrayList<Personenbeschreibung> temp_pbs = new ArrayList<>();

        ResultSet rs = Query.getSet(qry);
        while(rs.next()) {
            int id = rs.getInt("user_id");
            String name = rs.getString("user_name");
            int kontostand = rs.getInt("schulden inkl. gutschrift");
            temp_pbs.add(new Personenbeschreibung(name, id, kontostand));
        }
        return temp_pbs;
    }


    // ------------------- sortieren -------------------------->


    private void einsortieren(Personenbeschreibung pb) throws SQLException {
        if(getSortierung() == Sortierung.ID) {
            einsortierenNachID(pb);
        } else {
            einsortierenNachABC(pb);
        }
    }


    private void einsortierenNachID(Personenbeschreibung pb) {
        int i=0;
        while(i< data.size()) {
            if(data.get(i).id < pb.id) continue;
            data.add(i, pb);
            return;
        }
        data.add(pb);
    }


    private void einsortierenNachABC(Personenbeschreibung pb) throws SQLException {
        List<Integer> sort = getAbcSort(true);
        int i=0;
        while(i<data.size()) {
            if(sort.indexOf(data.get(i).id) < sort.indexOf(pb.id)) continue;
            data.add(i, pb);
            return;
        }
        data.add(pb);
    }


    private void sortiereDataAlphabetisch() throws SQLException {
        int size = getAbcSort(true).size();
        for(int i=0; i<size; i++) {
            int id = abcSort.get(i);
            int idx = findID(id);
            if(idx==-1) continue;
            Personenbeschreibung pb = data.get(idx);
            data.remove(idx);
            data.add(i, pb);
        }
    }


    private int findID(int id) {
        for (int i=0; i<data.size(); i++) {
            if(data.get(i).id==id)
                return i;
        }
        return -1;
    }


    private void sortiereDataNachID() {
        Collections.sort(data, (Personenbeschreibung content1, Personenbeschreibung content2) ->  Integer.compare(content1.id, content2.id));
    }
}
