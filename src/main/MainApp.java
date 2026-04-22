package main;

import javax.swing.SwingUtilities;

import ui.MainMenuUI;

public class MainApp {
    public static void main(String[] args) {
        // Standard Swing entry point
        SwingUtilities.invokeLater(() -> {
            new MainMenuUI().setVisible(true);
        });
    }
}