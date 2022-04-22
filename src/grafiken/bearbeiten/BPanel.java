package grafiken.bearbeiten;

import grafiken.MainFrame;
import helpers.SaveAssistant;
import users.Personenbeschreibung;

import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.*;
import java.util.List;

public class BPanel extends GrafischesBearbeitungsPanel {

    public BPanel(MainFrame frame, Personenbeschreibung pb) {
        super(frame, pb);
        customizeComponents();
    }

    @Override
    protected void warneSpeichernFehlgeschlagen() {

    }

    @Override
    protected boolean validateNames() {
        return false;
    }

    @Override
    protected void saveNames() {

    }


    private void customizeComponents() {
        customizeBack();
        customizeDelete();
        customizeNamen();
    }


    private void customizeDelete() {
        JButton delete = getDelete();
        delete.setFocusable(false);
        delete.addActionListener((e) -> {
            DeleteAssure ds = new DeleteAssure(getFrame());
            ds.setVisible(true);
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
