package edu.ntnu.idatt1002.k1g01.model.matches;

import edu.ntnu.idatt1002.k1g01.model.Group;
import edu.ntnu.idatt1002.k1g01.model.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
    @DisplayName("Tests the getWinners method")
    void getWinners() {
        testMatch.setResult(team1,"3");
        testMatch.setResult(team2,"1");

        Team winner = testMatch.getWinners(1).get(0);

        assertEquals(team1,winner);
    }

    @Test
    @DisplayName("Tests the getParticipants method")
    void getParticipants() {
        ArrayList<Team> participants = testMatch.getParticipants();

        assertTrue(participants.contains(team1)
                        && participants.contains(team2));
    }

    @Test
    @DisplayName("Tests the Set and Get methods for the time of a match to String")
    void setAndGetStartTimeAsString() {
        LocalTime time = LocalTime.of(20,15);

        testMatch.setStartTime(time);
        System.out.println(time.toString());
        assertEquals(time.toString(),testMatch.getStartTimeAsString());
    }

    @Test
    @DisplayName("Tests the Set and Get methods for the date of a match to String")
    void SetAndGetMatchDateAsString() {
        LocalDate date = LocalDate.of(2020,1,14);
        String outputTest = "" + date.getDayOfMonth() + " / " + date.getMonth();

        testMatch.setMatchDate(date);

        assertEquals(outputTest,testMatch.getMatchDateAsString());
    }

    @Test
    @DisplayName("Tests the Set and Get methods for MatchInfo")
    void SetAndGetMatchInfo() {
        String info = "The game takes place at Field 8";

        testMatch.setMatchInfo(info);

        assertEquals(info,testMatch.getMatchInfo());
    }

    @Test
    @DisplayName("Tests the getMatchResult method")
    void getMatchResult() {
        testMatch.setResult(team1,"3");
        testMatch.setResult(team2,"1");

        HashMap<Team,String> results = testMatch.getMatchResult();

        assertEquals("3",results.get(team1));
    }

    @Test
    @DisplayName("Tests the getMatchResult method when the result is empty")
    void getMatchResultWithoutResult() {
        PointMatch testMatch2 = new PointMatch(teamList);

        HashMap<Team,String> results = testMatch.getMatchResult();

        assertTrue(results.isEmpty());
    }

    @Test
    @DisplayName("Tests the getMatchResultOrdered method")
    void getMatchResultOrdered() {
        multiTeamMatch.setResult(team1,"3");
        multiTeamMatch.setResult(team2,"5");
        multiTeamMatch.setResult(team3,"1");

        LinkedHashMap<Team,String> ordered = multiTeamMatch.getMatchResultOrdered();
        ArrayList<Team> teamsOrdered = new ArrayList<>(ordered.keySet());

        assertEquals(team2,teamsOrdered.get(0));
    }

    @Test
    @DisplayName("Tests the getMatchResultByTeam method")
    void getMatchResultByTeam() {
        testMatch.setResult(team1,"3");
        testMatch.setResult(team2,"1");

        String resultTeam1 = testMatch.getMatchResultByTeam(team1);

        assertEquals("3",resultTeam1);
    }

    @Test
    @DisplayName("Tests the updateIsFinished method")
    void updateIsFinished(){
        PointMatch testMatch2 = new PointMatch(teamList);
        boolean before = testMatch2.isFinished();

        testMatch2.setResult(team1,"3");
        testMatch2.setResult(team2,"1");
        boolean after = testMatch2.isFinished();

        assertTrue(!before && after);

    }

    @Test
    @DisplayName("Test match with TeamHolograms")
    public void worksWithHolograms() {
        String win = "9001";
        String lose = "0";

        //Prepare teams and matches.
        ArrayList<Team> teamsA = new ArrayList<>();
        Team mario = new Team("mario"); teamsA.add(mario);
        Team luigi = new Team("luigi"); teamsA.add(luigi);
        ArrayList<Team> teamsB = new ArrayList<>();
        Team pingas = new Team("pingas"); teamsB.add(pingas);
        Team billy = new Team ("billy"); teamsB.add(billy);
        Match[] matches = new Match[2];
        matches[0] = new PointMatch(teamsA);
        matches[1] = new PointMatch(teamsB);
        Match matchC = new PointMatch(1, matches);

        //Make sure matchC full of holograms can't have results set when no sub-matches are finished.
        assertThrows(ClassCastException.class, () -> {
            matchC.setResult(matchC.getParticipants().get(0), win);
        });

        //Play some contained matches.
        matches[0].setResult(mario, win);
        matches[0].setResult(luigi, lose);
        matches[1].setResult(pingas, win);

        //Make sure matchC full of holograms can't have results set when one sub-match is not finished.
        assertThrows(ClassCastException.class, () -> {
            matchC.setResult(matchC.getParticipants().get(0), win);
        });

        //Play last contained match.
        matches[1].setResult(billy, lose);

        //Make sure matchC can be set now that all sub-matches are finished.
        matchC.setResult(mario, lose);
        matchC.setResult(pingas, win);

        //Make sure Correct winner can be fetched from finished matchC.
        assertEquals("pingas", matchC.getWinner(0).getName());
        assertEquals(pingas, matchC.getWinner(0));
    }

    @Test
    @DisplayName("Detects draws correctly with 2 teams")
    public void detectsDrawsWith2() {
        PointMatch match = new PointMatch(teamList);
        assertTrue(match.containsDraw());
        match.setResult(team1, "0");
        assertTrue(match.containsDraw());
        match.setResult(team2, "2");
        assertFalse(match.containsDraw());
        match.setResult(team1, "2");
        assertTrue(match.containsDraw());
    }

    @Test
    @DisplayName("Detects draws correctly with 3 teams")
    public void detectsDrawsWith3() {
        Match match = new PointMatch(extendedTeamList);
        assertTrue(match.containsDraw());// 0, 0, 0
        match.setResult(team2, "2");
        assertTrue(match.containsDraw());// 0, 2, 0
        match.setResult(team1, "2");
        assertTrue(match.containsDraw());// 2, 2, 0
        match.setResult(team3, "2");
        assertTrue(match.containsDraw());// 2, 2, 2
        match.setResult(team1, "1");
        match.setResult(team2, "2");
        match.setResult(team3, "3");
        assertFalse(match.containsDraw());// 1, 2, 3
    }
}
