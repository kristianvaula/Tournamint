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
     *
     * @param teams The teams to enter the group stage
     * @param advancingFromGroup Number of teams advancing from a group
     * @param teamsPerGroup Number of teams per group
     * @param matchType Type of matches in group stage
     */
    public GroupStage(ArrayList<Team> teams, int advancingFromGroup, int teamsPerGroup, String matchType) {
        super(new ArrayList<>());
        if (advancingFromGroup < 1) throw new IllegalArgumentException("At least one team must advance from each group!");
        else if(advancingFromGroup >= teamsPerGroup) throw new IllegalArgumentException("Cannot let " + advancingFromGroup + " teams advance from a group with only " + teamsPerGroup + " teams!");
        this.teamsPerGroup = teamsPerGroup;
        this.matchType = matchType;
        this.advancingFromGroup = advancingFromGroup;
        this.groups.addAll(setUpGroups(new ArrayList<>(teams), advancingFromGroup,teamsPerGroup,matchType));
        for (Group group : groups) {
            if (group.size() <= advancingFromGroup) {
                throw new IllegalArgumentException(advancingFromGroup + " teams advancing from group with only " + group.size() + "teams!");
            }
        }
    }

    /**
     * Randomly splits a list of teams into optimal sub-lists sized no larger than teamsPerGroup.
     *
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
            if (group.size() <= advancingFromGroup) {
                throw new IllegalArgumentException("At least one group with: " + group.size()
                        + " teams and " + advancingFromGroup + " teams advancing!");
            }
        }

        //Following knockoutStage will itself test if it receives compatible number of teams.
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
     * @return Number of teams advancing from each group.
     */
    public int getAdvancingFromGroup() {
        return advancingFromGroup;
    }

    /**
     * Checks if all groups in this groupStage are of size teamsPerGroup.
     * @return
     *      true: If all groups are of same full size
     *      false: If at least one group is missing 1 team.
     */
    public int partialGroupCount() {
        int output = 0;
        for (Group group : groups) {
            if (group.size() < teamsPerGroup) output++;
        }
        return output;
    }

    /**
     * @return size of the smallest group in this groupStage.
     * @author Martin Dammerud
     */
    public int minGroupTeamCount() {
        return groups.stream().mapToInt(Group::size).min().getAsInt();
    }

    /**
     * @return size of the largest group in this groupStage.
     * @author Martin Dammerud
     */
    public int maxGroupTeamCount() {
        return groups.stream().mapToInt(Group::size).max().getAsInt();
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
     * Checking if the groupStage is finished.
     * @return Returns True if the groupStage is finished and false if it??s not.
     */
    public boolean isFinished() {
        for(Group group : groups){
            if(!group.isFinished()) return false;
        }
        return true;
    }
}
