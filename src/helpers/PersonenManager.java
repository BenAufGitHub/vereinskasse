package helpers;

import users.Person;
import users.Personenbeschreibung;

public class PersonenManager {

    private Profilliste alle;
    private Profilliste negative;


    public PersonenManager(){
        // TODO anlegen einer config klasse und config file.
    }


    /**
     * Alle Fälle:
     * Person+PB != null: Veränderungen werden aufgenommen und in DB eingetragen.
     * Person = null: Person wird aus DB entfernt,
     * PB = null: Person wird in DB aufgenommen,
     * Person+PB = null: nichts
     * @param person Die zu speichernde Person.
     * @param urspruenglich Das Profil der Person, welches vor den Veränderungen angelegt war.
     */
    public void save(Person person, Personenbeschreibung urspruenglich) {
        // TODO
    }

    public int getNewID() {
        // TODO
        return 0;
    }

    public Profilliste getAlleProfile(){
        return alle;
    }

    public Profilliste getSchuldhafteProfile() {
        return negative;
    }

}
