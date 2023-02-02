package users;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class Verschuldung {

    static final private int tageBisZinsen = 10;
    static final private double zinssatz = 1.5;

    private String grund;
    private int betrag;
    private int id;
    private DateKeeper datum = new DateKeeper();



    public Verschuldung(String grund, int betrag, int id) {
        if(betrag <= 0)
            throw new IllegalArgumentException("Betrag muss größer 0 sein.");
        this.grund = grund;
        this.betrag = betrag;
        this.id = id;
    }


    public int getID () {
        return id;
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
        boolean equalGrund = (grund !=null) ? grund.equals(vergleich.getGrund()) : vergleich.getGrund()==null;
        return equalGrund
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

    public void setDatum(LocalDate datum) {this.datum = new DateKeeper(datum);}

    public double getZinssatz() {
        return (!istUeberfaellig()) ? 1.0 : 1.5;
    }

    // ====== Date =======


    private class DateKeeper {
        public final int year;
        public final int month;
        public final int day;

        private LocalDate ldate = null;

        private DateKeeper(){
            ldate = LocalDate.now();
            year = ldate.getYear();
            month = ldate.getMonthValue();
            day = ldate.getDayOfMonth();
        }

        private DateKeeper(LocalDate ldate) {
            this.ldate = ldate;
            year = ldate.getYear();
            month = ldate.getMonthValue();
            day = ldate.getDayOfMonth();
        }

        public LocalDate toLocalDate() {
            return ldate;
        }
    }
}