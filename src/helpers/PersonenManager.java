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


    public Person ladePerson(Personenbeschreibung pb) {
        return SaveAssistant.ladePerson(pb);
    }


    /**
     * Alle Faelle:
     * Person+PB !=null: Veraenderungen werden aufgenommen und in DB eingetragen.
     * Person=null: Person wird aus DB entfernt,
     * PB=null: Person wird in DB aufgenommen,
     * Person+PB =null: nichts
     * @param person Die zu speichernde Person.
     * @param urspruenglich Das Profil der Person, welches vor den Veraenderungen angelegt war.
     * @return true wenn erfolgreich, false bedeuted Name nicht korrekt formatiert.
     */
    public boolean save(Person person, Personenbeschreibung urspruenglich) {
        if(person == null && urspruenglich == null) return false;
        if(person == null)
            return removePerson(urspruenglich);
        else if(urspruenglich == null)
            return createPerson(person);
        else
            return changePerson(person, urspruenglich);
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


    private boolean removePerson(Personenbeschreibung pb) {
        alle.delete(pb);
        negative.delete(pb);
        data.schuldhafteIDs.remove(pb.id);
        data.totalUsers--;
        SaveAssistant.loeschePerson(pb);
        data.save();
        return true;
    }

    private boolean createPerson(Person person) {
        if(!SaveAssistant.istRichtigFormatiert(person.getBeschreibung()))
            return false;
        SaveAssistant.speicherePerson(person);
        alle.insert(person.getBeschreibung());
        data.totalUsers++;
        data.save();
        return true;
    }

    private boolean changePerson(Person person, Personenbeschreibung zuletztPB) {
        if(!SaveAssistant.istRichtigFormatiert(person.getBeschreibung()))
            return false;
        Personenbeschreibung aktuell = aktualisiereBeschreibung(person, zuletztPB);
        SaveAssistant.speicherePerson(person);
        if(person.istSchuldenfrei())
            registriereSchuldenfrei(aktuell);
        else
            registriereSchuldhaft(aktuell);
        data.save();
        return true;
    }

    private void registriereSchuldhaft(Personenbeschreibung pb) {
        data.schuldhafteIDs.add(pb.id);
        if(negative.findePositionNach(pb, Profilliste.Sortierung.ABC) == -1)
            negative.insert(pb);
    }

    private void registriereSchuldenfrei(Personenbeschreibung pb) {
        data.schuldhafteIDs.remove(pb.id);
        if(negative.findePositionNach(pb, Profilliste.Sortierung.ABC) != -1)
            negative.delete(pb);
    }

    private Personenbeschreibung aktualisiereBeschreibung(Person person, Personenbeschreibung zuletztPB) {
        Personenbeschreibung neu = person.getBeschreibung();
        if(neu.compareTo(zuletztPB) == 0) return zuletztPB;
        alle.delete(zuletztPB);
        alle.insert(neu);
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
            return !data.schuldhafteIDs.contains(element.id);
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
            this.schuldhafteIDs = new HashSet<>();
            this.totalUsers = personen.size();
            int maxID = 0;
            for(Personenbeschreibung pb : personen) {
                int schulden = SaveAssistant.greifeSchuldenBetrag(pb);
                maxID = Math.max(maxID, pb.id);
                if(schulden <= 0) continue;
                schuldhafteIDs.add(pb.id);
            }
            vergebeneIDs = maxID;
            save();
        }

        public void save() {
            SaveAssistant.speichereObjekt(this, metafile);
        }

    }

}
