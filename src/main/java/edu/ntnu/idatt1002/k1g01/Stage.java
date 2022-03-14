package edu.ntnu.idatt1002.k1g01;

import java.util.ArrayList;

/**
 * Represents a Stage in the tournament (Superclass)
 */
public class Stage {

    private ArrayList<Round> rounds; // All the rounds in the specific stage

    /**
     * Initiates a Stage
     * @param rounds
     */
    public Stage(ArrayList<Round> rounds) {
        this.rounds = rounds;
    }

    /**
     * Get the rounds in the Stage
     * @return rounds
     */
    public ArrayList<Round> getRounds() {
        return rounds;
    }
}
