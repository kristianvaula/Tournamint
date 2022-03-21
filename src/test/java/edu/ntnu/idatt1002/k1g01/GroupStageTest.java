package edu.ntnu.idatt1002.k1g01;

import edu.ntnu.idatt1002.k1g01.matches.PointMatch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GroupStageTest {

    private Team team1,team2,team3, team4;
    private ArrayList<Team> teamList1,teamList2, teamList3;
    private PointMatch testMatch1, testMatch2;
    private GroupStage groupStage;

    @BeforeEach
    void setUp() {
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

        teamList3 = new ArrayList<>();
        teamList3.add(team1);
        teamList3.add(team2);
        teamList3.add(team3);
        teamList3.add(team4);

        testMatch1 = new PointMatch(teamList1);
        testMatch1.setResult(team1, "2");
        testMatch1.setResult(team2, "1");
        testMatch2 = new PointMatch(teamList2);
        testMatch2.setResult(team3, "3");
        testMatch2.setResult(team4, "2");

        groupStage = new GroupStage(teamList3, 4);
    }

    @Test
    void getWinnersFromGroups() {
    }

    @Test
    void getGroups() {
    }

    @Test
    void isFinished() {
    }
}