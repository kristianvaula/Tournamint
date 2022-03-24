package edu.ntnu.idatt1002.k1g01.model.stages;

import edu.ntnu.idatt1002.k1g01.model.Group;
import edu.ntnu.idatt1002.k1g01.model.Round;
import edu.ntnu.idatt1002.k1g01.model.Team;
import edu.ntnu.idatt1002.k1g01.model.matches.Match;

import java.util.ArrayList;
import java.util.Random;

/**
 * Represents a group stage.
 * Initiates groups with teams.
 *
 * @author kristvje
 * @author martnal
 */
public class GroupStage extends Stage {

    //Groups partaking in the groupstage.
    private ArrayList<Group> groups = new ArrayList<>();
    //Number of teams advancing from group
    private int advancingFromGroup;
    //Number of teams per group
    private int teamsPerGroup;
    //Type of matches in group
    private String matchType;

    /**
     * Inititates a new group stage.
     * Calls for the setUpGroups() method
     * that creates the groups.
     * @param teams The teams to enter the group stage
     * @param advancingFromGroup Number of teams advancing from a group
     * @param teamsPerGroup Number of teams per group
     * @param matchType Type of matches in group stage
     */
    public GroupStage(ArrayList<Team> teams, int advancingFromGroup, int teamsPerGroup, String matchType) {
        super(new ArrayList<>());
        this.teamsPerGroup = teamsPerGroup;
        this.matchType = matchType;
        this.advancingFromGroup = advancingFromGroup;
        this.groups.addAll(setUpGroups(teams,advancingFromGroup,teamsPerGroup,matchType));
    }

    /**
     * Sets up groups in groupstage.
     *
     * @param teams The teams to enter the group stage
     * @param teamsPerGroup Number of teams per group
     * @param matchType Type of matches in group stage
     * @return The groups
     */
    private static ArrayList<Group> setUpGroups(ArrayList<Team> teams, int advancingFromGroup,
                                                int teamsPerGroup,String matchType) throws  IllegalArgumentException{
        /*
        TODO remove this old algorithm eventually
        if((teams.size()/teamsPerGroup)%4 != 0){
            throw new IllegalArgumentException("Incompatible number of teams compared to teamsPerGroup");
        }
        */

        //New input verification. Makes sure a power of 2 teams will advance to the finals.
        int advanceToFinals = teams.size()/teamsPerGroup*advancingFromGroup;
        boolean compatible = false;
        for (int i = 1; i < 10; i++) {
            if (advanceToFinals == Math.pow(2, i)) { compatible = true; break; }
        }
        if (!compatible) {
            throw new IllegalArgumentException("Incompatible number of teams advancing to finals: " + advanceToFinals);
        }

        ArrayList<Group> groups = new ArrayList<>();
        ArrayList<Team> teamList = new ArrayList<>(teams);
        Random rand = new Random();

        int amountOfGroups = teams.size()/teamsPerGroup;

        for (int i = 0; i < amountOfGroups; i++) {
            ArrayList<Team> groupTeams = new ArrayList<>();
            for (int j = 0; j < teamsPerGroup; j++) {
                int index = rand.nextInt(teamList.size());
                Team team = teamList.get(index);
                groupTeams.add(team);
                teamList.remove(index);
            }
            groups.add(new Group(matchType,groupTeams));
        }
        return groups;
    }

    /**
     * Get the teams who will proceed in the tournament.
     * @return the winners.
     */
    public ArrayList<Team> getWinnersFromGroups() {
        ArrayList<Team> teamsProceeding = new ArrayList<>();

        for (Group group : groups) {
            teamsProceeding.addAll(group.getTopTeams(advancingFromGroup));
        }
        return teamsProceeding;
    }

    /**
     * Get the groups participating in the groupstage.
     * @return Returns a list of the groups in the groupstage.
     */
    public ArrayList<Group> getGroups() {
        return groups;
    }

    /**
     * Gets the groupstage rounds
     * Returns a list of rounds, where the first
     * round contains all the matches from each groups
     * first round, and so on.
     * @return ArrayList of rounds
     */
    public ArrayList<Round> getGroupRounds(){
        ArrayList<Round> rounds = new ArrayList<>();
        int amountOfRounds = groups.get(0).getRounds().size();
        for (int i = 0; i < amountOfRounds; i++) {
            for (Group group : groups){
                ArrayList<Match> matches = new ArrayList<>(group.getRounds().get(i).getMatches());
                rounds.add(new Round(matches,"GroupRound "+i));
            }
        }
        return rounds;
    }

    /**
     * Checking if the groupstage is finished.
     * @return Returns True if the groupstage is finished and false if it´s not.
     */
    public boolean isFinished() {
        for(Group group : groups){
            if(!group.isFinished()) return false;
        }
        return true;
    }
}
