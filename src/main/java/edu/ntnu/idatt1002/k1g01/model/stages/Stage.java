package edu.ntnu.idatt1002.k1g01.model.stages;

import edu.ntnu.idatt1002.k1g01.model.Round;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents a Stage in the tournament (Superclass)
 */
public abstract class Stage implements Serializable {

    private ArrayList<Round> rounds; // All the rounds in the specific stage

    /**
     * Initiates a Stage
     * @param rounds an ArrayList of type Round
     */
    public Stage(ArrayList<Round> rounds) {
        this.rounds = rounds;
    }

    /**
     * Get the rounds in the Stage
     * @return rounds an ArrayList of type Round
     */
    public ArrayList<Round> getRounds() {
        return rounds;
    }

    /**
     * Sets the rounds in the Stage
     * @param rounds ArrayList of rounds
     */
    public void setRounds(ArrayList<Round> rounds) {
        this.rounds = rounds;
    }

    /**
     * Abstract method called to make a stage check its internal state.
     */
    public abstract void update();
}
