package dao;

import db.DBConnection;
import model.Venue;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VenueDAO {

    public boolean insert(Venue venue) {
        String sql = "INSERT INTO Venue (venue_name, location) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, venue.getVenueName());
            ps.setString(2, venue.getLocation());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        venue.setVenueId(rs.getInt(1));
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

    public List<Venue> getAll() {
        List<Venue> venues = new ArrayList<>();
        String sql = "SELECT * FROM Venue";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Venue venue = new Venue();
                venue.setVenueId(rs.getInt("venue_id"));
                venue.setVenueName(rs.getString("venue_name"));
                venue.setLocation(rs.getString("location"));
                venues.add(venue);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return venues;
    }
}