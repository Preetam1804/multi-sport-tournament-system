package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DBConnection;
import model.Match;


public class MatchDAO {

    public boolean insert(Match match) throws SQLException {
        // Using 'Matches' as the table name based on your database schema
        String sql = "INSERT INTO `Matches` (tournament_id, sport_id, venue_id, match_date, round_number, status) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) {
                throw new SQLException("Failed to establish database connection");
            }

            try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, match.getTournamentId());
            ps.setInt(2, match.getSportId());
            ps.setInt(3, match.getVenueId());
            ps.setDate(4, match.getMatchDate());
            ps.setInt(5, match.getRoundNumber());
            ps.setString(6, match.getStatus());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        match.setMatchId(rs.getInt(1));
                    }
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Database Error: " + e.getMessage());
            throw e;
        }
        }
    }

    public List<Match> getAll() {
        List<Match> matches = new ArrayList<>();
        String sql = "SELECT m.*, s.sport_name, v.venue_name " + 
                     "FROM `Matches` m " +
                     "JOIN Sport s ON m.sport_id = s.sport_id " +
                     "JOIN Venue v ON m.venue_id = v.venue_id";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Match match = new Match();
                match.setMatchId(rs.getInt("match_id"));
                match.setTournamentId(rs.getInt("tournament_id"));
                match.setSportId(rs.getInt("sport_id"));
                match.setVenueId(rs.getInt("venue_id"));
                match.setMatchDate(rs.getDate("match_date"));
                match.setRoundNumber(rs.getInt("round_number"));
                match.setStatus(rs.getString("status"));
                // Note: sport_name and venue_name are available in ResultSet but not stored in POJO
                matches.add(match);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return matches;
    }
}