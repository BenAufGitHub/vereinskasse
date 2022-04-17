package users;

/** Zum Listen der Personen mit wenig Speicher-Gebrauch */
public class Personenbeschreibung implements Comparable<Personenbeschreibung>{
    public final String vorname;
    public final String nachname;
    public final int id;

    public Personenbeschreibung (String vorname, String nachname, int id) {
        this.vorname = vorname;
        this.nachname = nachname;
        this.id = id;
    }

    @Override
    public int compareTo(Personenbeschreibung o) {
        if(this.vorname.compareTo(o.vorname) == 1) return 1;
        if(this.vorname.compareTo(o.vorname) == -1) return -1;
        if(this.nachname.compareTo(o.nachname) == 1) return 1;
        if(this.nachname.compareTo(o.nachname) == -1) return -1;
        return (this.id < o.id) ? -1 : 1;
    }
}