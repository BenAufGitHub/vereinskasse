package grafiken;


import users.Person;
import users.Personenbeschreibung;

public abstract class FunktionalesBearbeitungsPanel extends OuterJPanel{

    private String namenRegEx = "\\b([A-ZÀ-ÿ][-,a-z. 'äöüß]+[ ]*)+";
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
        getParent().addLetzteBearbeitet(person.getBeschreibung());
        getParent().showMenuPanel();
    }

    public void resetUndBack() {
        getParent().addLetzteBearbeitet(ausgangsDaten);
        getParent().showMenuPanel();
    }

    public void deleteUndBack() {
        getParent().loescheAusLetzteBearbeitet(ausgangsDaten);
        getPM().save(null, ausgangsDaten);
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
