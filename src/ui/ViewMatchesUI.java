package ui;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import dao.MatchTeamDAO;
import model.MatchTeam;

public class ViewMatchesUI extends JFrame {
    private JTable table;
    private DefaultTableModel model;

    public ViewMatchesUI() {
        setTitle("View Matches");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        String[] columns = {"Match ID", "Team ID", "Score"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        
        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton btnClose = new JButton("Close");
        btnClose.addActionListener(e -> dispose());
        
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(btnClose);
        add(bottomPanel, BorderLayout.SOUTH);

        loadData();
    }

    private void loadData() {
        MatchTeamDAO dao = new MatchTeamDAO();
        List<MatchTeam> matchTeams = dao.getAll();

        for (MatchTeam mt : matchTeams) {
            model.addRow(new Object[]{
                mt.getMatchId(),
                mt.getTeamId(),
                mt.getScore()
            });
        }
    }
}