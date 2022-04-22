package grafiken;

public class GeldFormat {

    public static int toGeld(String geld) {
        int comma = geld.indexOf(',');
        if(comma == -1) return Integer.parseInt(geld) * 100;
        int euros = Integer.parseInt(geld.substring(0, comma));
        int centGrenze = Math.min(comma+3, geld.length());
        int cents = Integer.parseInt(geld.substring(comma+1, centGrenze));
        if(centGrenze-(comma+1) == 1)
            cents *= 10;
        int amount = (100*euros) + cents;
        return (geld.charAt(0) != '-') ? amount : -amount;
    }

    public static boolean isValidMoney(String geld) {
        int comma = geld.indexOf(',');
        if(comma == -1) return geld.matches("-?\\d+");
        if(comma == geld.length()-1) return false;
        boolean euros = geld.substring(0, comma).matches("-?\\d+");
        boolean cents = geld.substring(comma+1).matches("\\d+");
        return euros && cents;
    }

    public static String geldToStr(int betrag, boolean mitEuro) {
        int euros = Math.abs(betrag / 100);
        int cents = Math.abs(betrag % 100);
        String c = (cents >= 10) ? cents + "" : "0" + cents;
        String er = (betrag < 0) ?  "-" +euros : ""+euros;
        if(mitEuro)
            return er+","+c+"€";
        return er+","+c;
    }
}
