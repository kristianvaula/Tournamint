package edu.ntnu.idatt1002.k1g01;

import edu.ntnu.idatt1002.k1g01.model.Group;
import edu.ntnu.idatt1002.k1g01.model.Round;
import edu.ntnu.idatt1002.k1g01.model.Team;
import edu.ntnu.idatt1002.k1g01.model.matches.Match;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class GroupTest {

    private ArrayList<Team> generateTeams(int n){
        ArrayList<Team> teams = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            int roll = i % 6;
            switch (roll) {
                case 0: { teams.add(new Team("pingas_" + i/6 )); break; }
                case 1: { teams.add(new Team("luigi_" + i/6 )); break; }
                case 2: { teams.add(new Team("princess_" + i/6 )); break; }
                case 3: { teams.add(new Team("maiboi_" + i/6 )); break; }
                case 4: { teams.add(new Team("stinker_" + i/6 )); break; }
                case 5: { teams.add(new Team("frog_" + i/6 )); break; }
            }
        }
        return teams;
    }

    @Test
    @DisplayName("Tests if the Group constructor works with two teams")
    public void CanConstructWith2() {
        Team teamPingas = new Team("Pingas");
        Team teamLuigi = new Team("Luigi");
        Group group = new Group("pointMatch", teamPingas, teamLuigi);
    }

    //TODO Fix constructor to handle arbitrary number of teams.
    @Test
    @DisplayName("Tests that the group constructor works with up to 12 teams")
    public void CanConstructWithUpTo12() {
        for (int i = 3; i < 12; i++) {
            try {
                Group group = new Group("pointMatch", generateTeams(i));
            }
            catch (Exception e) {throw new AssertionError("constructor crashed at: " + i + " teams.");
            }
        }
    }

    @Test
    @DisplayName("Tests the group constructor with timeType matches up to 12 teams")
    public void CanConstructWithTimeTypeMatches() {
        for (int i = 2; i < 12; i++) {
            try {
                Group group = new Group("timeMatch", generateTeams(i));
            }
            catch (Exception e) {throw new AssertionError("constructor crashed at: " + i + " teams.");
            }
        }
    }

    @Test
    @DisplayName("Tests that the Group constructor throws the correct exceptions")
    public void ConstructorTrowsCorrectExceptions() {
        //Initialize teams
        Team teamPingas = new Team("Pingas");
        Team teamLuigi = new Team("Luigi");
        //Perform tests
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            Group group = new Group("pointMatch", teamPingas);
        });
        String expected = "Attempted to create group with " + 1 + " teams. Minimum group size: 2!";
        String actual = exception.getMessage();
        assertEquals(expected, actual);
        exception = assertThrows(IllegalArgumentException.class, () -> {
            Group group = new Group("pointMatch", teamPingas, teamPingas);
        });
        expected = "Attempted to create group with duplicate team entries!";
        actual = exception.getMessage();
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Tests that the constructor generates optimal number of Rounds")
    public void constructorGeneratesOptimalNumberOfRounds() {
        for (int i = 2; i < 12; i++) {
            Group group = new Group("pointMatch", generateTeams(i));
            int expected = i - ((i+1) % 2);
            assertEquals(expected, group.getRounds().size());
        }
    }

    @Test
    @DisplayName("Tests that the contructor generates rounds without duplicate Teams")
    public void constructorGeneratesRoundsWithNoDuplicateTeams() {
        for (int n = 2; n < 12; n++) {
            Group group = new Group("pointMatch", generateTeams(n));
            for (Round round : group.getRounds()) {
                HashSet<Team> teamSet = new HashSet<>();
                ArrayList<Team> teamList = new ArrayList<>();
                for (Match match : round.getMatches()) {
                    teamSet.addAll(match.getParticipants());
                    teamList.addAll(match.getParticipants());
                    assertEquals(teamSet.size(), teamList.size(), "Detected round with duplicate teams in group with "+n+" teams");
                }
            }
        }
    }

    @Test
    @DisplayName("Tests that getTopTeams method throws correct exceptions")
    public void getTopTeamsTrowsCorrectExceptions() {
        Exception exception = assertThrows(IndexOutOfBoundsException.class, () -> {
            Group group = new Group("pointMatch", generateTeams(4));
            group.getTopTeams(5);
        });
        String expected = "Requested top "+5+" teams from group with only "+4+" teams!";
        String actual = exception.getMessage();
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Tests that the getTopTeams method returns null when not finished")
    public void getTopTeamsReturnsNullWhenNotFinished(){
        Group group = new Group("pointMatch", generateTeams(6));
        assertNull(group.getTopTeams(2));
    }

    @Test
    @DisplayName("Tests that the getTopTeam method gets the correct teams")
    public void getTopTeamsGetsCorrectTeams(){
        //Create teams
        Team teamPingas = new Team("Pingas");
        Team teamLuigi = new Team("Luigi");
        Team teamPrincess = new Team("Princess");

        //Pingas Wins
        Group group = new Group("pointMatch", teamPingas, teamLuigi);
        group.getMatches().get(0).setResult(teamPingas, "3");
        group.getMatches().get(0).setResult(teamLuigi, "1");
        assertEquals( teamPingas, group.getTopTeams(1).get(0));

        //Luigu Wins
        group = new Group("pointMatch", teamPingas, teamLuigi);
        group.getMatches().get(0).setResult(teamLuigi, "3");
        group.getMatches().get(0).setResult(teamPingas, "2");
        assertEquals( teamLuigi, group.getTopTeams(1).get(0));

        //Princess Wins, Pingas second Luigi last.
        group = new Group("pointMatch", teamPingas, teamLuigi, teamPrincess);
        for (Match match : group.getMatches()) {
            if(match.getParticipants().contains(teamPrincess)) {
                match.setResult(teamPrincess, "4");
                for (Team team : match.getParticipants()) {
                    if (team != teamPrincess) {
                        match.setResult(team, "1");
                    }
                }
            }
            else {
                match.setResult(teamPingas, "2");
                match.setResult(teamLuigi, "0");
            }
        }
        assertEquals( teamPrincess, group.getTopTeams(3).get(0));
        assertEquals( teamPingas, group.getTopTeams(3).get(1));
        assertEquals( teamLuigi, group.getTopTeams(3).get(2));
    }

    //TODO create test for Group.getStanding. Make sure teams are returned in correct order.
}
