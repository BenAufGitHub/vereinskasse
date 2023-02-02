package helpers;

import com.google.gson.Gson;
import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.tuple.Pair;
import users.Person;
import users.Personenbeschreibung;
import users.Verschuldung;

import javax.management.MBeanTrustPermission;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static javax.swing.JOptionPane.showMessageDialog;


/**
 * Interaktionen mit der Datenbank (also den Speicherdateien)
 */
public class SaveAssistant {
    private static String saveFileValidationRegEx = "\\b([A-ZÀ-ÿ][-,a-z. 'äöüß]+[ ]*)+, \\b([A-ZÀ-ÿ][-,a-z. 'äöüß]+[ ]*)+#\\d+\\.json";
    public static String savePath = ".\\resources\\save_files\\";


    public static Person erschaffePerson(String vorname, String nachname) throws SQLException {
        String erschaffe = QueryStrings.erschaffePerson(vorname, nachname);
        Query.update(erschaffe);
        ResultSet rs = Query.getSet(QueryStrings.getNewID);
        rs.next();
        int id = rs.getInt("id");
        return new Person(vorname, nachname, id);
    }

    public static Person ladePerson(Personenbeschreibung pb) {
        return ladePerson(pb.id);
    }

    /** Gibt null zurück wenn File nicht gefunden. */
    public static Person ladePerson(int id) {
        try {
            return ladePersonThrows(id);
        } catch (SQLException exc) {
            exc.printStackTrace();
            showMessageDialog(null, exc);
            return null;
        }
    }


    private static Person ladePersonThrows(int id) throws SQLException {
        String ausgleichen = QueryStrings.kontoAusgleichen(id, 0);
        Query.update(ausgleichen);
        ResultSet rs = Query.getSet(QueryStrings.getBaseProfil(id));

        rs.next();
        String vorname = rs.getString("vorname");
        String nachname = rs.getString("nachname");
        int gutschrift = rs.getInt("gutschrift");
        Timestamp datum = rs.getTimestamp("erstellt");

        rs = Query.getSet(QueryStrings.getKontostand(id));
        rs.next();
        int accountBalanceDBProbe = rs.getInt("kontostand");

        Person person = new Person(vorname, nachname, id, gutschrift);
        person.setSchulden(loadSchulden(id));
        person.logErstellung(datum);
        loadGeschichte(id, person);

        // sollte beim laden nicht passieren
        if(-accountBalanceDBProbe != person.getRestSchulden())
            showMessageDialog(null, "Person nicht korrekt mit Database synchronisiert");
        return person;
    }

    private static void loadGeschichte(int id, Person p) throws SQLException {
        ArrayList list = new ArrayList();
        String qry = QueryStrings.getGeschichte(id);
        ResultSet rs = Query.getSet(qry);

        while(rs.next()) {
            Timestamp datum = rs.getTimestamp("datum");
            String aktion = rs.getString("handlung");
            if(aktion.equals("verwerfen"))
                p.logVerwerfen(datum);
            if(aktion.equals("belasten"))
                p.logVerschuldung(datum, rs.getString("schuld.grund"), rs.getInt("schuld.betrag"));
            if(aktion.equals("begleichen"))
                p.logBezahlung(datum, rs.getString("schuld.grund"), rs.getInt("schuld.abbezahlt"));
            if(aktion.equals("einzahlen"))
                p.logGutschrift(datum, rs.getInt("guthaben.betrag"), rs.getInt("guthaben.neuer_stand"));
        }
    }

    private static ArrayList<Verschuldung> loadSchulden(int id) throws SQLException {
        ArrayList<Verschuldung> list = new ArrayList<>();
        String qry = QueryStrings.getOffeneSchulden(id);
        ResultSet rs = Query.getSet(qry);

        while(rs.next()) {
            Timestamp datum = rs.getTimestamp("datum");
            String grund = rs.getString("grund");
            int betrag = rs.getInt("betrag");
            int schuldID = rs.getInt("schuld_id");
            Verschuldung vs = new Verschuldung(grund, betrag, schuldID);
            vs.setDatum(datum.toLocalDateTime().toLocalDate());
            list.add(vs);
        }

        return list;
    }

    public static void loeschePerson(Personenbeschreibung pb) throws SQLException {
        String qry = QueryStrings.loeschePerson(pb.id);
        Query.update(qry);
    }


    public static void bennenePersonUm(Person person) throws SQLException {
        String qry = QueryStrings.benennePerson(person.getID(), person.getVorname(), person.getNachname());
        Query.update(qry);
    }


    /** Return: ArrayListe aus Personenbeschreibungen von allen gefunden Personendateien */
    public static ArrayList<Personenbeschreibung> getPersonenbeschreibungen() throws SQLException {
        ArrayList<Personenbeschreibung> liste = new ArrayList<>();
        ResultSet rs = Query.getSet(QueryStrings.GET_PROFILE);

        while(rs.next()) {
            int id = rs.getInt("user_id");
            String vorname = rs.getString("vorname");
            String nachname = rs.getString("nachname");
            int letzterKontostand = rs.getInt("letzterKontostand");
            // liste.add(new Personenbeschreibung(vorname, nachname, id, letzterKontostand));
        }
        return liste;
    }



    private static boolean istSaveFile(String filename) {
        if(filename.indexOf(',') != filename.lastIndexOf(',')) return false;
        if(filename.indexOf('#') != filename.lastIndexOf('#')) return false;
        return filename.matches(saveFileValidationRegEx);
    }
}
