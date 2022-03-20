package edu.ntnu.idatt1002.k1g01;

import java.util.ArrayList;

public class GroupStage extends Stage{

    private ArrayList<Group> groups; //Groups partaking in the groupstage.

    /**
     * Initiates a Stage
     *
     * @param rounds an ArrayList of type Round
     */
    public GroupStage(ArrayList<Round> rounds) {
        super(rounds);
    }

    /**
     * Get the teams who will proceed in the tournament.
     * @return the winners.
     */
    public ArrayList<Team> getWinnersFromGroups() { //Need getWinners() method in Group first.
        ArrayList<Team> teamsProceeding = new ArrayList<>();
        //Group group = new Group();

        for (Group group1 : groups) {
            //teamsProceeding.addAll(group.getWinners());
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
