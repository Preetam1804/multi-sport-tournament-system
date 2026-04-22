package dao;

import db.DBConnection;
import model.Tournament;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TournamentDAO {

    public boolean insert(Tournament tournament) {
        String sql = "INSERT INTO Tournament (name, start_date, end_date) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, tournament.getName());
            ps.setDate(2, tournament.getStartDate());
            ps.setDate(3, tournament.getEndDate());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        tournament.setTournamentId(rs.getInt(1));
                    }
                }
                return true;
            }
            return false;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Tournament> getAll() {
        List<Tournament> tournaments = new ArrayList<>();
        String sql = "SELECT * FROM Tournament";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Tournament tournament = new Tournament();
                tournament.setTournamentId(rs.getInt("tournament_id"));
                tournament.setName(rs.getString("name"));
                tournament.setStartDate(rs.getDate("start_date"));
                tournament.setEndDate(rs.getDate("end_date"));
                tournaments.add(tournament);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tournaments;
    }
}