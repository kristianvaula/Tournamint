package edu.ntnu.idatt1002.k1g01.model;


import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a team in the tournament
 * @author kristvje
 */
public class Team implements Serializable {
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

    /**
     * Two teams equal each other if
     * they have the same name
     * @param o The team we are comparing to
     * @return True if teams have the same name
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return Objects.equals(name, team.name);
    }

    @Override
    public String toString() {
        return "Team{" +
                "name='" + name + '\'' +
                '}';
    }

    /**
     * Hashes name of team
     * @return The hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
