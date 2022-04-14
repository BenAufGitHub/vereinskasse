import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class Person {

    private String vorname;
    private String nachname;

    private int kontostand = 0;
    private ArrayList<Verschuldung> schuldenLog = new ArrayList<>();
    private ArrayList<String> geschichte = new ArrayList<>();


    public Person(String vorname, String nachname) {
        this.vorname = vorname;
        this.nachname = nachname;
        registriereErstmalig();
    }

    public void addVerschuldung(String grund, int betrag) {
        schuldenLog.add(new Verschuldung(grund, betrag));
        registriereVerschuldung(grund, betrag);
        versucheSchuldenBegleichen();
    }

    public void fuelleKonto(int betrag) {
        kontostand += betrag;
        registriereKontostand(betrag);
        versucheSchuldenBegleichen();
    }


    // ===================== Schulden begleichen =========================


    public void begleicheOhneKontoEingriff(Verschuldung veschuldung) {
        streicheAusSchuldenLog(veschuldung);
    }

    public void auffuellenBisErsteBeglichen() {
        int menge = Math.max(getMengeBisNaechsteAbzahlung(), 0);
        fuelleKonto(menge);
    }

    public void versucheSchuldenBegleichen() {
        while(!schuldenLog.isEmpty()){
            if(getMengeBisNaechsteAbzahlung() > 0) break;
            Verschuldung schuld = schuldenLog.get(0);
            int kosten = schuld.berechneZuBezahlen();
            kontostand -= kosten;
            streicheAusSchuldenLog(schuld);
        }
    }

    public void begleicheAlleSchulden() {
        kontostand += getRestSchulden();
        versucheSchuldenBegleichen();
    }

    private void streicheAusSchuldenLog(Verschuldung schuld) {
        for(int i=0; i<schuldenLog.size(); i++){
            if(!schuld.isEqual(schuldenLog.get(i)))
                continue;
            schuld = schuldenLog.get(i);
            registriereBezahlung(schuld.getGrund(), schuld.berechneZuBezahlen(), schuld.getTageVerstrichen());
            schuldenLog.remove(i);
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
        String ct = (cent >= 10) ? cent + "" : "0"+cent;
        return euros+","+ct+"€";
    }

    private String getDatum() {
        return new SimpleDateFormat("dd-MM-yyyy").format(new Date());
    }


    // ============================== Setter ====================================


    /**
     * TODO: Wrapper-Methode mit Speichern des neuen Namens (und löschen des alten).
     */
    private void setName(String vorname, String nachname) {
        this.vorname = vorname;
        this.nachname = nachname;
        registriereNamechange(vorname, nachname);
    }


    // =================================== getter ========================================


    /** Berechnet Kontostand postiv mit ein. */
    public int getRestSchulden(){
        int gesamtSchulden = 0;
        for(Verschuldung s : schuldenLog){
            gesamtSchulden += s.berechneZuBezahlen();
        }
        return gesamtSchulden - kontostand;
    }

    /** Fehlendes Geld auf Konto bis nächste Abzahlung */
    public int getMengeBisNaechsteAbzahlung() {
        if(schuldenLog.isEmpty()) return 0;
        return schuldenLog.get(0).berechneZuBezahlen() - kontostand;
    }

    public String getVorname(){
        return vorname;
    }

    public String getNachname(){
        return nachname;
    }

    public int getKontostand(){
        return kontostand;
    }

    public ArrayList<Verschuldung> getSchuldenLog() {
        return schuldenLog;
    }

    public ArrayList<String> getGeschichte() {
        return geschichte;
    }
}
