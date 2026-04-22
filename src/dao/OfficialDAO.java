package dao;

import db.DBConnection;
import model.Official;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OfficialDAO {

    public boolean insert(Official official) {
        String sql = "INSERT INTO Official (official_name, experience_level) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, official.getOfficialName());
            ps.setString(2, official.getExperienceLevel());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        official.setOfficialId(rs.getInt(1));
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

    public List<Official> getAll() {
        List<Official> officials = new ArrayList<>();
        String sql = "SELECT * FROM Official";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Official official = new Official();
                official.setOfficialId(rs.getInt("official_id"));
                official.setOfficialName(rs.getString("official_name"));
                official.setExperienceLevel(rs.getString("experience_level"));
                officials.add(official);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return officials;
    }
}