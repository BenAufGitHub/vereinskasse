package grafiken.bearbeiten;

import grafiken.GeldFormat;
import grafiken.MainFrame;
import helpers.Profilliste;
import helpers.SaveAssistant;
import users.Personenbeschreibung;
import users.Verschuldung;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Insets;
import java.util.List;

public class BPanel extends GrafischesBearbeitungsPanel {

    private boolean editing = false;

    public BPanel(MainFrame frame, Personenbeschreibung pb) {
        super(frame, pb);
        customizeComponents();
        fillPersonData();
    }

    private void fillPersonData() {
        updateKontoLabel();
        updateNaechsteLabel();
        updateGesamtSchulden();
        zeigeSchulden();
    }


    @Override
    protected void warneSpeichernFehlgeschlagen() {
        JTextArea info = getInfo();
        info.setText("Name ist nicht im korrekten Format. \nErlaubt sind Doppelnamen, deutsches Alphabet..." +
                "\nNicht erlaubt: Großschreiben im Wort, Leerfelder und Sonderzeichen wie &/({# etc." );
    }

    @Override
    protected boolean validateNames() {
        String vor = getVorname().getText().strip();
        String nach = getNachname().getText().strip();
        return vor.matches(namenRegEx) && nach.matches(namenRegEx);
    }

    @Override
    protected void saveNames() {
        String vor = getVorname().getText().strip();
        String nach = getNachname().getText().strip();
        getPerson().setName(vor, nach);
    }


    private void customizeComponents() {
        customizeBack();
        customizeDelete();
        customizeEditing();
        customizeResets();
        customizeNamen();
        customizeSave();
        customizeFuellen();
        customizeErgaenzen();
        customizeAdd();
        customizeLog();
    }

    private void zeigeSchulden() {
        switchPanels(SchuldenAnsicht.createAnsicht(getPerson().getSchulden(), eastWidth));
    }


    private void updateNaechsteLabel() {
        JTextArea area = getNaechste();
        String text = "Geld bis nächste\nAbzahlung: ";
        int amount = getPerson().getMengeBisNaechsteAbzahlung();
        area.setText(text + GeldFormat.geldToStr(amount, true));
    }

    private void updateGesamtSchulden() {
        JTextArea area = getGesamtSchulden();
        String text = "Gesamtschulden: ";
        int amount = getPerson().getRestSchulden();
        area.setText(text + GeldFormat.geldToStr(amount, true));
    }

    private void updateKontoLabel() {
        JLabel label = getGesammelt();
        String text = "Gesammelt: ";
        int konto = getPerson().getKontostand();
        String repr = GeldFormat.geldToStr(konto, true);
        label.setText(text + repr);
    }


    private void customizeFuellen() {
        JTextField field = getAuffuellenFeld();
        field.setMargin(new Insets(0,12,0,0));

        JButton button = getFuellenButton();
        button.addActionListener((e) -> {
            String geld = field.getText().strip();
            if(!GeldFormat.isValidMoney(geld) || GeldFormat.toGeld(geld) > 10000) {
                field.setBackground(Color.RED);
                return;
            }
            int amount = GeldFormat.toGeld(geld);
            field.setBackground(Color.WHITE);
            field.setText("");
            getPerson().fuelleKonto(amount);
            fillPersonData();
        });
    }

    // ------------------------------- Customizing ---------------------------------

    private void customizeLog() {
    }


    private void customizeAdd() {
        JButton add = getAdd();
        add.setFocusable(false);
        add.addActionListener((e) -> {
            AddPopUp popup = new AddPopUp(getFrame());
            if(!popup.getResult()) return;
            getPerson().addVerschuldung(popup.getGrund(), popup.getBetrag());
            fillPersonData();
        });
    }

    private void customizeErgaenzen() {
        JButton button = getErgaenzenButton();
        button.setFocusable(false);
        button.addActionListener((e) -> {
            int geld = getPerson().getMengeBisNaechsteAbzahlung();
            getPerson().fuelleKonto(geld);
            fillPersonData();
        });
    }

    private void customizeSave() {
        JButton save = getSave();
        save.setFocusable(false);
        save.addActionListener((e) -> {
            if(editing) {
                getInfo().setText("Übernehme die Einstellungen");
                return;
            }
            int pos = getPM().getAlleProfile().findePositionNach(getAusgangsDaten(), Profilliste.Sortierung.ABC);
            System.out.println("size: "+ getPM().getAlleProfile().size());
            System.out.println(pos);
            saveUndBack();
            pos = getPM().getAlleProfile().findePositionNach(getPerson().getBeschreibung(), Profilliste.Sortierung.ABC);
            System.out.println("size: "+ getPM().getAlleProfile().size());
            System.out.println(pos);
        });
    }

    private void customizeResets() {
        getInfo().setText("");
        JButton reset = getReset();
        reset.setFocusable(false);
        reset.setBackground(Color.BLUE);
        reset.addActionListener((e) -> {
            getVorname().setText(getPerson().getVorname());
            getNachname().setText(getPerson().getNachname());
            getInfo().setText("");
        });
    }


    private void customizeEditing() {
        JButton edit = getEdit();
        edit.setFocusable(false);
        edit.addActionListener( (e) -> {
            if(!editing)
                edit(edit);
            else
                takeName(edit);
        });
    }

    private void takeName(JButton edit) {
        if(!validateNames()){
            warneSpeichernFehlgeschlagen();
            return;
        }
        edit.setText("edit");
        getVorname().setEnabled(false);
        getNachname().setEnabled(false);
        getInfo().setText("");
        editing = false;
    }

    private void edit(JButton edit) {
        edit.setText("übernehme");
        getVorname().setEnabled(true);
        getNachname().setEnabled(true);
        editing = true;
    }


    private void customizeDelete() {
        JButton delete = getDelete();
        delete.setFocusable(false);
        delete.addActionListener((e) -> {
            DeleteAssure ds = new DeleteAssure(getFrame());
            ds.setVisible(true);
            if(ds.getResult())
                deleteUndBack();
        });
    }


    private void customizeNamen() {
        JTextField vor = getVorname();
        JTextField nach = getNachname();

        vor.setText(getPerson().getVorname());
        vor.setToolTipText("Vorname");
        vor.setDisabledTextColor(Color.GRAY);
        vor.setMargin(new Insets(0,5,0,0));

        nach.setToolTipText("Nachname");
        nach.setText(getPerson().getNachname());
        nach.setDisabledTextColor(Color.GRAY);
        nach.setMargin(new Insets(0,5,0,0));

        vor.setEnabled(false);
        nach.setEnabled(false);
    }


    private void customizeBack() {
        JButton back = getBack();
        back.addActionListener((e) -> resetUndBack());
    }


}
