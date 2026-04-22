package dao;

import db.DBConnection;
import model.TournamentSport;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TournamentSportDAO {

    public boolean insert(int tournamentId, int sportId) {
        String sql = "INSERT INTO TournamentSport (tournament_id, sport_id) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, tournamentId);
            ps.setInt(2, sportId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<TournamentSport> getAll() {
        List<TournamentSport> tournamentSports = new ArrayList<>();
        String sql = "SELECT ts.tournament_id, ts.sport_id, t.name as tournament_name, s.sport_name " +
                     "FROM TournamentSport ts " +
                     "JOIN Tournament t ON ts.tournament_id = t.tournament_id " +
                     "JOIN Sport s ON ts.sport_id = s.sport_id";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                TournamentSport ts = new TournamentSport();
                ts.setTournamentId(rs.getInt("tournament_id"));
                ts.setSportId(rs.getInt("sport_id"));
                // Note: tournament_name and sport_name are available in ResultSet but not stored in POJO
                tournamentSports.add(ts);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tournamentSports;
    }
}