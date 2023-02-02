package helpers;

import java.sql.Array;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.sql.Date;
import java.sql.Time;

public class QueryStrings {

    public static final String GET_PROFILE = "SELECT * from user_konten ORDER BY user_id;";

    public static final String GET_ABC_SORTED_IDS = "SELECT user_id FROM user ORDER BY nachname, vorname";

    public static String getProfil(int id) {
        return "SELECT user_id, CONCAT(vorname, nachname) AS user_name," +
                "(calc_schulden(user_id)+ CAST(gutschrift AS SIGNED)) AS kontostand" +
                "FROM user WHERE user_id=" +
                id+";";
    }

    public static String getBaseProfil(int id) {
        return "SELECT user_id, vorname, nachname, gutschrift, erstellt " +
                "FROM user WHERE user_id="
                +id+";";
    }

    public static String getKontostand(int id) {
        return "SELECT (CAST(gutschrift AS SIGNED)-calc_schulden(user_id)) AS kontostand FROM user WHERE user_id="+id+";";
    }

    public static String getName(int id) {
        return "SELECT vorname, nachname FROM user WHERE user_id="+id+";";
    }

    public static final String GET_IDS_ALPHABETICAL = "SELECT id FROM user ORDER BY nachname, vorname;";

    public static String updateName(int id, String vorname, String nachname) {
        return "UPDATE user SET vorname='"+vorname+"', nachname='"+nachname+"' WHERE user_id="+id+";";
    }

    /** Fügt menge zur Konto Gutschrift hinzu und begleicht anschließend Schulden wenn möglich.
     *  Bei menge=0 werden nur begleichbare Schulden beglichen.
     * */
    public static String kontoAusgleichen(int id, int menge) {
        return "CALL konto_ausgleichen("+id+", "+menge+");";
    }

    public static String erschaffePerson(String vorname, String nachname) {
        return "INSERT INTO user (vorname, nachname) VALUES('"+vorname+"', '"+nachname+"');";
    }

    public static final String getNewID = "SELECT last_insert_id() as id;";

    public static String getOffeneSchulden(int id) {
        return "SELECT aktion.datum AS datum, grund, betrag, schuld.schuld_id as schuld_id FROM schuld " +
                "INNER JOIN aktion ON schuld.schuld_id=aktion.schuld_id " +
                "WHERE schuld.user_id="+id+" AND schuld.abbezahlt IS NULL AND aktion.handlung='belasten'" +
                "ORDER BY datum, schuld.schuld_id;";
    }


    public static String getGeschichte(int id) {
        return "SELECT datum, handlung, schuld.grund, schuld.betrag, schuld.abbezahlt, guthaben.betrag, guthaben.neuer_stand FROM aktion " +
                "LEFT JOIN schuld " +
                "ON schuld.schuld_id = aktion.schuld_id " +
                "LEFT JOIN guthaben " +
                "ON guthaben.guthaben_id = aktion.guthaben_id " +
                "WHERE aktion.user_id="+id+" " +
                "ORDER BY datum, aktion_id;";
    }


    public static String loeschePerson(int id) {
        return "DELETE FROM user WHERE user_id="+id+";";
    }


    public static String benennePerson(int id, String vorname, String nachname) {
        return "UPDATE user SET vorname='"+vorname+"', SET nachname='"+nachname+"' WHERE user_id="+id+";";
    }

    public static String insertSchuld(int userID, String grund, int betrag, int schuldID) {
        return "INSERT INTO schuld (user_id, grund, betrag) VALUES("+userID+", '"+grund+"', "+betrag+"); " +
                kontoAusgleichen(userID, 0) +" -- sid="+schuldID+";";
    }

    public static String verwerfeSchuld(int schuldID, int userID) {
        return "DELETE FROM schuld WHERE schuld_id="+schuldID+"; "+kontoAusgleichen(userID, 0);
    }

    private static String getTime(LocalDateTime datum) {
        java.sql.Timestamp sq = Timestamp.valueOf(datum);
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        return sdf.format(sq);
    }
}
