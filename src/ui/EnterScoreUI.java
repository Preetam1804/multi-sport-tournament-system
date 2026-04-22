package ui;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import dao.MatchTeamDAO;

public class EnterScoreUI extends JFrame {
    public EnterScoreUI() {
        setTitle("Enter Match Score");
        setSize(300, 200);
        setLayout(new GridLayout(4, 2, 10, 10));
        setLocationRelativeTo(null);

        JTextField txtMatchId = new JTextField();
        JTextField txtTeamId = new JTextField();
        JTextField txtScore = new JTextField();

        add(new JLabel(" Match ID:")); add(txtMatchId);
        add(new JLabel(" Team ID:")); add(txtTeamId);
        add(new JLabel(" Score:")); add(txtScore);

        JButton btnSubmit = new JButton("Submit");
        JButton btnCancel = new JButton("Cancel");
        add(btnSubmit); add(btnCancel);

        btnSubmit.addActionListener(e -> {
            if (txtMatchId.getText().isEmpty() || txtTeamId.getText().isEmpty() || 
                txtScore.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required!");
                return;
            }

            btnSubmit.setEnabled(false);
            btnCancel.setEnabled(false);

            new SwingWorker<Boolean, Void>() {
                @Override
                protected Boolean doInBackground() throws Exception {
                    try {
                        int matchId = Integer.parseInt(txtMatchId.getText());
                        int teamId = Integer.parseInt(txtTeamId.getText());
                        int score = Integer.parseInt(txtScore.getText());

                        MatchTeamDAO dao = new MatchTeamDAO();
                        return dao.insert(matchId, teamId, score);
                    } catch (NumberFormatException ex) {
                        throw ex;
                    }
                }

                @Override
                protected void done() {
                    try {
                        if (get()) {
                            JOptionPane.showMessageDialog(EnterScoreUI.this, "Score recorded successfully!");
                            dispose();
                        } else {
                            JOptionPane.showMessageDialog(EnterScoreUI.this, "Update failed.", "Error", JOptionPane.ERROR_MESSAGE);
                            btnSubmit.setEnabled(true);
                            btnCancel.setEnabled(true);
                        }
                    } catch (Exception ex) {
                        String errorMsg = getErrorMessage(ex);
                        JOptionPane.showMessageDialog(EnterScoreUI.this, "Error: " + errorMsg, "Database Error", JOptionPane.ERROR_MESSAGE);
                        System.err.println("Full error: " + ex);
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
            if (message.contains("foreign key constraint fails")) {
                java.util.regex.Pattern p = java.util.regex.Pattern.compile("FOREIGN KEY \\(`(\\w+?)`\\) REFERENCES `(\\w+?)`");
                java.util.regex.Matcher m = p.matcher(message);
                if (m.find()) {
                    String failingColumn = m.group(1);
                    String referencedTable = m.group(2);
                    return String.format("Foreign Key Error: The provided %s does not exist in the %s table.", failingColumn.replace("_id", " ID"), referencedTable);
                }
            }
            return message;
        }
        return cause.toString();
    }
}