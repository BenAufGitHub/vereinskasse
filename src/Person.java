import java.util.ArrayList;

public class Person {

    private String vorname;
    private String nachname;

    private int beglicheneSchulden;
    private ArrayList<Verschuldung> schuldenLog = new ArrayList<>();

    public Person(String vorname, String nachname) {
        this.vorname = vorname;
        this.nachname = nachname;
    }

    public void addVerschuldung(String grund, int betrag) {
        schuldenLog.add(new Verschuldung(grund, betrag));
    }

    public void addBegleichung(int betrag) {
        beglicheneSchulden += betrag;
    }

    public int getRestSchulden(){
        int gesamtSchulden = 0;
        for(Verschuldung s : schuldenLog)
            gesamtSchulden += s.getBetrag();
        return gesamtSchulden - beglicheneSchulden;
    }

    public void begleicheAlleSchulden() {
        beglicheneSchulden += getRestSchulden();
    }

    public boolean istSchuldenfrei() {
        return getRestSchulden() <= 0;
    }

    public boolean hatNegativSchulden() {
        return getRestSchulden() < 0;
    }



    // ============================== Setter ====================================


    /**
     * TODO: Wrapper-Methode mit Speichern des neuen Namens (und löschen des alten).
     */
    private void setName(String vorname, String nachname) {
        this.vorname = vorname;
        this.nachname = nachname;
    }


    // =================================== getter ========================================

    public String getVorname(){
        return vorname;
    }

    public String getNachname(){
        return nachname;
    }

    public int getBeglicheneSchulden(){
        return beglicheneSchulden;
    }

    public ArrayList<Verschuldung> getSchuldenLog() {
        return schuldenLog;
    }
}
