package dao;

import db.DBConnection;
import model.Sport;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SportDAO {

    public boolean insert(Sport sport) {
        String sql = "INSERT INTO Sport (sport_name) VALUES (?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, sport.getSportName());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        sport.setSportId(rs.getInt(1));
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

    public List<Sport> getAll() {
        List<Sport> sports = new ArrayList<>();
        String sql = "SELECT * FROM Sport";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Sport sport = new Sport();
                sport.setSportId(rs.getInt("sport_id"));
                sport.setSportName(rs.getString("sport_name"));
                sports.add(sport);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sports;
    }
}