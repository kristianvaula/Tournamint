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

    /**
     * Two teams equal each other if
     * they have the same name
     * @param o The team we are comparing to
     * @return True if teams have the same name
     */
    @Override
    public boolean equals(Object o) {
        //First checks in case o is just a TeamHologram. Holograms are all initialized to name = "?" by default.
        if (o.getClass() == TeamHologram.class) {
            if (this == ((TeamHologram) o).getTrueTeam()) return true;
            return this == o;
        }
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
