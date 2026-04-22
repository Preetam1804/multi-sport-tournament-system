package dao;

import db.DBConnection;
import model.TeamSport;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TeamSportDAO {

    public boolean insert(int teamId, int sportId) {
        String sql = "INSERT INTO TeamSport (team_id, sport_id) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, teamId);
            ps.setInt(2, sportId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<TeamSport> getAll() {
        List<TeamSport> teamSports = new ArrayList<>();
        String sql = "SELECT ts.team_id, ts.sport_id, t.team_name, s.sport_name " +
                     "FROM TeamSport ts " +
                     "JOIN Team t ON ts.team_id = t.team_id " +
                     "JOIN Sport s ON ts.sport_id = s.sport_id";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                TeamSport ts = new TeamSport();
                ts.setTeamId(rs.getInt("team_id"));
                ts.setSportId(rs.getInt("sport_id"));
                // Note: team_name and sport_name are available in ResultSet but not stored in POJO
                teamSports.add(ts);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return teamSports;
    }
}