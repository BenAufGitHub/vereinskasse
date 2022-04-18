package helpers;

import users.Person;
import users.Personenbeschreibung;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class PersonenManager {

    private Profilliste alle;
    private Profilliste negative;
    private Metadata data;
    private static String metafile = "metadata.json";


    public PersonenManager(){
        this.data = SaveAssistant.ladeObjekt(metafile, Metadata.class);
        // kein vorhandenes data file
        if(this.data == null)
            this.data = createDataSet();
        ArrayList<Personenbeschreibung> pbs = SaveAssistant.getPersonenbeschreibungen();
        initListen(pbs);
    }


    /**
     * Alle Fälle:
     * Person+PB !=null: Veränderungen werden aufgenommen und in DB eingetragen.
     * Person=null: Person wird aus DB entfernt,
     * PB=null: Person wird in DB aufgenommen,
     * Person+PB =null: nichts
     * @param person Die zu speichernde Person.
     * @param urspruenglich Das Profil der Person, welches vor den Veränderungen angelegt war.
     */
    public void save(Person person, Personenbeschreibung urspruenglich) {
        if(person == null && urspruenglich == null) return;
        if(person == null)
            removePerson(urspruenglich);
        else if(urspruenglich == null)
            createPerson(person);
        else
            changePerson(person, urspruenglich);
    }

    /** Return: Einzigartige id. */
    public int getNewID() {
        int id = ++(data.vergebeneIDs);
        data.save();
        return id;
    }

    /** Liste aller Profile */
    public Profilliste getAlleProfile(){
        return alle;
    }

    /** Liste aller Personen mit Restschulden */
    public Profilliste getSchuldhafteProfile() {
        return negative;
    }


    // ============================== Private Methoden ============================


    private void removePerson(Personenbeschreibung pb) {
        alle.delete(pb);
        negative.delete(pb);
        data.schuldhafteIDs.remove(pb.id);
        data.totalUsers--;
        SaveAssistant.loeschePerson(pb);
        data.save();

    }

    private void createPerson(Person person) {
        SaveAssistant.speicherePerson(person);
        alle.insert(person.getBeschreibung());
        data.totalUsers++;
    }

    private void changePerson(Person person, Personenbeschreibung zuletztPB) {
        Personenbeschreibung aktuell = aktualisiereBeschreibung(person, zuletztPB);
        SaveAssistant.speicherePerson(person);
        if(person.istSchuldenfrei())
            data.schuldhafteIDs.remove(aktuell.id);
        else
            data.schuldhafteIDs.add(aktuell.id);
    }

    private Personenbeschreibung aktualisiereBeschreibung(Person person, Personenbeschreibung zuletztPB) {
        Personenbeschreibung neu = person.getBeschreibung();
        if(neu.compareTo(zuletztPB) == 0) return zuletztPB;
        alle.delete(zuletztPB);
        negative.delete(zuletztPB);
        SaveAssistant.bennenePersonUm(neu, zuletztPB);
        return neu;
    }

    private Metadata createDataSet() {
        data = new Metadata();
        data.evalEverything();
        data.save();
        return data;
    }

    private void initListen(ArrayList<Personenbeschreibung> pbs) {
        this.alle = new Profilliste(pbs);
        pbs.removeIf((element) -> {
            return data.schuldhafteIDs.contains(element.id);
        });
        this.negative = new Profilliste(pbs);
    }


    // ============================ Metadata ======================================


    private class Metadata {

        private HashSet<Integer> schuldhafteIDs;
        private int vergebeneIDs;
        private int totalUsers;

        public Metadata() {
            this.schuldhafteIDs = new HashSet<>();
            this.vergebeneIDs = 0;
            this.totalUsers = 0;
        }


        /** Scans all data, should not be used frequently. */
        private void evalEverything() {
            List<Personenbeschreibung> personen = SaveAssistant.getPersonenbeschreibungen();
            this.totalUsers = personen.size();
            int maxID = 0;
            for(Personenbeschreibung pb : personen) {
                int schulden = SaveAssistant.greifeSchuldenBetrag(pb);
                maxID = Math.max(maxID, pb.id);
                if(schulden <= 0) continue;
                schuldhafteIDs.add(pb.id);
            }
            vergebeneIDs = maxID;
        }

        public void save() {
            SaveAssistant.speichereObjekt(this, metafile);
        }

    }

}
