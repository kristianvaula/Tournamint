package edu.ntnu.idatt1002.k1g01.matches;

import edu.ntnu.idatt1002.k1g01.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.*;

public class PointMatchTest {
    private String teamName1,teamName2;
    private Team team1,team2,team3;
    private ArrayList<Team> teamList;
    private ArrayList<Team> extendedTeamList;
    private PointMatch testMatch;
    private PointMatch multiTeamMatch;

    @BeforeEach
    void setUp() {
        teamName1 = "TeamName1";
        teamName2 = "TeamName2";
        team1 = new Team(teamName1);
        team2 = new Team(teamName2);
        team3 = new Team("Team3");

        teamList = new ArrayList<>();
        teamList.add(team1);
        teamList.add(team2);

        extendedTeamList = new ArrayList<>();
        extendedTeamList.add(team1);
        extendedTeamList.add(team2);
        extendedTeamList.add(team3);

        testMatch = new PointMatch(teamList);
        multiTeamMatch = new PointMatch(extendedTeamList);
    }

    @Test
    void getWinners() {
        testMatch.setResult(team1,"3");
        testMatch.setResult(team2,"1");

        Team winner = testMatch.getWinners(1).get(0);

        assertEquals(team1,winner);
    }

    @Test
    void getParticipants() {
        ArrayList<Team> participants = testMatch.getParticipants();

        assertTrue(participants.contains(team1)
                        && participants.contains(team2));
    }

    @Test
    void setAndGetStartTimeAsString() {
        LocalTime time = LocalTime.of(20,15);

        testMatch.setStartTime(time);
        System.out.println(time.toString());
        assertEquals(time.toString(),testMatch.getStartTimeAsString());
    }

    @Test
    void SetAndGetMatchDateAsString() {
        LocalDate date = LocalDate.of(2020,1,14);
        String outputTest = "" + date.getDayOfMonth() + " / " + date.getMonth();

        testMatch.setMatchDate(date);

        assertEquals(outputTest,testMatch.getMatchDateAsString());
    }

    @Test
    void SetAndGetMatchInfo() {
        String info = "The game takes place at Field 8";

        testMatch.setMatchInfo(info);

        assertEquals(info,testMatch.getMatchInfo());
    }

    @Test
    void getMatchResult() {
        testMatch.setResult(team1,"3");
        testMatch.setResult(team2,"1");

        HashMap<Team,String> results = testMatch.getMatchResult();

        assertEquals("3",results.get(team1));
    }

    @Test
    void getMatchResultWithoutResult() {
        PointMatch testMatch2 = new PointMatch(teamList);

        HashMap<Team,String> results = testMatch.getMatchResult();

        assertEquals(true,results.isEmpty());
    }

    @Test
    void getMatchResultOrdered() {
        multiTeamMatch.setResult(team1,"3");
        multiTeamMatch.setResult(team2,"5");
        multiTeamMatch.setResult(team3,"1");

        LinkedHashMap<Team,String> ordered = multiTeamMatch.getMatchResultOrdered();
        ArrayList<Team> teamsOrdered = new ArrayList<>(ordered.keySet());

        assertEquals(team2,teamsOrdered.get(0));
    }

    @Test
    void getMatchResultByTeam() {
        testMatch.setResult(team1,"3");
        testMatch.setResult(team2,"1");

        String resultTeam1 = testMatch.getMatchResultByTeam(team1);

        assertEquals("3",resultTeam1);
    }

    @Test
    void updateIsFinished(){
        PointMatch testMatch2 = new PointMatch(teamList);
        boolean before = testMatch2.isFinished();

        testMatch2.setResult(team1,"3");
        testMatch2.setResult(team2,"1");
        boolean after = testMatch2.isFinished();

        assertTrue(before == false && after == true);

    }
}