package users;

/** Zum Listen der Personen mit wenig Speicher-Gebrauch */
public class Personenbeschreibung implements Comparable<Personenbeschreibung>{
    public final String name;
    public final int id;
    public final int kontostand;

    public Personenbeschreibung (String name, int id, int kontostand) {
        this.name = name;
        this.id = id;
        this.kontostand = kontostand;
    }

    @Override
    public boolean equals(Object o) {
        Personenbeschreibung p = (Personenbeschreibung) o;
        return compareTo(p) == 0;
    }

    @Override
    public int compareTo(Personenbeschreibung o) {
        if(this.name.compareTo(o.name) > 0) return 1;
        return ((Integer) id).compareTo(o.id);
    }
}