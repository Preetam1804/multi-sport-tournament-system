package ui;

import java.awt.GridLayout;
import java.sql.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import dao.MatchDAO;
import model.Match;

public class CreateMatchUI extends JFrame {
    public CreateMatchUI() {
        setTitle("Create Match");
        setSize(400, 350);
        setLayout(new GridLayout(6, 2, 10, 10));
        setLocationRelativeTo(null);

        JTextField txtTournId = new JTextField();
        JTextField txtSportId = new JTextField();
        JTextField txtVenueId = new JTextField();
        JTextField txtDate = new JTextField("YYYY-MM-DD");
        JTextField txtRound = new JTextField();

        add(new JLabel(" Tournament ID:")); add(txtTournId);
        add(new JLabel(" Sport ID:")); add(txtSportId);
        add(new JLabel(" Venue ID:")); add(txtVenueId);
        add(new JLabel(" Date (YYYY-MM-DD):")); add(txtDate);
        add(new JLabel(" Round Number:")); add(txtRound);

        JButton btnSubmit = new JButton("Submit");
        JButton btnCancel = new JButton("Cancel");
        add(btnSubmit); add(btnCancel);

        btnSubmit.addActionListener(e -> {
            if (txtTournId.getText().isEmpty() || txtSportId.getText().isEmpty() || 
                txtVenueId.getText().isEmpty() || txtDate.getText().isEmpty() || 
                txtRound.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Required fields are empty!");
                return;
            }

            btnSubmit.setEnabled(false);
            btnCancel.setEnabled(false);

            new SwingWorker<Boolean, Void>() {
                @Override
                protected Boolean doInBackground() throws Exception {
                    try {
                        int tournId = Integer.parseInt(txtTournId.getText());
                        int sportId = Integer.parseInt(txtSportId.getText());
                        int venueId = Integer.parseInt(txtVenueId.getText());
                        Date matchDate = Date.valueOf(txtDate.getText());
                        int round = Integer.parseInt(txtRound.getText());

                        Match match = new Match();
                        match.setTournamentId(tournId);
                        match.setSportId(sportId);
                        match.setVenueId(venueId);
                        match.setMatchDate(matchDate);
                        match.setRoundNumber(round);
                        match.setStatus("Scheduled");

                        MatchDAO dao = new MatchDAO();
                        boolean success = dao.insert(match);

                        return success;
                    } catch (Exception ex) {
                        throw ex;
                    }
                }

                @Override
                protected void done() {
                    try {
                        if (get()) {
                            JOptionPane.showMessageDialog(CreateMatchUI.this, "Match created successfully!");
                            dispose();
                        } else {
                            JOptionPane.showMessageDialog(CreateMatchUI.this, "Failed to create match.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception ex) {
                        String errorMsg = ex.getMessage();
                        JOptionPane.showMessageDialog(CreateMatchUI.this, "Error: " + errorMsg, "Database Error", JOptionPane.ERROR_MESSAGE);
                        btnSubmit.setEnabled(true);
                        btnCancel.setEnabled(true);
                    }
                }
            }.execute();
        });

        btnCancel.addActionListener(e -> dispose());
    }

    private String getErrorMessage(Throwable ex) {
        Throwable cause = ex;
        // Traverse down to the root cause of the exception
        while (cause != null && cause.getCause() != null && cause.getCause() != cause) {
            cause = cause.getCause();
        }

        String message = cause.getMessage();
        if (message != null && !message.isEmpty()) {
            // Attempt to parse specific foreign key error message from MySQL/MariaDB
            // Example: "Cannot add or update a child row: a foreign key constraint fails (`sports_db`.`Matches`, CONSTRAINT `fk_match_tournament` FOREIGN KEY (`tournament_id`) REFERENCES `Tournament` (`tournament_id`))"
            if (message.contains("foreign key constraint fails")) {
                java.util.regex.Pattern p = java.util.regex.Pattern.compile("FOREIGN KEY \\(`(\\w+?)`\\) REFERENCES `(\\w+?)`");
                java.util.regex.Matcher m = p.matcher(message);
                if (m.find()) {
                    String failingColumn = m.group(1); // e.g., "tournament_id"
                    String referencedTable = m.group(2); // e.g., "Tournament"
                    return String.format("Foreign Key Error: The provided %s does not exist in the %s table.", failingColumn.replace("_id", " ID"), referencedTable);
                }
            }
            return message;
        }
        return cause.toString(); // Fallback to the default toString if no message
    }
}