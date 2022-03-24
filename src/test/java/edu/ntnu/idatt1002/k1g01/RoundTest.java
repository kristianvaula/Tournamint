package edu.ntnu.idatt1002.k1g01;

import edu.ntnu.idatt1002.k1g01.model.Round;
import edu.ntnu.idatt1002.k1g01.model.Team;
import edu.ntnu.idatt1002.k1g01.model.matches.Match;
import edu.ntnu.idatt1002.k1g01.model.matches.PointMatch;
import edu.ntnu.idatt1002.k1g01.model.stages.GroupStage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

class RoundTest {

    private Team team1, team2, team3, team4;
    private ArrayList<Team> teamList1, teamList2, teamList3;
    private GroupStage groupStage;
    private ArrayList<Match> matches;

    @BeforeEach
    void setUp() {
        team1 = new Team("TeamName1");
        team2 = new Team("TeamName2");
        team3 = new Team("TeamName3");
        team4 = new Team("TeamName4");

        teamList1 = new ArrayList<>();
        teamList1.add(team1);
        teamList1.add(team2);
        teamList1.add(team3);
        teamList1.add(team4);

        teamList2 = new ArrayList<>();
        teamList2.add(team1);
        teamList2.add(team2);

        teamList3 = new ArrayList<>();
        teamList3.add(team3);
        teamList3.add(team4);

        groupStage = new GroupStage(teamList1, 2, 4, "pointMatch");
        matches = groupStage.getGroups().get(0).getMatches();


    }


    @Test
    @DisplayName("tests the Round constructor and get methods")
    public void roundConstructorAndGetMethodsTest() {

        String roundName = "TestRound";
        Round testRound = new Round(matches, roundName);
        assertEquals(testRound.getMatches(), matches);
        assertEquals(testRound.getRoundName(), roundName);
        assertTrue(matches.size() == 6);
    }

    @Test
    @DisplayName("Tests the amountOfMatches and addMatch methods")
    public void addMatchTest() {
        String roundName = "TestRound";
        Round testRound = new Round(matches, roundName);
        assertEquals(testRound.amountOfMatches(), matches.size());
        int numberOfCurrentMatches = testRound.amountOfMatches();
        PointMatch testMatch = new PointMatch(teamList2);
        testRound.addMatch(testMatch);
        assertTrue(testRound.amountOfMatches() > numberOfCurrentMatches);
    }

    @Test
    @DisplayName("Tests the isFinished method depending on if there's matches left of not")
    public void isFinishedMethodTest() {
        String roundName = "TestRound";
        Round testRound = new Round(matches, roundName);
        assertFalse(testRound.isFinished());
        for (Match testMatch : testRound.getMatches()) {
            testMatch.setFinished(true);
        }
        assertTrue(testRound.isFinished());
    }

    @Test
    @DisplayName("Test the getWinners method")
    public void getWinnersMethodTest() {
        String roundName = "TestRound";
        matches.clear();
        PointMatch testMatch1 = new PointMatch(teamList2);
        PointMatch testMatch2 = new PointMatch(teamList3);
        matches.add(testMatch1);
        matches.add(testMatch2);
        Round testRound = new Round(matches, roundName);
        for (Match match : testRound.getMatches()) {
            if (match.getParticipants().contains(team1)) {
                match.setResult(team1, "4");
                for (Team team : match.getParticipants()) {
                    if (team != team1) {
                        match.setResult(team, "1");
                    }

                }
            }
            if (match.getParticipants().contains(team3)) {
                match.setResult(team3, "4");
                for (Team team : match.getParticipants()) {
                    if (team != team3) {
                        match.setResult(team, "1");
                    }
                }
            }
        }
        System.out.println(testRound.getWinners(2));
    }
}