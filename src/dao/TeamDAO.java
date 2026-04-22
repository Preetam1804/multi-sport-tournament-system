package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DBConnection;
import model.Team;

public class TeamDAO {

    public boolean addTeam(Team team) throws SQLException {
        String sql = "INSERT INTO team (college_name, team_name, coach_name) VALUES (?, ?, ?)";

        Connection conn = DBConnection.getConnection();
        if (conn == null) {
            throw new SQLException("Failed to establish database connection");
        }

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            try {
                stmt.setString(1, team.getCollegeName());
                stmt.setString(2, team.getTeamName());
                stmt.setString(3, team.getCoachName());

                int rowsAffected = stmt.executeUpdate();
                return rowsAffected > 0;
            } finally {
                stmt.close();
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

    public List<Team> getAllTeams() {
        List<Team> teams = new ArrayList<>();
        String sql = "SELECT * FROM team";

        try (Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Team team = new Team(
                        rs.getString("college_name"),
                        rs.getString("team_name"),
                        rs.getString("coach_name"));
                team.setTeamId(rs.getInt("team_id"));
                teams.add(team);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return teams;
    }
}
