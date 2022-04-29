package edu.ntnu.idatt1002.k1g01.model;

import edu.ntnu.idatt1002.k1g01.model.matches.Match;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents one round in the tournament
 */
public class Round implements Serializable {

    private ArrayList<Match> matches; // All the current matches being played in the specific round
    private String roundName; // Name of the round. Quarterfinals for example.

    /**
     * Initiates an empty round.
     *
     * @param roundName returns the name of the round.
     */
    public Round(String roundName) {

        this.matches = new ArrayList<>();
        this.roundName = roundName;
    }

    /**
     * Initiates a round.
     *
     * @param matches returns matches in round.
     * @param roundName returns the name of the round.
     */
    public Round(ArrayList<Match> matches, String roundName) {

        this.matches = matches;
        this.roundName = roundName;
    }

    public String getRoundName() {
        return roundName;
    }

    /**
     * Get the matches in the specific round
     * @return matches
     */
    public ArrayList<Match> getMatches() {
        return matches;
    }

    /**
     * Checks if the round is finished
     * by looping through and checking
     * for unfinished matches
     * @return True if round finished.
     */
    public boolean isFinished(){
        for(Match match : matches){
            if(!match.isFinished()) return false;
        }
        return true;
    }

    /**
     * Gets winners from round. Takes how many should advance per
     * match and gets winners from each match.
     *
     * @param advancingPerMatch How many should advance per match
     * @return Winners from round.
     */
    public ArrayList<Team> getWinners(int advancingPerMatch){
        ArrayList<Team> winners = new ArrayList<>();
        for(Match match : matches){
            winners.addAll(match.getWinners(advancingPerMatch));
        }
        return winners;
    }

    /**
     * Adds a match to the matches list
     *
     * @param match match to add
     */
    public void addMatch(Match match){
        matches.add(match);
    }

    /**
     * Get the integer amount of mathes played in the round.
     * @return Number of matches
     */
    public int amountOfMatches() {
        return matches.size();
    }

    /**
     * Set the matches that are competing in the round.
     *
     * @param matches Matches you want to partake in the Round.
     */
    public void setMatches(ArrayList<Match> matches) {
        this.matches = matches;
    }

    /**
     * Displays matches in the round.
     * @return matches
     */
    @Override
    public String toString() {
        return "Matches in round: " + getMatches();
    }
}
