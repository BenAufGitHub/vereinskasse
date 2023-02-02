package helpers;

import users.Person;
import users.Personenbeschreibung;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;

public class PersonenManager {

    private PersonenListe pbs = new PersonenListe();

    public PersonenListe getPBs () {return pbs;}

    public Person ladePerson(Personenbeschreibung pb) {
        return SaveAssistant.ladePerson(pb);
    }


    /**
     * @return true wenn erfolgreich, false ist fehler mit datenbank.
     */
    public boolean update(Person p) {
        try {
            Query.update(QueryStrings.updateName(p.getID(), p.getVorname(), p.getNachname()));
            execPersonQueries(p);
            String qry = QueryStrings.kontoAusgleichen(p.getID(), 0);
            Query.update(qry);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void execPersonQueries(Person p) {
        ArrayList<String> queries = p.popQueries();
        for (String qry : queries) {
            try {
                Query.update(qry);
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("SQL-Exception: "+e.getSQLState());
            }
        }
    }

    public boolean delete(Personenbeschreibung pb) {
        return removePerson(pb);
    }


    // ============================== Private Methoden ============================


    /** return bool = success */
    private boolean removePerson(Personenbeschreibung pb) {
        try {
            SaveAssistant.loeschePerson(pb);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }


}
