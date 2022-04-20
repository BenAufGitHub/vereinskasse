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
    public boolean equals(Object o) {
        Personenbeschreibung p = (Personenbeschreibung) o;
        return compareTo(p) == 0;
    }

    @Override
    public int compareTo(Personenbeschreibung o) {
        if(this.vorname.compareTo(o.vorname) > 0) return 1;
        if(this.vorname.compareTo(o.vorname) < 0) return -1;
        if(this.nachname.compareTo(o.nachname) > 0) return 1;
        if(this.nachname.compareTo(o.nachname) < 0) return -1;
        return ((Integer) id).compareTo(o.id);
    }
}