package edu.ntnu.idatt1002.k1g01;

import java.util.ArrayList;

public class GroupStage extends Stage{

    private ArrayList<Group> groups; //Groups partaking in the groupstage.
    private int advancingFromGroup; //Number of teams advancing from group


    public GroupStage(ArrayList<Team> teams,int advancingFromGroup) {
        super(new ArrayList<>());

        this.advancingFromGroup = advancingFromGroup;
        this.groups.addAll(setUpGroups(teams,advancingFromGroup));
    }

    private static ArrayList<Group> setUpGroups(ArrayList<Team> teams,int advancingFromGroup){
        ArrayList<Group> groupList = new ArrayList<>();
        if(advancingFromGroup * 2)
        int teamsPerGroup = advancingFromGroup * 2;
        int amountOfGroups = teams.size()/teamsPerGroup;
        for (int i = 0; i < ; i++) {

        }
    }

    /**
     * Get the teams who will proceed in the tournament.
     * @return the winners.
     */
    public ArrayList<Team> getWinnersFromGroups() { //Need getWinners() method in Group first.
        ArrayList<Team> teamsProceeding = new ArrayList<>();

        for (Group group : groups) {
            teamsProceeding.addAll(group.getTopTeams());
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
     * Checking if the groupstage is finished.
     * @return Returns True if the groupstage is finished and false if it´s not.
     */
    public boolean isFinished() {
        return !getWinnersFromGroups().isEmpty();
    }
}
