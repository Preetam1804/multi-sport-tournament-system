package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.DBConnection;
import model.Player;

public class PlayerDAO {

    public boolean addPlayer(Player player) throws SQLException {
        String query = "INSERT INTO Player (team_id, player_name, jersey_number) VALUES (?, ?, ?)";

        Connection conn = DBConnection.getConnection();
        if (conn == null) {
            throw new SQLException("Failed to establish database connection");
        }

        try {
            PreparedStatement ps = conn.prepareStatement(query);
            try {
                ps.setInt(1, player.getTeamId());
                ps.setString(2, player.getPlayerName());
                ps.setInt(3, player.getJerseyNumber());

                ps.executeUpdate();
                return true;
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

    public List<Player> getAllPlayers() {
        String query = "SELECT * FROM Player";
        List<Player> players = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Player player = new Player();
                player.setPlayerId(rs.getInt("player_id"));
                player.setPlayerName(rs.getString("player_name"));
                player.setTeamId(rs.getInt("team_id"));
                player.setJerseyNumber(rs.getInt("jersey_number"));
                players.add(player);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return players;
    }
}
