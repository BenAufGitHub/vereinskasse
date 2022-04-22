package users;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Person {

    private String vorname;
    private String nachname;
    private int id;

    private int kontostand = 0;
    private ArrayList<Verschuldung> schulden = new ArrayList<>();
    private ArrayList<String> geschichte = new ArrayList<>();


    public Person(String vorname, String nachname, int id) {
        this.vorname = vorname;
        this.nachname = nachname;
        this.id = id;
        registriereErstmalig();
    }

    /** Nebeneffekt: Falls genug Geld auf dem Konto ist, wird Schuld direkt beglichen. */
    public void addVerschuldung(String grund, int betrag) {
        schulden.add(new Verschuldung(grund, betrag));
        registriereVerschuldung(grund, betrag);
        versucheSchuldenBegleichen();
    }

    /** Konto wird um Betrag ergaenzt. Auch negative Betraege sind moeglich. */
    public void fuelleKonto(int betrag) {
        kontostand += betrag;
        registriereKontostand(betrag);
        versucheSchuldenBegleichen();
    }


    // ===================== Schulden begleichen =========================

    /** Konto bleibt unveraendert, angegebene Schuld wird beglichen. */
    public void begleicheOhneKontoEingriff(Verschuldung veschuldung) {
        streicheAusSchuldenLog(veschuldung);
    }

    /** fuellt Kontostand bis erste Verschuldung beglichen wird, anschliessend wird beglichen. */
    public void auffuellenBisErsteBeglichen() {
        int menge = Math.max(getMengeBisNaechsteAbzahlung(), 0);
        fuelleKonto(menge);
    }

    private void versucheSchuldenBegleichen() {
        while(!schulden.isEmpty()){
            if(getMengeBisNaechsteAbzahlung() > 0) break;
            Verschuldung schuld = schulden.get(0);
            int kosten = schuld.berechneZuBezahlen();
            kontostand -= kosten;
            streicheAusSchuldenLog(schuld);
        }
    }

    /** Alle schulden werden beglichen. Nebeneffekt: Kontostand wird auf 0 gesetzt. */
    public void begleicheAlleSchulden() {
        kontostand += getRestSchulden();
        versucheSchuldenBegleichen();
    }

    private void streicheAusSchuldenLog(Verschuldung schuld) {
        for(int i = 0; i< schulden.size(); i++){
            if(!schuld.isEqual(schulden.get(i)))
                continue;
            schuld = schulden.get(i);
            registriereBezahlung(schuld.getGrund(), schuld.berechneZuBezahlen(), schuld.getTageVerstrichen());
            schulden.remove(i);
            return;
        }
    }

    // ========================== Abfragen ================================


    public boolean istSchuldenfrei() {
        return getRestSchulden() <= 0;
    }

    public boolean hatNegativSchulden() {
        return getRestSchulden() < 0;
    }

    // ============================= Geschichte ================================


    private void registriereErstmalig() {
        geschichte.add(getDatum()+": " + getVorname()+" "+getNachname()+" erstellt.");
    }

    private void registriereKontostand(int veraenderung){
        geschichte.add(getDatum()+": "+inEuro(veraenderung)+" zum Konto hinzugefügt, gesamt "+inEuro(kontostand)+".");
    }

    private void registriereNamechange(String neuvor, String neunach) {
        geschichte.add(getDatum()+": Name zu " + neuvor+" "+neunach+" geändert.");
    }

    private void registriereVerschuldung(String grund, int betrag) {
        geschichte.add(getDatum()+": Schulden von "+inEuro(betrag)+" wegen "+grund+" aufgenommen.");
    }

    private void registriereBezahlung(String grund, int kosten, int tage) {
        geschichte.add(getDatum()+": Schulden wegen "+grund+" für "+inEuro(kosten)+" nach "+tage+" Tagen beglichen.");
    }


    private String inEuro(int betrag){
        int euros = betrag / 100;
        int cent = Math.abs(betrag % 100);
        String er = (betrag < 0) ?  "-" +Math.abs(euros) : ""+euros;
        String ct = (cent >= 10) ? cent + "" : "0"+cent;
        return er+","+ct+"€";
    }

    private String getDatum() {
        return new SimpleDateFormat("dd-MM-yyyy").format(new Date());
    }


    // ============================== Setter ====================================


    public void setName(String vorname, String nachname) {
        this.vorname = vorname;
        this.nachname = nachname;
        registriereNamechange(vorname, nachname);
    }


    // =================================== getter ========================================


    /** Info: berechnet Kontostand postiv mit ein. */
    public int getRestSchulden(){
        int gesamtSchulden = 0;
        for(Verschuldung s : schulden){
            gesamtSchulden += s.berechneZuBezahlen();
        }
        return gesamtSchulden - kontostand;
    }

    /** Fehlendes Geld auf Konto bis naechste Abzahlung */
    public int getMengeBisNaechsteAbzahlung() {
        if(schulden.isEmpty()) return 0;
        return schulden.get(0).berechneZuBezahlen() - kontostand;
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

    public int getKontostand(){
        return kontostand;
    }

    /** Ueberblick unbeglichener Schulden als Liste. */
    public ArrayList<Verschuldung> getSchulden() {
        return schulden;
    }

    /** Ueberblick ueber alle Aktionen als String-Liste. */
    public ArrayList<String> getGeschichte() {
        return geschichte;
    }

    public Personenbeschreibung getBeschreibung() {
        return new Personenbeschreibung(getVorname(), getNachname(), getID());
    }
}
