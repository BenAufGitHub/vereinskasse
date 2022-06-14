package grafiken.erstellen;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class InputFormPanel extends JPanel {

    protected final String namenRegEx = "\\b([A-ZÀ-ÿ][-,a-z. 'äöüß]+[ ]*)+";

    private JTextField vorname = null;
    private JTextField nachname = null;
    private JLabel hint = null;
    private JTextArea nameError = null;
    private FormListener listener = null;

    public InputFormPanel() {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBorder(new EmptyBorder(6,25,0,0));
        setOpaque(false);

        JLabel label = getLabel();
        hint = new JLabel();
        hint.setAlignmentX(Component.LEFT_ALIGNMENT);
        hint.setForeground(Color.WHITE);
        JTextField vorname = getVornameField();
        JTextField nachname = getNachnameField();

        nameError = getBaseErrorText();

        add(Box.createRigidArea(new Dimension(1,12)));
        add(label);
        add(Box.createRigidArea(new Dimension(1,5)));
        add(hint);
        add(Box.createRigidArea(new Dimension(1,8)));
        add(vorname);
        add(Box.createRigidArea(new Dimension(1,5)));
        add(nachname);
        add(Box.createRigidArea(new Dimension(1,20)));
        add(nameError);
    }

    private JTextArea getBaseErrorText() {
        JTextArea text = new JTextArea();
        text.setText("Name ist nicht im korrekten Format. \n" +
                "Erlaubt sind Doppelnamen, deutsches Alphabet...\n" +
                "Nicht erlaubt: Großschreiben im Wort, Leerfelder und Sonderzeichen wie &/({# etc.");
        text.setPreferredSize(new Dimension(200, 100));
        text.setForeground(Color.RED);
        text.setOpaque(false);
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        text.setEditable(false);
        text.setAlignmentX(Component.LEFT_ALIGNMENT);
        text.setVisible(false);
        return text;
    }


    private JTextField getNachnameField() {
        nachname = new JTextField();
        nachname.setAlignmentX(Component.LEFT_ALIGNMENT);
        nachname.setPreferredSize(new Dimension(330,45));
        nachname.setMaximumSize(new Dimension(330,45));
        nachname.setMargin(new Insets(2,6,2,2));
        nachname.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                hint.setText("Gebe den Nachnamen an");
            }
        });
        nachname.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(listener != null)
                    listener.onFormFinish();
            }
        });
        return nachname;
    }


    private JTextField getVornameField() {
        vorname = new JTextField();
        vorname.setAlignmentX(Component.LEFT_ALIGNMENT);
        vorname.setPreferredSize(new Dimension(330,45));
        vorname.setMaximumSize(new Dimension(330,45));
        vorname.setMargin(new Insets(2,6,2,2));
        vorname.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                hint.setText("Gib den Vornamen an");
            }
        });
        vorname.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                transferFocusTo(nachname);
            }
        });
        SwingUtilities.invokeLater(() ->  vorname.requestFocusInWindow());
        return vorname;
    }

    public void setFormLister(FormListener listener) {
        this.listener = listener;
    }

    private void transferFocusTo(JComponent comp) {
        SwingUtilities.invokeLater(() -> comp.requestFocusInWindow());
    }

    private JLabel getLabel() {
        JLabel label = new JLabel();
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setBorder(null);
        label.setText("Erstellen");
        label.setForeground(Color.WHITE);
        label.setFont(new Font("arial", Font.BOLD, 20));
        return label;
    }

    public String getVorname() {
        return vorname.getText().strip();
    }

    public String getNachname() {
        return nachname.getText().strip();
    }

    public JTextField getVorTextfield(){
        return vorname;
    }

    public JTextField getNachTextfield(){
        return nachname;
    }


    public boolean isRegExApproved() {
        String vor = getVorname();
        String nach = getNachname();
        return vor.matches(namenRegEx) && nach.matches(namenRegEx);
    }

    public void showNameError() {
        nameError.setVisible(true);
    }
}
