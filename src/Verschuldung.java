import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Verschuldung {

    static final private int tageBisZinsen = 7;
    static final private double zinssatz = 1.5;

    private String grund;
    private int betrag;
    private DateKeeper datum = new DateKeeper();



    public Verschuldung(String grund, int betrag) {
        if(betrag <= 0)
            throw new IllegalArgumentException("Betrag muss größer 0 sein.");
        this.grund = grund;
        this.betrag = betrag;
    }


    public int berechneZuBezahlen() {
        if(istUeberfaellig())
            return (int) (betrag * zinssatz);
        return betrag;
    }


    public boolean istUeberfaellig(){
        int tage = getTageVerstrichen();
        return tage >= tageBisZinsen;
    }


    public int getTageVerstrichen() {
        LocalDate current = LocalDate.now();
        LocalDate seit = LocalDate.of(datum.year, datum.month, datum.day);
        return (int) ChronoUnit.DAYS.between(seit, current);
    }


    public boolean isEqual(Verschuldung vergleich){
        return grund.equals(vergleich.getGrund())
                && betrag == vergleich.getBetrag()
                && datum.day == vergleich.datum.day
                && datum.month == vergleich.datum.month
                && datum.year == vergleich.datum.year;
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
        public final int year;
        public final int month;
        public final int day;

        private DateKeeper(){
            LocalDate time = LocalDate.now();
            year = time.getYear();
            month = time.getMonthValue();
            day = time.getDayOfMonth();
        }

    }
}