package edu.ntnu.idatt1002.k1g01;

import edu.ntnu.idatt1002.k1g01.Group;
import edu.ntnu.idatt1002.k1g01.Round;
import edu.ntnu.idatt1002.k1g01.Team;
import edu.ntnu.idatt1002.k1g01.matches.Match;
import edu.ntnu.idatt1002.k1g01.matches.PointMatch;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class Grouptest {

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
    public void CanConstructWith_2() {
        Team teamPingas = new Team("Pingas");
        Team teamLuigi = new Team("Luigi");
        Group group = new Group(PointMatch.class, teamPingas, teamLuigi);
    }
    @Test
    public void CanConstructWith_3() {
        Group group = new Group("point", generateTeams(3));
    }
    @Test
    public void CanConstructWith_4() {
        Group group = new Group("point", generateTeams(4));
    }
    @Test
    public void CanConstructWith_5() {
        Group group = new Group("point", generateTeams(5));
    }
    @Test
    public void CanConstructWith_6() {
        Group group = new Group("point", generateTeams(6));
    }
    @Test
    public void CanConstructWith_10() {
        Group group = new Group("point", generateTeams(10));
    }
    @Test
    public void CanConstructWith_13() {
        Group group = new Group("point", generateTeams(13));
    }
    @Test
    public void CanConstructWith_17() {
        Group group = new Group("point", generateTeams(17));
    }

    @Test
    public void ConstructorTrowsCorrectExceptions() {
        //Initialize teams
        Team teamPingas = new Team("Pingas");
        Team teamLuigi = new Team("Luigi");
        //Perform tests
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            Group group = new Group(PointMatch.class, teamPingas);
        });
        String expected = "Attempted to create group with " + 1 + " teams. Minimum group size: 2!";
        String actual = exception.getMessage();
        assertEquals(expected, actual);
        exception = assertThrows(IllegalArgumentException.class, () -> {
            Group group = new Group(PointMatch.class, teamPingas, teamPingas);
        });
        expected = "Attempted to create group with duplicate team entries!";
        actual = exception.getMessage();
        assertEquals(expected, actual);
    }

    // TODO: Fix Group constructor to make groups of ideal size and not fail this test.
    @Test
    public void constructorGeneratesCorrectNumberOfRounds() {
        Group group = new Group("point", generateTeams(2));
        assertEquals(1, group.getRounds().size());
        group = new Group("point", generateTeams(3));
        assertEquals(3, group.getRounds().size());
        group = new Group("point", generateTeams(4));
        assertEquals(3, group.getRounds().size());
        group = new Group("point", generateTeams(5));
        assertEquals(5, group.getRounds().size());
        group = new Group("point", generateTeams(6));
        System.out.print("here");
        assertEquals(5, group.getRounds().size());
        group = new Group("point", generateTeams(7));
        assertEquals(7, group.getRounds().size());
        group = new Group("point", generateTeams(8));
        assertEquals(7, group.getRounds().size());
    }

    @Test
    public void constructorGeneratesRoundsWithNoDuplicateTeams() {
        for (int n = 2; n < 20; n++) {
            Group group = new Group("point", generateTeams(n));
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
    public void getTopTeamsTrowsCorrectExceptions() {
        Exception exception = assertThrows(IndexOutOfBoundsException.class, () -> {
            Group group = new Group("point", generateTeams(4));
            group.getTopTeams(5);
        });
        String expected = "Requested top "+5+" teams from group with only "+4+" teams!";
        String actual = exception.getMessage();
        assertEquals(expected, actual);
        exception = assertThrows(NoSuchFieldException.class, () -> {
            Group group = new Group("point", generateTeams(4));
            group.getTopTeams(2);
        });
        expected = "Not all matches have finished";
        actual = exception.getMessage();
        assertEquals(expected, actual);
    }

    @Test
    public void getTopTeamsGetsCorrectTeams() throws NoSuchFieldException {
        //Create teams
        Team teamPingas = new Team("Pingas");
        Team teamLuigi = new Team("Luigi");
        Team teamPrincess = new Team("Princess");

        //Pingas Wins
        Group group = new Group(PointMatch.class, teamPingas, teamLuigi);
        group.getMatches().get(0).setResult(teamPingas, "3");
        group.getMatches().get(0).setResult(teamLuigi, "1");
        assertEquals( teamPingas, group.getTopTeams(1).get(0));

        //Luigu Wins
        group = new Group(PointMatch.class, teamPingas, teamLuigi);
        group.getMatches().get(0).setResult(teamLuigi, "3");
        group.getMatches().get(0).setResult(teamPingas, "2");
        assertEquals( teamLuigi, group.getTopTeams(1).get(0));

        //Princess Wins, Pingas second Luigi last.
        group = new Group(PointMatch.class, teamPingas, teamLuigi, teamPrincess);
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
}
