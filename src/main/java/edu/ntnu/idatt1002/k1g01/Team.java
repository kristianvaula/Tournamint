package edu.ntnu.idatt1002.k1g01;


/**
 * Represents a team in the tournament
 */
public class Team {
    // Name of the team
    private String name;

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
}
