package grafiken.bearbeiten;

import grafiken.GeldFormat;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;


public class AddPopUp extends JDialog {

    private boolean result = false;
    private int btrg;
    private String grnd;

    public AddPopUp(JFrame parent) {
        setModal(true);
        setLocationRelativeTo(parent);
        setSize(300,200);
        setLayout(new BorderLayout());
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        addComponents(panel);
        add(panel, BorderLayout.CENTER);
        setVisible(true);
    }

    private void addComponents(JPanel p) {
        addLabels(p);
        JTextField grund = new JTextField();
        JTextField betrag = new JTextField();
        JButton add = new JButton("add");

        addGrund(p, grund, betrag);
        addBetrag(p, betrag, add);
        addButton(p,grund, betrag, add);
    }

    private void addButton(JPanel p, JTextField grund, JTextField betrag, JButton add) {
        add.addActionListener((e) -> {
            String amount = betrag.getText().strip();
            if(!GeldFormat.isValidMoney(amount) || GeldFormat.toGeld(amount)<=0.0) {
                betrag.setBackground(Color.RED);
                return;
            }
            btrg = GeldFormat.toGeld(amount);
            grnd = grund.getText().strip();
            result = true;
            this.setVisible(false);
        });
        p.add(add);
    }

    private void addBetrag(JPanel p, JTextField betrag, JButton add) {
        JLabel label = new JLabel("Betrag:");
        betrag.setPreferredSize(new Dimension(60,40));
        betrag.setMargin(new Insets(0,10,0,0));
        betrag.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                add.doClick();
            }
        });
        p.add(label);
        p.add(Box.createRigidArea(new Dimension(5,5)));
        p.add(betrag);
        p.add(Box.createRigidArea(new Dimension(5,5)));
    }

    private void addGrund(JPanel p, JTextField grund, JTextField betrag) {
        grund.setPreferredSize(new Dimension(250,40));
        grund.setMargin(new Insets(0,10,0,0));
        grund.requestFocus();
        grund.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                betrag.requestFocus();
            }
        });
        p.add(grund);
        p.add(Box.createRigidArea(new Dimension(5,5)));
    }

    private void addLabels(JPanel p) {
        JLabel label = new JLabel("Schuld erstellen");
        JLabel grund = new JLabel("Grund:");
        p.add(label);
        p.add(Box.createRigidArea(new Dimension(5,5)));
        p.add(grund);
    }


    public boolean getResult() {
        return result;
    }

    public String getGrund() {
        return grnd;
    }

    public int getBetrag() {
        return btrg;
    }

}
