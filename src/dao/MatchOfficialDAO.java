package dao;

import db.DBConnection;
import model.MatchOfficial;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MatchOfficialDAO {

    public boolean insert(int matchId, int officialId) {
        String sql = "INSERT INTO MatchOfficial (match_id, official_id) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, matchId);
            ps.setInt(2, officialId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<MatchOfficial> getAll() {
        List<MatchOfficial> matchOfficials = new ArrayList<>();
        String sql = "SELECT mo.match_id, mo.official_id, o.official_name " +
                     "FROM MatchOfficial mo " +
                     "JOIN Official o ON mo.official_id = o.official_id";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                MatchOfficial mo = new MatchOfficial();
                mo.setMatchId(rs.getInt("match_id"));
                mo.setOfficialId(rs.getInt("official_id"));
                // Note: official_name is available in ResultSet but not stored in POJO
                matchOfficials.add(mo);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return matchOfficials;
    }
}