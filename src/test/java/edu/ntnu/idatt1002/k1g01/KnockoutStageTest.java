package edu.ntnu.idatt1002.k1g01;

import edu.ntnu.idatt1002.k1g01.matches.PointMatch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class KnockoutStageTest {

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
    void getRounds() {
    }

    @Test
    void getWinnerFromKnockouts() {
    }

    @Test
    void getRoundWinners() {
    }

    @Test
    void getRoundName() {
    }

    @Test
    void generateNextRound() {
    }
}