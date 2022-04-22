package grafiken.bearbeiten;


import grafiken.MainFrame;
import grafiken.OuterJPanel;
import users.Person;
import users.Personenbeschreibung;


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
        getPM().save(person, ausgangsDaten);
        getFrame().addLetzteBearbeitet(person.getBeschreibung());
        getFrame().showMenuPanel();
    }

    public void resetUndBack() {
        getFrame().addLetzteBearbeitet(ausgangsDaten);
        getFrame().showMenuPanel();
    }

    public void deleteUndBack() {
        getFrame().loescheAusLetzteBearbeitet(ausgangsDaten);
        getPM().save(null, ausgangsDaten);
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
