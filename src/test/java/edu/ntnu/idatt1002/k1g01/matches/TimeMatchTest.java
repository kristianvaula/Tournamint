package edu.ntnu.idatt1002.k1g01.matches;

import edu.ntnu.idatt1002.k1g01.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.*;

public class TimeMatchTest {

    private String teamName1,teamName2;
    private Team team1,team2,team3;
    private ArrayList<Team> teamList;
    private ArrayList<Team> extendedTeamList;
    private TimeMatch testMatch;
    private TimeMatch multiTeamMatch;
    private final String TWO_MINUTES = "00:02:00:0000";
    private final String FOUR_MINUTES = "00:04:00:0000";

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

        testMatch = new TimeMatch(teamList);
        multiTeamMatch = new TimeMatch(extendedTeamList);
    }

    @Test
    void getWinners() {
        testMatch.setResult(team1,TWO_MINUTES);
        testMatch.setResult(team2,FOUR_MINUTES);

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
        testMatch.setResult(team1,TWO_MINUTES);
        testMatch.setResult(team2,FOUR_MINUTES);

        HashMap<Team,String> results = testMatch.getMatchResult();

        assertEquals("00:02:00:00",results.get(team1));
    }

    @Test
    void getMatchResultWithoutResult() {
        PointMatch testMatch2 = new PointMatch(teamList);

        HashMap<Team,String> results = testMatch.getMatchResult();

        assertNull(results);
    }

    @Test
    void getMatchResultOrdered() {
        multiTeamMatch.setResult(team1,TWO_MINUTES);
        multiTeamMatch.setResult(team2,FOUR_MINUTES);
        multiTeamMatch.setResult(team3,"00:01:00:0000");

        LinkedHashMap<Team,String> ordered = multiTeamMatch.getMatchResultOrdered();
        ArrayList<Team> teamsOrdered = new ArrayList<>(ordered.keySet());

        assertEquals(team2,teamsOrdered.get(0));
    }

    @Test
    void getMatchResultByTeam() {
        testMatch.setResult(team1,FOUR_MINUTES);
        testMatch.setResult(team2,TWO_MINUTES);

        String resultTeam1 = testMatch.getMatchResultByTeam(team1);

        assertEquals("00:04:00:00",resultTeam1);
    }

    @Test
    void updateIsFinished(){
        TimeMatch testMatch2 = new TimeMatch(teamList);
        boolean before = testMatch2.isFinished();

        testMatch2.setResult(team1,FOUR_MINUTES);
        testMatch2.setResult(team2,TWO_MINUTES);
        boolean after = testMatch2.isFinished();

        assertTrue(before == false && after == true);

    }
}
