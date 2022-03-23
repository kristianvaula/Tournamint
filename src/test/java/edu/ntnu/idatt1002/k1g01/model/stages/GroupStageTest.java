package edu.ntnu.idatt1002.k1g01.model.stages;

import edu.ntnu.idatt1002.k1g01.model.stages.GroupStage;
import edu.ntnu.idatt1002.k1g01.model.Team;
import edu.ntnu.idatt1002.k1g01.model.matches.PointMatch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class GroupStageTest {

    private Team team1,team2,team3, team4,team5,team6,team7,team8;
    private ArrayList<Team> teamList1,teamList2, teamList3;
    private PointMatch testMatch1, testMatch2;
    private GroupStage groupStage;

    @BeforeEach
    void setUp() {
        team1 = new Team("TeamName1");
        team2 = new Team("TeamName2");
        team3 = new Team("TeamName3");
        team4 = new Team("TeamName4");
        team5 = new Team("TeamName5");
        team6 = new Team("TeamName6");
        team7 = new Team("TeamName7");
        team8 = new Team("TeamName8");

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
        teamList3.add(team5);
        teamList3.add(team6);
        teamList3.add(team7);
        teamList3.add(team8);

        groupStage = new GroupStage(teamList3, 2,4, "pointMatch");
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