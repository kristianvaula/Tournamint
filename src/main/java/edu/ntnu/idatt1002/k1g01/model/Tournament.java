package edu.ntnu.idatt1002.k1g01.model;

import edu.ntnu.idatt1002.k1g01.model.stages.GroupStage;
import edu.ntnu.idatt1002.k1g01.model.stages.KnockoutStage;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents a tournament.
 * <br> Keeps track of all teams and stages.
 * Controls the flow of the tournament and makes
 * the decisions on how the tournament develops
 *
 * @author kristvje
 */
public class Tournament implements Serializable {
    //Name of the tournament
    private String tournamentName;
    //All the teams competing in the tournament
    private ArrayList<Team> teams;
    //Group stage of tournament
    private GroupStage groupStage;
    //Tells us if tournament has group stage
    private boolean hasGroupStage;
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
     * @param matchType type of match ("pointMatch" / "timeMatch")
     * @param teamsPerMatch teams per match
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
        this.hasGroupStage = false;
        this.knockoutStage = new KnockoutStage(teams,teamsPerMatch,matchType);
    }

    /**
     * Initiates a new tournament.
     * <br>Takes a name and a list of
     * teams as arguments.
     * @param tournamentName Name of tournament
     * @param teams Teams to be in tournament
     * @param matchType type of match ("pointMatch" / "timeMatch")
     * @param teamsPerMatch teams per match
     * @param teamsPerGroup maximum teams per group in groupStage
     * @param advancingPerGroup teams advancing to knockoutStage from each group in groupStage
     */
    public Tournament(String tournamentName, ArrayList<Team> teams, String matchType,int teamsPerMatch,
                      int teamsPerGroup,int advancingPerGroup) throws IllegalArgumentException{
        if(teams.size() < 2) {
            throw new IllegalArgumentException(
                    "Cannot create a tournament with less than two teams");
        }
        this.matchType = matchType;
        this.teamsPerMatch = teamsPerMatch;
        this.tournamentName = tournamentName;
        this.teams = teams;
        this.groupStage = new GroupStage(new ArrayList<>(teams),advancingPerGroup,teamsPerGroup,matchType);
        this.knockoutStage = new KnockoutStage(getTeamHolograms(groupStage), teamsPerMatch, matchType);
        this.hasGroupStage = true;
    }

    /**
     * Generates stand-in teams (holograms) based on a groupStage.
     * @param groupStage A groupStage
     * @return ArrayList of TeamHolograms.
     * @author Martin Dammerud.
     */
    private ArrayList<Team> getTeamHolograms(GroupStage groupStage) {
        ArrayList<Team> holoTeams = new ArrayList<>();
        ArrayList<Group> groups = groupStage.getGroups();
        for (Group group : groups) for (int w = 0; w < groupStage.getAdvancingFromGroup(); w++) {
            holoTeams.add(new TeamHologram(group, w));
        }
        return holoTeams;
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
     * Returns a team based on name
     * @param name Name of team
     * @return Team if exists
     */
    public Team getTeamByName(String name){
        for(Team team : teams){
            if(team.getName().equals(name)) return team;
        }
        return null;
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
     * Gets teams per match
     * @return int teams per match
     */
    public int getTeamsPerMatch() {
        return teamsPerMatch;
    }

    /**
     * Gets match type
     * @return string match type
     */
    public String getMatchType() {
        return matchType;
    }

    /**
     * Has group stage
     * @return true if tournament has group stage
     */
    public boolean hasGroupStage() {
        return hasGroupStage;
    }

    /**
     * Gets groupStage
     * @return the groupStage
     */
    public GroupStage getGroupStage() {
        return groupStage;
    }

    /**
     * Checks if this tournament has a groupStage with partial groups.
     * @return
     *      true: If at least one group is not of full size.
     *      false: If there is no group stage, or group stage has only full groups.
     * @author Martin Dammerud
     */
    public int partialGroupCount() {
        if (groupStage == null) return 0;
        return groupStage.partialGroupCount();
    }

    /**
     * Gets knockoutStage
     * @return the knockoutStage
     */
    public KnockoutStage getKnockoutStage() {
        return knockoutStage;
    }

    /**
     * Gets all rounds generated at the
     * time the method is called
     * @return ArrayList all rounds
     */
    public ArrayList<Round> getAllRounds(){
        if(hasGroupStage){
            if(!groupStage.isFinished()){
                return groupStage.getGroupRounds();
            }
            else if(groupStage.isFinished()){
                ArrayList allRounds = groupStage.getGroupRounds();
                allRounds.addAll(knockoutStage.getRounds());
                return allRounds;
            }
        }
        return knockoutStage.getRounds();

    }

    /**
     * Updates the tournament.
     * If the groupStage is finished and the
     * knockoutStage has not yet been created,
     * we initiate the knockoutStage. Else we
     * just call for the knockoutStage update
     * method();
     */
    public void updateTournament(){
        System.out.println("Updating tournament:");
        System.out.println("    teamsPerMatch: " + teamsPerMatch);
        System.out.println("    matchType: " + matchType);

         if (groupStage != null && knockoutStage == null) {
             if (groupStage.isFinished()) {
                 //Generate knockoutStage from finished groupStage.
                 knockoutStage = new KnockoutStage(groupStage.getWinnersFromGroups(),teamsPerMatch,matchType);
             }
             else {
                 //Updates groupStage. This method currently does nothing.
                 groupStage.update();
             }
         }
         else knockoutStage.update();
    }

    /**
     * Adds a team to the tournament
     * <br> Checks if team is already in
     * tournament before it adds it to the
     * tournament team list.
     * TODO does this actually work? Tournament would need to be re-initialized with altered team list.
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
     * TODO does this actually work? Tournament would need to be re-initialized with altered team list.
     */
    public void removeTeam(Team team){
        teams.remove(team);
    }
}
