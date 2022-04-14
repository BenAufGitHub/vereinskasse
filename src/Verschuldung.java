public class Verschuldung {
    private String grund;
    private int betrag;

    public Verschuldung(String grund, int betrag) {
        this.grund = grund;
        this.betrag = betrag;
    }

    public String getGrund() {
        return grund;
    }

    public int getBetrag() {
        return betrag;
    }
}