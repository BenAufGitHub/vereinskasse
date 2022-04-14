import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Verschuldung {

    static private int tageBisZinsen = 7;
    static private double zinssatz = 1.5;

    private String grund;
    private int betrag;
    private DateKeeper datum = new DateKeeper();
    transient private boolean abbezahlt = false;



    public Verschuldung(String grund, int betrag) {
        if(betrag <= 0)
            throw new IllegalArgumentException("Betrag muss größer 0 sein.");
        this.grund = grund;
        this.betrag = betrag;
    }


    public int berechneZuBezahlen() {
        if(istAbbezahlt()) return 0;
        if(istUeberfaellig())
            return (int) (betrag * zinssatz);
        return betrag;
    }


    public boolean istUeberfaellig(){
        int tage = getTageVerstrichen();
        return tage >= tageBisZinsen;
    }

    public boolean istAbbezahlt(){
        return abbezahlt;
    }

    public void macheAbbezahlt() {
        abbezahlt = true;
    }

    public int getTageVerstrichen() {
        LocalDate current = LocalDate.now();
        LocalDate seit = LocalDate.of(datum.year, datum.month, datum.day);
        return (int) ChronoUnit.DAYS.between(seit, current);
    }

    // ===================== Getter und Setter ===================================


    public String getGrund() {
        return grund;
    }

    public int getBetrag() {
        return betrag;
    }


    // ====== Date =======


    private class DateKeeper {
        private int year;
        private int month;
        private int day;

        private DateKeeper(){
            LocalDate time = LocalDate.now();
            year = time.getYear();
            month = time.getMonthValue();
            day = time.getDayOfMonth();
        }

    }
}