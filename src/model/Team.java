package model;

public class Team {

    private int teamId;
    private String collegeName;
    private String teamName;
    private String coachName;

    public Team(String collegeName, String teamName, String coachName) {
        this.collegeName = collegeName;
        this.teamName = teamName;
        this.coachName = coachName;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getCoachName() {
        return coachName;
    }

    public void setCoachName(String coachName) {
        this.coachName = coachName;
    }
}