package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.DBConnection;
import model.MatchTeam;

public class MatchTeamDAO {

    public boolean insert(int matchId, int teamId, int score) throws SQLException {
        String sql = "INSERT INTO MatchTeam (match_id, team_id, score) VALUES (?, ?, ?)";

        Connection conn = DBConnection.getConnection();
        if (conn == null) {
            throw new SQLException("Failed to establish database connection");
        }

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            try {
                ps.setInt(1, matchId);
                ps.setInt(2, teamId);
                ps.setInt(3, score);

                int rowsAffected = ps.executeUpdate();
                return rowsAffected > 0;
            } finally {
                ps.close();
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
            throw e;
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    public List<MatchTeam> getAll() {
        List<MatchTeam> matchTeams = new ArrayList<>();
        String sql = "SELECT mt.match_id, mt.team_id, mt.score, t.team_name " +
                     "FROM MatchTeam mt " +
                     "JOIN Team t ON mt.team_id = t.team_id";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                MatchTeam mt = new MatchTeam();
                mt.setMatchId(rs.getInt("match_id"));
                mt.setTeamId(rs.getInt("team_id"));
                mt.setScore(rs.getInt("score"));
                // Note: team_name is available in ResultSet but not stored in POJO
                matchTeams.add(mt);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return matchTeams;
    }
}