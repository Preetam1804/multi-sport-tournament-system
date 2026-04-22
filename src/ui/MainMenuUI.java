package ui;

import javax.swing.*;
import java.awt.*;

public class MainMenuUI extends JFrame {

    public MainMenuUI() {
        setTitle("Sports Management System - Main Menu");
        setSize(400, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(7, 1, 10, 10));

        JLabel titleLabel = new JLabel("Tournament Management System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(titleLabel);

        JButton btnAddTeam = new JButton("Add Team");
        JButton btnAddPlayer = new JButton("Add Player");
        JButton btnCreateMatch = new JButton("Create Match");
        JButton btnEnterScore = new JButton("Enter Score");
        JButton btnViewMatches = new JButton("View Matches");
        JButton btnExit = new JButton("Exit");

        add(btnAddTeam);
        add(btnAddPlayer);
        add(btnCreateMatch);
        add(btnEnterScore);
        add(btnViewMatches);
        add(btnExit);

        // Action Listeners
        btnAddTeam.addActionListener(e -> new AddTeamUI().setVisible(true));
        btnAddPlayer.addActionListener(e -> new AddPlayerUI().setVisible(true));
        btnCreateMatch.addActionListener(e -> new CreateMatchUI().setVisible(true));
        btnEnterScore.addActionListener(e -> new EnterScoreUI().setVisible(true));
        btnViewMatches.addActionListener(e -> new ViewMatchesUI().setVisible(true));
        btnExit.addActionListener(e -> System.exit(0));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainMenuUI().setVisible(true);
        });
    }
}