package edu.ntnu.idatt1002.k1g01.model.stages;

import edu.ntnu.idatt1002.k1g01.model.Group;
import edu.ntnu.idatt1002.k1g01.model.Round;
import edu.ntnu.idatt1002.k1g01.model.Team;
import edu.ntnu.idatt1002.k1g01.model.matches.Match;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toCollection;

/**
 * Represents a group stage.
 * Initiates groups with teams.
 *
 * @author kristvje
 * @author martnal
 * @author espenjus
 * @author martdam
 */
public class GroupStage extends Stage {

    //Groups partaking in the groupStage.
    private ArrayList<Group> groups = new ArrayList<>();
    //Number of teams advancing from group
    private int advancingFromGroup;
    //Number of teams per group
    private int teamsPerGroup;
    //Type of matches in group
    private final String matchType;

    /**
     * Initiates a new group stage.
     * Calls for the setUpGroups() method
     * that creates the groups.
     * @param teams The teams to enter the group stage
     * @param advancingFromGroup Number of teams advancing from a group
     * @param teamsPerGroup Number of teams per group
     * @param matchType Type of matches in group stage
     */
    public GroupStage(ArrayList<Team> teams, int advancingFromGroup, int teamsPerGroup, String matchType) {
        super(new ArrayList<>());
        if (advancingFromGroup < 1 || advancingFromGroup >= teamsPerGroup) throw new IllegalArgumentException("Teams advancing from group must be at least one and maximum the amount of teams in the group");
        this.teamsPerGroup = teamsPerGroup;
        this.matchType = matchType;
        this.advancingFromGroup = advancingFromGroup;
        this.groups.addAll(setUpGroups(new ArrayList<>(teams), advancingFromGroup,teamsPerGroup,matchType));
    }

    /**
     * Randomly splits a list of teams into optimal sub-lists sized no larger than teamsPerGroup.
     * @param teamList ArrayLists of teams.
     * @param teamsPerGroup Maximum number of teams per output sub-list.
     * @return input teams divided evenly between optimal number of sub-lists.
     */
    private static ArrayList<ArrayList<Team>> distributeTeams(ArrayList<Team> teamList, int teamsPerGroup) {
        //Calculate correct number of groups.
        int groupCount;
        int teamCount = teamList.size();
        if (teamList.size()%teamsPerGroup == 0) groupCount = teamCount / teamsPerGroup;
        else groupCount = teamCount / teamsPerGroup + 1;

        //Distribute teams among groupTeamLists as evenly as possible.
        ArrayList<ArrayList<Team>> output = Stream
                .generate(ArrayList<Team>::new).limit(groupCount)
                .collect(toCollection(ArrayList<ArrayList<Team>>::new));
        Random random = new Random();
        for (int n = 0; n < teamCount; n++) {
            output.get(n % groupCount) //cycle through groupTeamLists.
                    .add(teamList.remove(random.nextInt(teamList.size()))); //Add random team from teamList.
        }

        assert (teamList.isEmpty()); //Make sure all teams are transferred. Only done during testing.
        return output;
    }

    /**
     * Sets up groups in groupStage.
     *
     * @param teams The teams to enter the group stage
     * @param teamsPerGroup Number of teams per group
     * @param matchType Type of matches in group stage
     * @return The groups
     */
    private static ArrayList<Group> setUpGroups(ArrayList<Team> teams, int advancingFromGroup,
                                                int teamsPerGroup,String matchType) throws  IllegalArgumentException{

        //Distribute teams into groups of size teamsPerGroup, or teamsPerGroup-1 if there are too few teams to fill all groups.
        ArrayList<Group> groups = distributeTeams(teams, teamsPerGroup).stream()
                .map(g -> new Group(matchType, g))
                .collect(toCollection(ArrayList<Group>::new));

        //Verify that all groups are valid.
        for (Group group : groups) {
            if (group.getTeamCount() <= advancingFromGroup) {
                throw new IllegalArgumentException("Invalid group! At least one group of size: " + group.getTeamCount()
                        + " with " + advancingFromGroup + " advancing.");
            }
        }

        //Makes sure a power of 2 teams will advance to the knockoutStage.
        int advanceToFinals = groups.size()*advancingFromGroup;
        boolean compatible = false;
        for (int i = 1; i < 10; i++) {
            if (advanceToFinals == Math.pow(2, i)) { compatible = true; break; }
        }
        if (!compatible) {
            throw new IllegalArgumentException("Incompatible number of teams advancing to finals: " + advanceToFinals);
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
     * Get the groups participating in the groupStage.
     * @return Returns a list of the groups in the groupStage.
     */
    public ArrayList<Group> getGroups() {
        return groups;
    }

    /**
     * Checks if all groups in this groupStage are of size teamsPerGroup.
     * @return
     *      true: If all groups are of same full size
     *      false: If at least one group is missing 1 team.
     */
    public boolean hasPartialGroups() {
        for (Group group : groups) {
            if (group.getTeamCount() != teamsPerGroup) return true;
        }
        return false;
    }

    /**
     * Gets the groupStage rounds
     * Returns a list of rounds, where the first
     * round contains all the matches from each group
     * first round, and so on.
     * @return ArrayList of rounds
     */
    public ArrayList<Round> getGroupRounds(){
        ArrayList<Round> rounds = new ArrayList<>();
        int amountOfRounds = groups.stream().mapToInt(Group::getRoundCount).max().getAsInt();
        for (int i = 0; i < amountOfRounds; i++) {
            for (Group group : groups){
                if (group.getRoundCount() > i) { //Some groups may not be at full size, and have fewer rounds.
                    ArrayList<Match> matches = new ArrayList<>(group.getRounds().get(i).getMatches());
                    rounds.add(new Round(matches,"GroupRound "+i));
                }

            }
        }
        return rounds;
    }

    /**
     * Implementation of abstract method in Stage.
     * Currently, does nothing in this class.
     * Flesh out if something needs to be updated between matches.
     */
    public void update() {}

    /**
     * Checking if the groupStage is finished.
     * @return Returns True if the groupStage is finished and false if it´s not.
     */
    public boolean isFinished() {
        for(Group group : groups){
            if(!group.isFinished()) return false;
        }
        return true;
    }
}
