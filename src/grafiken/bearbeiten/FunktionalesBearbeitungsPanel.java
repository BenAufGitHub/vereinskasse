package grafiken.bearbeiten;


import grafiken.MainFrame;
import grafiken.OuterJPanel;
import helpers.Query;
import helpers.QueryStrings;
import users.Person;
import users.Personenbeschreibung;

import java.sql.SQLException;

import static javax.swing.JOptionPane.showMessageDialog;


public abstract class FunktionalesBearbeitungsPanel extends OuterJPanel {

    protected final String namenRegEx = "\\b([A-ZÀ-ÿ][-,a-z. 'äöüß]+[ ]*)+";
    private Personenbeschreibung ausgangsDaten;
    private Person person;


    public FunktionalesBearbeitungsPanel(MainFrame parent, Personenbeschreibung pb) {
        super(parent);
        ausgangsDaten = pb;
        person = getPM().ladePerson(ausgangsDaten);
    }


    // =================================== Button Events ================================


    public void saveUndBack() {
        if(!validateNames()){
            warneSpeichernFehlgeschlagen();
            return;
        }
        saveNames();
        getFrame().addLetzteBearbeitet(person.getBeschreibung());
        if(!getPM().update(person))
            showMessageDialog(null, "Person konnte nicht vollständig aktualisiert werden.");
        getFrame().showMenuPanel();
    }

    public void resetUndBack() {
        getFrame().addLetzteBearbeitet(ausgangsDaten);
        getFrame().showMenuPanel();
    }

    public void deleteUndBack() {
        getFrame().loescheAusLetzteBearbeitet(ausgangsDaten);
        try {
            Query.update(QueryStrings.loeschePerson(ausgangsDaten.id));
        } catch (SQLException e) {
            e.printStackTrace();
            showMessageDialog(null, "SQLError: " + e);
        }
        getFrame().showMenuPanel();
    }


    // ============================= Background Methoden =======================


    protected abstract void warneSpeichernFehlgeschlagen();

    protected abstract boolean validateNames();

    protected abstract void saveNames ();


    // ============================= getter und setter =========================


    public Person getPerson() {
        return person;
    }

    public Personenbeschreibung getAusgangsDaten() {
        return ausgangsDaten;
    }

}
