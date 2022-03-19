package edu.ntnu.idatt1002.k1g01;

import edu.ntnu.idatt1002.k1g01.matches.Match;
import edu.ntnu.idatt1002.k1g01.matches.PointMatch;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class KnockoutStageTest {

    private Team team1,team2,team3, team4;
    private ArrayList<Team> teamList1, teamList2;
    private PointMatch testMatch1, testMatch2;

    @Test
    void getRounds() {
        team1 = new Team("TeamName1");
        team2 = new Team("TeamName2");
        team3 = new Team("TeamName3");
        team4 = new Team("TeamName4");

        teamList1 = new ArrayList<>();
        teamList1.add(team1);
        teamList1.add(team2);

        teamList2 = new ArrayList<>();
        teamList2.add(team3);
        teamList2.add(team4);

        testMatch1 = new PointMatch(teamList1);
        testMatch2 = new PointMatch(teamList2);

        ArrayList<Match> matches = new ArrayList<>();
        matches.add(testMatch1);
        matches.add(testMatch2);

        Round round1 = new Round(matches, "SEMIFINAL");
        ArrayList<Round> rounds = new ArrayList<>();
        rounds.add(round1);
        KnockoutStage knockoutStage = new KnockoutStage(rounds);

        assertEquals(rounds, knockoutStage.getRounds());
    }

    @Test
    void getWinnerFromKnockouts() {
        team1 = new Team("TeamName1");
        team2 = new Team("TeamName2");

        teamList1 = new ArrayList<>();
        teamList1.add(team1);
        teamList1.add(team2);

        testMatch1 = new PointMatch(teamList1);
        testMatch1.setResult(team1,"2");
        testMatch1.setResult(team2,"1");

        ArrayList<Match> matches = new ArrayList<>();
        matches.add(testMatch1);

        Round round1 = new Round(matches, "FINAL");
        ArrayList<Round> rounds = new ArrayList<>();
        rounds.add(round1);
        KnockoutStage knockoutStage = new KnockoutStage(rounds);

        assertEquals(team1, knockoutStage.getWinnerFromKnockouts());
    }

    @Test
    void getRoundWinners() {
        team1 = new Team("TeamName1");
        team2 = new Team("TeamName2");
        team3 = new Team("TeamName3");
        team4 = new Team("TeamName4");

        teamList1 = new ArrayList<>();
        teamList1.add(team1);
        teamList1.add(team2);

        teamList2 = new ArrayList<>();
        teamList2.add(team3);
        teamList2.add(team4);

        testMatch1 = new PointMatch(teamList1);
        testMatch1.setResult(team1,"2");
        testMatch1.setResult(team2,"1");
        testMatch2 = new PointMatch(teamList2);
        testMatch2.setResult(team3,"3");
        testMatch2.setResult(team4,"2");

        ArrayList<Match> matches = new ArrayList<>();
        matches.add(testMatch1);
        matches.add(testMatch2);

        Round round1 = new Round(matches, "SEMIFINAL");
        ArrayList<Round> rounds = new ArrayList<>();
        rounds.add(round1);
        KnockoutStage knockoutStage = new KnockoutStage(rounds);

        ArrayList<Team> winners = new ArrayList<>();
        winners.add(team1);
        winners.add(team3);

        assertEquals(winners, knockoutStage.getRoundWinners(2));
    }

    @Test
    void getRoundName() {
        team1 = new Team("TeamName1");
        team2 = new Team("TeamName2");

        teamList1 = new ArrayList<>();
        teamList1.add(team1);
        teamList1.add(team2);

        testMatch1 = new PointMatch(teamList1);
        testMatch1.setResult(team1,"2");
        testMatch1.setResult(team2,"1");

        ArrayList<Match> matches = new ArrayList<>();
        matches.add(testMatch1);

        Round round1 = new Round(matches, "FINAL");
        ArrayList<Round> rounds = new ArrayList<>();
        rounds.add(round1);

        assertEquals("FINAL", KnockoutStage.getRoundName());
    }

    @Test
    void generateNextRound() {
        team1 = new Team("TeamName1");
        team2 = new Team("TeamName2");
        team3 = new Team("TeamName3");
        team4 = new Team("TeamName4");

        teamList1 = new ArrayList<>();
        teamList1.add(team1);
        teamList1.add(team2);

        teamList2 = new ArrayList<>();
        teamList2.add(team3);
        teamList2.add(team4);

        testMatch1 = new PointMatch(teamList1);
        testMatch1.setResult(team1,"2");
        testMatch1.setResult(team2,"1");
        testMatch2 = new PointMatch(teamList2);
        testMatch2.setResult(team3,"3");
        testMatch2.setResult(team4,"2");

        ArrayList<Match> matches = new ArrayList<>();
        matches.add(testMatch1);
        matches.add(testMatch2);

        Round round1 = new Round(matches, "SEMIFINAL");
        ArrayList<Round> rounds = new ArrayList<>();
        rounds.add(round1);
        KnockoutStage knockoutStage = new KnockoutStage(rounds);

        knockoutStage.generateNextRound(2,2,1,'P');

        assertEquals("FINAL", KnockoutStage.getRoundName());
    }
}