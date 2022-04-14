import java.util.ArrayList;

public class Person {

    private String vorname;
    private String nachname;

    private int kontostand = 0;
    private ArrayList<Verschuldung> schuldenLog = new ArrayList<>();
    private ArrayList<String> geschichte = new ArrayList<>();


    public Person(String vorname, String nachname) {
        this.vorname = vorname;
        this.nachname = nachname;
    }

    public void addVerschuldung(String grund, int betrag) {
        schuldenLog.add(new Verschuldung(grund, betrag));
        versucheSchuldenBegleichen();
    }

    public void fuelleKonto(int betrag) {
        kontostand += betrag;
        versucheSchuldenBegleichen();
    }


    // ===================== Schulden begleichen =========================


    public void begleicheOhneKontoEingriff(Verschuldung veschuldung) {
        begleicheEineVerschuldung(veschuldung);
    }

    public void auffuellenBisObersteBeglichen() {
        int menge = Math.max(getMengeBisNaechsteAbzahlung(), 0);
        fuelleKonto(menge);
    }

    public void versucheSchuldenBegleichen() {
        while(!schuldenLog.isEmpty()){
            int naechsteSchulden = schuldenLog.get(0).berechneZuBezahlen();
            if(getMengeBisNaechsteAbzahlung() > 0) break;
            kontostand -= naechsteSchulden;
            begleicheEineVerschuldung(schuldenLog.get(0));
        }
    }


    /** Ohne Kontoeingriff! */
    private void begleicheEineVerschuldung(Verschuldung schuld) {
        streicheAusSchuldenLog(schuld);
    }

    public void begleicheAlleSchulden() {
        kontostand += getRestSchulden();
        versucheSchuldenBegleichen();
    }

    private void streicheAusSchuldenLog(Verschuldung schuld) {
        for(int i=0; i<schuldenLog.size(); i++){
            if(!schuld.isEqual(schuldenLog.get(i)))
                continue;
            schuldenLog.remove(i);
        }
    }

    // ========================== Abfragen ================================


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
