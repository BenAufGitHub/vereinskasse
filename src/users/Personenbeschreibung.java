package users;

/** Zum Listen der Personen mit wenig Speicher-Gebrauch */
public class Personenbeschreibung {
    public final String vorname;
    public final String nachname;
    public final int id;

    public Personenbeschreibung (String vorname, String nachname, int id) {
        this.vorname = vorname;
        this.nachname = nachname;
        this.id = id;
    }
}