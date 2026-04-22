package dao;

import db.DBConnection;
import model.PlayerMatchStats;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerMatchStatsDAO {

    public boolean insert(int matchId, int playerId, int teamId, int fouls) {
        String sql = "INSERT INTO PlayerMatchStats (match_id, player_id, team_id, fouls) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, matchId);
            ps.setInt(2, playerId);
            ps.setInt(3, teamId);
            ps.setInt(4, fouls);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<PlayerMatchStats> getAll() {
        List<PlayerMatchStats> stats = new ArrayList<>();
        String sql = "SELECT pms.match_id, pms.player_id, pms.team_id, pms.fouls, p.player_name, t.team_name " +
                     "FROM PlayerMatchStats pms " +
                     "JOIN Player p ON pms.player_id = p.player_id " +
                     "JOIN Team t ON pms.team_id = t.team_id";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                PlayerMatchStats pms = new PlayerMatchStats();
                pms.setMatchId(rs.getInt("match_id"));
                pms.setPlayerId(rs.getInt("player_id"));
                pms.setTeamId(rs.getInt("team_id"));
                pms.setFouls(rs.getInt("fouls"));
                // Note: player_name and team_name are available in ResultSet but not stored in POJO
                stats.add(pms);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stats;
    }
}