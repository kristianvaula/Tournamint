package edu.ntnu.idatt1002.k1g01;

import java.util.ArrayList;

public class GroupStage {

    private ArrayList<Group> groups; //Groups partaking in the groupstage.

    /**
     * Get the teams who will proceed in the tournament.
     * @return the winners.
     */
    public ArrayList<Team> getWinnersFromGroups() {
        ArrayList<Team> teamsProceeding = new ArrayList<>();
        //for (Group group : groups) {
        //    teamsProceeding.addAll(); (Add all groupwinners to the list).
        //}
        return teamsProceeding;
    }

    /**
     * Checking if the groupstage is finished.
     * @return true/false
     */
    public boolean isFinished() {
        return true; //Code not finished.
    }
}
