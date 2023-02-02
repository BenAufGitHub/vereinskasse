package users;

import com.mysql.cj.protocol.a.LocalDateTimeValueEncoder;
import helpers.QueryStrings;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

public class Person {

    private String vorname;
    private String nachname;
    private int id;

    private static int tempSchuldID = -1;

    private int gutschrift = 0;
    private ArrayList<Verschuldung> schulden = new ArrayList<>();
    private ArrayList<String> geschichte = new ArrayList<>();

    /** Bei vor dem verändern gespeicherte Queries */
    private ArrayList<String> queryListe = new ArrayList<>();


    public Person(String vorname, String nachname, int id) {
        this.vorname = vorname;
        this.nachname = nachname;
        this.id = id;
    }

    public Person(String vorname, String nachname, int id, int gutschrift) {
        this.vorname = vorname;
        this.nachname = nachname;
        this.id = id;
        this.gutschrift = gutschrift;
    }


    public static int getTempSchuldID() {
        int id = tempSchuldID++;
        return id;
    }

    /** Nebeneffekt: Falls genug Geld auf dem Konto ist, wird Schuld direkt beglichen.
     * id < 0: noch nicht in db eingetragen
     * */
    public void addVerschuldung(String grund, int betrag, int id) {
        schulden.add(new Verschuldung(grund, betrag, id));
        logVerschuldung(Timestamp.valueOf(LocalDateTime.now()), grund, betrag);

        versucheSchuldenBegleichen();
    }

    public void streicheSchuldAusQueries (int schuldID) {
        String matcher = "-- sid="+schuldID;
        queryListe.removeIf(content -> content.indexOf(matcher) >= 0);
    }

    /** Konto wird um Betrag ergaenzt. Auch negative Betraege sind moeglich. */
    public void fuelleKonto(int betrag) {
        gutschrift += betrag;
        logGutschrift(Timestamp.valueOf(LocalDateTime.now()), betrag, getRestSchulden());
        versucheSchuldenBegleichen();
    }


    // ===================== Schulden begleichen =========================

    /** Konto bleibt unveraendert, angegebene Schuld wird beglichen. */
    public void begleicheOhneKontoEingriff(Verschuldung veschuldung) {
        begleicheAusSchulden(veschuldung, true);
    }

    /** fuellt Gutschrift bis erste Verschuldung beglichen wird, anschliessend wird beglichen. */
    public void auffuellenBisErsteBeglichen() {
        int menge = Math.max(getMengeBisNaechsteAbzahlung(), 0);
        fuelleKonto(menge);
    }

    private void versucheSchuldenBegleichen() {
        while(!schulden.isEmpty()){
            if(getMengeBisNaechsteAbzahlung() > 0) break;
            Verschuldung schuld = schulden.get(0);
            int kosten = schuld.berechneZuBezahlen();
            gutschrift -= kosten;
            begleicheAusSchulden(schuld, false);
        }
    }

    /** Alle schulden werden beglichen. Nebeneffekt: Gutschrift wird auf 0 gesetzt. */
    public void begleicheAlleSchulden() {
        gutschrift += getRestSchulden();
        versucheSchuldenBegleichen();
    }

    private void begleicheAusSchulden(Verschuldung schuld, boolean verwerfen) {
        for(int i = 0; i< schulden.size(); i++){
            if(schuld.getID() != schulden.get(i).getID())
                continue;
            schuld = schulden.get(i);
            if(verwerfen)
                logVerwerfen(Timestamp.valueOf(LocalDateTime.now()));
            else
                logBezahlung(Timestamp.valueOf(LocalDateTime.now()),schuld.getGrund(), schuld.berechneZuBezahlen());
            schulden.remove(i);
            return;
        }
    }

    public void verwerfeAusSchulden(Verschuldung schuld) {

    }

    // ========================== Abfragen ================================


    public boolean istSchuldenfrei() {
        return getRestSchulden() <= 0;
    }

    public boolean hatNegativSchulden() {
        return getRestSchulden() < 0;
    }

    // ============================= Geschichte ================================


    public void logErstellung(Timestamp date) {
        geschichte.add(getDatum(date)+": Person erstellt.");
    }


    public void logGutschrift(Timestamp date, int veraenderung, int gesamt){
        geschichte.add(getDatum(date)+": "+inEuro(veraenderung)+" zum Konto hinzugefügt, gesamt Schulden: "+inEuro(gesamt)+".");
    }


    public void logVerschuldung(Timestamp date, String grund, int betrag) {
        grund = (grund != null) ? grund : "--";
        geschichte.add(getDatum(date)+": Schulden von "+inEuro(betrag)+" wegen "+grund+" aufgenommen.");
    }

    public void logBezahlung(Timestamp date, String grund, int kosten) {
        grund = (grund != null) ? grund : "--";
        geschichte.add(getDatum(date)+": Schulden wegen "+grund+" für "+inEuro(kosten)+" beglichen.");
    }

    public void logVerwerfen(Timestamp date) {
        geschichte.add(getDatum(date)+": Eine Schuld verworfen.");
    }


    private String inEuro(int betrag){
        int euros = betrag / 100;
        int cent = Math.abs(betrag % 100);
        String er = (betrag < 0) ?  "-" +Math.abs(euros) : ""+euros;
        String ct = (cent >= 10) ? cent + "" : "0"+cent;
        return er+","+ct+"€";
    }

    private String getDatum(Timestamp date) {
        return new SimpleDateFormat("dd-MM-yyyy").format(date);
    }


    // ============================== Setter ====================================


    public void setName(String vorname, String nachname) {
        this.vorname = vorname;
        this.nachname = nachname;
    }


    // =================================== getter ========================================


    /** Info: berechnet Gutschrift postiv mit ein. */
    public int getRestSchulden(){
        int gesamtSchulden = 0;
        for(Verschuldung s : schulden){
            gesamtSchulden += s.berechneZuBezahlen();
        }
        return gesamtSchulden - gutschrift;
    }

    /** Fehlendes Geld auf Konto bis naechste Abzahlung */
    public int getMengeBisNaechsteAbzahlung() {
        if(schulden.isEmpty()) return 0;
        return schulden.get(0).berechneZuBezahlen() - gutschrift;
    }

    public String getVorname(){
        return vorname;
    }

    public String getNachname(){
        return nachname;
    }

    public int getID() {
        return id;
    }

    public int getGutschrift(){
        return gutschrift;
    }

    /** Ueberblick unbeglichener Schulden als Liste. */
    public ArrayList<Verschuldung> getSchulden() {
        return schulden;
    }

    public void setSchulden(ArrayList<Verschuldung> schulden) {
        this.schulden = schulden;
    }

    /** Ueberblick ueber alle Aktionen als String-Liste. */
    public ArrayList<String> getGeschichte() {
        return geschichte;
    }

    public void setGeschichte(ArrayList<String> geschichte) {
        this.geschichte = geschichte;
    }

    public Personenbeschreibung getBeschreibung() {
        return new Personenbeschreibung(getVorname()+" "+getNachname(), getID(), getGutschrift());
    }

    public void addQuery (String qry) {
        queryListe.add(qry);
    }

    public ArrayList<String> popQueries () {
        ArrayList<String> tempQueries = queryListe;
        queryListe = new ArrayList<String>();
        return tempQueries;
    }

    public boolean hasNoQueries () {
        return queryListe.size() == 0;
    }
}
