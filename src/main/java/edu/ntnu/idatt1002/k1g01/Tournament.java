package edu.ntnu.idatt1002.k1g01;

import java.util.ArrayList;

/**
 * Represents a tournament.
 * <br> Keeps track of all teams and stages.
 * Controls the flow of the tournament and makes
 * the decisions on how the tournament develops
 */
public class Tournament {
    //Name of the tournament
    private String tournamentName;
    //All the teams competing in the tournament
    private ArrayList<Team> teams;

    /**
     * Initiates a new tournament.
     * <br>Takes a name and a list of
     * teams as arguments.
     * @param tournamentName Name of tournament
     * @param teams Teams to be in tournament
     */
    public Tournament(String tournamentName, ArrayList<Team> teams) {
        this.tournamentName = tournamentName;
        this.teams = teams;
    }

    /**
     * Gets the tournament name
     * @return The name of the tournament
     */
    public String getTournamentName() {
        return tournamentName;
    }

    /**
     * Gets a copy of the teams list
     * of the tournament
     * @return copy of team list
     */
    public ArrayList<Team> getTeams() {
        return new ArrayList<>(teams);
    }

    /**
     * Adds a team to the tournament
     * <br> Checks if team is already in
     * tournament before it adds it to the
     * tournament team list.
     * @return True if team added to list.
     */
    public boolean addTeam(Team team){
        if(!teams.contains(team)){
            teams.add(team);
            return true;
        }
        return false;
    }
}
