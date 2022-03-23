package edu.ntnu.idatt1002.k1g01.model;

import edu.ntnu.idatt1002.k1g01.model.stages.GroupStage;
import edu.ntnu.idatt1002.k1g01.model.stages.KnockoutStage;

import java.util.ArrayList;

/**
 * Represents a tournament.
 * <br> Keeps track of all teams and stages.
 * Controls the flow of the tournament and makes
 * the decisions on how the tournament develops
 *
 * @author kristvje
 */
public class Tournament {
    //Name of the tournament
    private String tournamentName;
    //All the teams competing in the tournament
    private ArrayList<Team> teams;
    //Group stage of tournament
    private GroupStage groupStage;
    //Knockout stage of tournament;
    private KnockoutStage knockoutStage;
    //How many teams play each other in a match
    private int teamsPerMatch;
    //What types of matches they play (PointMatch // TimeMatch)
    private String matchType;

    /**
     * Initiates a new tournament.
     * <br>Takes a name, list of teams,
     * and a knockoutstage as arguments.
     * @param tournamentName Name of tournament
     * @param teams Teams to be in tournament
     */
    public Tournament(String tournamentName, ArrayList<Team> teams,String matchType,int teamsPerMatch) throws IllegalArgumentException{
        if(teams.size() < 2) {
            throw new IllegalArgumentException(
                    "Cannot create a tournament with less than two teams");
        }
        this.tournamentName = tournamentName;
        this.teams = teams;
        this.teamsPerMatch = teamsPerMatch;
        this.matchType = matchType;
        this.knockoutStage = new KnockoutStage(teams,teamsPerMatch,matchType);
    }

    /**
     * Initiates a new tournament.
     * <br>Takes a name and a list of
     * teams as arguments.
     * @param tournamentName Name of tournament
     * @param teams Teams to be in tournament
     */
    public Tournament(String tournamentName, ArrayList<Team> teams, String matchType,int teamsPerMatch,int teamsPerGroup,int advancingPerGroup) throws IllegalArgumentException{
        if(teams.size() < 2) {
            throw new IllegalArgumentException(
                    "Cannot create a tournament with less than two teams");
        }
        this.tournamentName = tournamentName;
        this.teams = teams;
        this.groupStage = new GroupStage(teams,advancingPerGroup,teamsPerGroup,matchType);
    }

    /**
     * Gets the tournament name
     * @return The name of the tournament
     */
    public String getTournamentName() {
        return tournamentName;
    }

    /**
     * Sets the TournamentName
     * @param tournamentName String name
     */
    public void setTournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
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
     * Gets the number of teams in the
     * team list
     * @return Number of teams
     */
    public int getNumberOfTeams(){
        return teams.size();
    }

    /**
     * Gets groupStage
     * @return the groupStage
     */
    public GroupStage getGroupStage() {
        return groupStage;
    }

    /**
     * Gets knockoutStage
     * @return the knockoutStage
     */
    public KnockoutStage getKnockoutStage() {
        return knockoutStage;
    }

    /**
     * Updates the tournament.
     * If the groupStage is finished and the
     * knockoutStage has not yet been created,
     * we initiate the knockoutStage. Else we
     * just call for the knockoutStage update
     * method();
     * @throws NoSuchFieldException
     */
    public void updateTournament() throws NoSuchFieldException{
        if(groupStage.isFinished() && knockoutStage == null){
            knockoutStage = new KnockoutStage(groupStage.getWinnersFromGroups(),teamsPerMatch,matchType);
        }else{
            knockoutStage.updateKnockoutStage();
        }
    }

    /**
     * Adds a team to the tournament
     * <br> Checks if team is already in
     * tournament before it adds it to the
     * tournament team list.
     */
    public void addTeam(Team team) throws IllegalArgumentException{
        if(teams.contains(team)){
            throw new IllegalArgumentException(
                    "You cannot add the same team more than once");
        }
        else teams.add(team);
    }

    /**
     * removes a team to the tournament
     * <br> Checks if team is already in
     * tournament before it removes it to the
     * tournament team list.
     */
    public void removeTeam(Team team){
        teams.remove(team);
    }
}
