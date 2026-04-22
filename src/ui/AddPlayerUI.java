package ui;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import dao.PlayerDAO;
import model.Player;

public class AddPlayerUI extends JFrame {
    public AddPlayerUI() {
        setTitle("Add New Player");
        setSize(350, 250);
        setLayout(new GridLayout(4, 2, 10, 10));
        setLocationRelativeTo(null);

        JLabel lblTeamId = new JLabel(" Team ID:");
        JTextField txtTeamId = new JTextField();
        JLabel lblName = new JLabel(" Player Name:");
        JTextField txtName = new JTextField();
        JLabel lblJersey = new JLabel(" Jersey Number:");
        JTextField txtJersey = new JTextField();

        JButton btnSubmit = new JButton("Submit");
        JButton btnCancel = new JButton("Cancel");

        add(lblTeamId); add(txtTeamId);
        add(lblName); add(txtName);
        add(lblJersey); add(txtJersey);
        add(btnSubmit); add(btnCancel);

        btnSubmit.addActionListener(e -> {
            String teamIdStr = txtTeamId.getText().trim();
            String name = txtName.getText().trim();
            String jerseyStr = txtJersey.getText().trim();

            if (teamIdStr.isEmpty() || name.isEmpty() || jerseyStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            btnSubmit.setEnabled(false);
            btnCancel.setEnabled(false);

            new SwingWorker<Boolean, Void>() {
                @Override
                protected Boolean doInBackground() throws Exception {
                    try {
                        int teamId = Integer.parseInt(teamIdStr);
                        int jersey = Integer.parseInt(jerseyStr);

                        Player player = new Player();
                        player.setTeamId(teamId);
                        player.setPlayerName(name);
                        player.setJerseyNumber(jersey);

                        PlayerDAO dao = new PlayerDAO();
                        return dao.addPlayer(player);
                    } catch (NumberFormatException ex) {
                        throw ex;
                    }
                }

                @Override
                protected void done() {
                    try {
                        if (get()) {
                            JOptionPane.showMessageDialog(AddPlayerUI.this, "Player added successfully!");
                            dispose();
                        } else {
                            JOptionPane.showMessageDialog(AddPlayerUI.this, "Failed to add player to database.", "Error", JOptionPane.ERROR_MESSAGE);
                            btnSubmit.setEnabled(true);
                            btnCancel.setEnabled(true);
                        }
                    } catch (Exception ex) {
                        String errorMsg = getErrorMessage(ex);
                        JOptionPane.showMessageDialog(AddPlayerUI.this, "Error: " + errorMsg, "Database Error", JOptionPane.ERROR_MESSAGE);
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