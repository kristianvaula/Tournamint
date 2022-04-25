package edu.ntnu.idatt1002.k1g01.model;

import edu.ntnu.idatt1002.k1g01.model.Group;
import edu.ntnu.idatt1002.k1g01.model.Team;
import edu.ntnu.idatt1002.k1g01.model.TeamHologram;
import edu.ntnu.idatt1002.k1g01.model.matches.Match;
import edu.ntnu.idatt1002.k1g01.model.matches.PointMatch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

public class TeamHologramTest {
    private static final String defaultName = " - ";
    ArrayList<Team> teams = new ArrayList<>();
    Match match;
    Group group;

    @BeforeEach
    private void init() {
        teams.add(new Team("bilbo"));
        teams.add(new Team("frodo"));
        match = new PointMatch(teams);
        group = new Group("pointMatch", teams);
    }

    @Test
    public void testWithMatchInput1() {
        TeamHologram hTeam = new TeamHologram(match, 0);
        assertEquals(defaultName, hTeam.getName());
        assertNull(hTeam.getTrueTeam());
        match.setResult(teams.get(0), "9001");
        match.setResult(teams.get(1), "0");
        assertEquals(teams.get(0).getName(), hTeam.getName());
        assertEquals(teams.get(0), hTeam.getTrueTeam());
    }

    @Test
    public void testWithMatchInput2() {
        TeamHologram hTeam = new TeamHologram(match, 0);
        assertEquals(defaultName, hTeam.getName());
        assertNull(hTeam.getTrueTeam());
        match.setResult(teams.get(0), "0");
        match.setResult(teams.get(1), "9001");
        assertEquals(teams.get(1).getName(), hTeam.getName());
        assertEquals(teams.get(1), hTeam.getTrueTeam());
    }

    @Test
    public void testWithMatchInput3() {
        TeamHologram hTeam = new TeamHologram(match, 1);
        assertEquals(defaultName, hTeam.getName());
        assertNull(hTeam.getTrueTeam());
        match.setResult(teams.get(0), "9001");
        match.setResult(teams.get(1), "0");
        assertEquals(teams.get(1).getName(), hTeam.getName());
        assertEquals(teams.get(1), hTeam.getTrueTeam());
    }

    @Test
    public void testWithGroupInput1() {
        Group group = new Group("pointMatch", teams);
        TeamHologram hTeam = new TeamHologram(group, 0);
        assertEquals(defaultName, hTeam.getName());
        assertNull(hTeam.getTrueTeam());
        group.getMatches().get(0).setResult(teams.get(0), "9001");
        group.getMatches().get(0).setResult(teams.get(1), "0");
        assertEquals(teams.get(0).getName(), hTeam.getName());
        assertEquals(teams.get(0), hTeam.getTrueTeam());
    }

    @Test
    public void testWithGroupInput2() {
        Group group = new Group("pointMatch", teams);
        TeamHologram hTeam = new TeamHologram(group, 0);
        assertEquals(defaultName, hTeam.getName());
        assertNull(hTeam.getTrueTeam());
        group.getMatches().get(0).setResult(teams.get(0), "0");
        group.getMatches().get(0).setResult(teams.get(1), "9001");
        assertEquals(teams.get(1).getName(), hTeam.getName());
        assertEquals(teams.get(1), hTeam.getTrueTeam());
    }

    @Test
    public void testWithGroupInput3() {
        Group group = new Group("pointMatch", teams);
        TeamHologram hTeam = new TeamHologram(group, 1);
        assertEquals(defaultName, hTeam.getName());
        assertNull(hTeam.getTrueTeam());
        group.getMatches().get(0).setResult(teams.get(0), "9001");
        group.getMatches().get(0).setResult(teams.get(1), "0");
        assertEquals(teams.get(1).getName(), hTeam.getName());
        assertEquals(teams.get(1), hTeam.getTrueTeam());
    }
}
