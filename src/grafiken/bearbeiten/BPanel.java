package grafiken.bearbeiten;

import grafiken.GeldFormat;
import grafiken.MainFrame;
import helpers.Profilliste;
import helpers.SaveAssistant;
import users.Personenbeschreibung;
import users.Verschuldung;

import javax.sound.sampled.Line;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class BPanel extends GrafischesBearbeitungsPanel {

    private boolean editing = false;
    private boolean imLog = false;

    public BPanel(MainFrame frame, Personenbeschreibung pb) {
        super(frame, pb);
        customizeComponents();
        fillPersonData();
        SwingUtilities.invokeLater(() -> requestFocusInWindow());
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
        if(vor.equals(getPerson().getVorname()) && nach.equals(getPerson().getNachname()))
            return;
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
        SchuldenAnsicht scroll = SchuldenAnsicht.createAnsicht(getPerson().getSchulden(), eastWidth);
        SwingUtilities.invokeLater(() -> scroll.getVerticalScrollBar().setValue(0));
        initTrashClicks(scroll.getPanels());
        switchPanels(scroll);
        getSwitchButton().setText("Siehe Log");
        imLog = false;
    }

    private void initTrashClicks(ArrayList<SchuldenAnsicht.SchuldPanel> panels) {
        for (SchuldenAnsicht.SchuldPanel p : panels) {
            p.getTrashButton().addActionListener((e) -> {
                getPerson().begleicheOhneKontoEingriff(p.getVerschuldung());
                fillPersonData();
            });
        }
    }


    private void zeigeLog() {
        switchPanels(getLogPanel());
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
        JButton button = getFuellenButton();
        JTextField field = getAuffuellenFeld();

        button.setFocusable(false);
        button.addActionListener((e) -> {
            String geld = field.getText().strip();
            if(!tryFill(geld ,field)) return;
            field.setBackground(Color.WHITE);
            field.setText("");
            this.requestFocusInWindow();
        });

        field.setMargin(new Insets(0,12,0,0));
        field.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(field.getText().strip().length() != 0){
                    button.doClick();
                    return;
                }
                field.setBackground(Color.WHITE);
                field.setText("");
                BPanel.this.requestFocusInWindow();
            }
        });

    }

    private boolean tryFill(String geld, JTextField field) {
        if(!GeldFormat.isValidMoney(geld) || GeldFormat.toGeld(geld) > 10000) {
            field.setBackground(Color.RED);
            return false;
        }
        int amount = GeldFormat.toGeld(geld);
        getPerson().fuelleKonto(amount);
        fillPersonData();
        return true;
    }


    // ------------------------------- Customizing ---------------------------------

    private void customizeLog() {
        JButton log = getSwitchButton();
        log.setFocusable(false);
        log.addActionListener((e) -> {
            if(!imLog) {
                imLog = true;
                log.setText("Siehe Kasse");
                zeigeLog();
                return;
            }
            imLog = false;
            log.setText("Siehe Log");
            zeigeSchulden();
        });
    }


    private JComponent getLogPanel() {
        JTextPane area = new JTextPane();
        area.setBackground(Color.LIGHT_GRAY);
        area.setMargin(new Insets(30,30,0,0));
        area.setEnabled(false);
        area.setSize(eastWidth-20, 10);
        area.setDisabledTextColor(Color.BLACK);
        area.setFont(new Font("arial", Font.PLAIN, 18));
        for(String update : getPerson().getGeschichte()) {
            area.setText(update + "\n\n" + area.getText());
        }
        JScrollPane scroll = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.getViewport().add(area);
        scroll.setBorder(null);
        area.setCaretPosition(0);
        return scroll;
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
        save.setBackground(Color.decode("#008a00"));

        Border inner = new EmptyBorder(7,7,7,7);
        Border outer = BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.GRAY, Color.BLACK);
        Border comp = new CompoundBorder(outer, inner);

        save.setForeground(Color.lightGray);
        save.setBorder(comp);
        save.addActionListener((e) -> {
            if(editing) {
                getInfo().setText("Übernehme die Einstellungen");
                return;
            }
            saveUndBack();
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

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if( e.getKeyCode() == KeyEvent.VK_ESCAPE && back != null)
                    back.doClick();
            }
        });
    }


}
