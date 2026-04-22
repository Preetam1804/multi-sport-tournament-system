package ui;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import dao.TeamDAO;
import model.Team;

public class AddTeamUI extends JFrame {
    public AddTeamUI() {
        setTitle("Add New Team");
        setSize(350, 250);
        setLayout(new GridLayout(4, 2, 10, 10));
        setLocationRelativeTo(null);

        JLabel lblCollege = new JLabel(" College Name:");
        JTextField txtCollege = new JTextField();
        JLabel lblTeam = new JLabel(" Team Name:");
        JTextField txtTeam = new JTextField();
        JLabel lblCoach = new JLabel(" Coach Name:");
        JTextField txtCoach = new JTextField();

        JButton btnSubmit = new JButton("Submit");
        JButton btnCancel = new JButton("Cancel");

        add(lblCollege); add(txtCollege);
        add(lblTeam); add(txtTeam);
        add(lblCoach); add(txtCoach);
        add(btnSubmit); add(btnCancel);

        btnSubmit.addActionListener(e -> {
            String college = txtCollege.getText().trim();
            String teamName = txtTeam.getText().trim();
            String coach = txtCoach.getText().trim();

            if (college.isEmpty() || teamName.isEmpty() || coach.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            btnSubmit.setEnabled(false);
            btnCancel.setEnabled(false);

            new SwingWorker<Boolean, Void>() {
                @Override
                protected Boolean doInBackground() throws Exception {
                    Team team = new Team(college, teamName, coach);
                    TeamDAO dao = new TeamDAO();
                    return dao.addTeam(team);
                }

                @Override
                protected void done() {
                    try {
                        if (get()) {
                            JOptionPane.showMessageDialog(AddTeamUI.this, "Team added successfully!");
                            dispose();
                        } else {
                            JOptionPane.showMessageDialog(AddTeamUI.this, "Database insertion failed.", "Error", JOptionPane.ERROR_MESSAGE);
                            btnSubmit.setEnabled(true);
                            btnCancel.setEnabled(true);
                        }
                    } catch (Exception ex) {
                        String errorMsg = getErrorMessage(ex);
                        JOptionPane.showMessageDialog(AddTeamUI.this, "Error: " + errorMsg, "Database Error", JOptionPane.ERROR_MESSAGE);
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