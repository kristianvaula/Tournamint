package edu.ntnu.idatt1002.k1g01.model;


import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a team in the tournament
 * @author kristvje
 */
public class Team implements Serializable {
    // Name of the team
    protected String name;

    /**
     * Default constructor for inheritance.
     */
    public Team() {}

    /**
     * Initiates a new team
     * @param name Name of the team
     */
    public Team(String name) {
        this.name = name;
    }

    /**
     * Gets name
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Only here to make it easier to handle holograms.
     * TODO look for a way to not need this stupid method.
     * @return this exact same Team.
     */
    public Team getTrueTeam() { return this; }

    @Override
    public String toString() {
        return "Team{" +
                "name='" + name + '\'' +
                '}';
    }
}
