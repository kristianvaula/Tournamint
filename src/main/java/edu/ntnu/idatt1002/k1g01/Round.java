package edu.ntnu.idatt1002.k1g01;

import java.util.ArrayList;

/**
 * Represents one round in the tournament
 */
public class Round {

    private ArrayList<Match> matches; // All the current matches being played in the specific round

    /**
     * Initiates a round
     * @param matches Matches in the round
     */
    public Round(ArrayList<Match> matches) {
        this.matches = matches;
    }

    /**
     * Get the matches in the specific round
     * @return matches
     */
    public ArrayList<Match> getMatches() {
        return matches;
    }

    @Override
    public String toString() {
        return "Matches in round: " + getMatches();
    }
}
