package edu.ntnu.idatt1002.k1g01.model.stages;

import edu.ntnu.idatt1002.k1g01.model.Group;
import edu.ntnu.idatt1002.k1g01.model.matches.Match;
import edu.ntnu.idatt1002.k1g01.model.Team;
import edu.ntnu.idatt1002.k1g01.model.matches.PointMatch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GroupStageTest {

    private Team team1,team2,team3, team4,team5,team6,team7,team8;
    private ArrayList<Team> teamList1,teamList2, teamList3;
    private PointMatch testMatch1, testMatch2;
    private GroupStage groupStage;
    int advancingFromGroup = 2;
    int teamsPerGroup = 4;

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
        teamList1.add(team3);

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
    @DisplayName("Tests that the GroupStage constructor divides teams from passed in ArrayList to correct number of groups")
    public void groupStageConstructorTest() {
        assertFalse(groupStage.getGroups().isEmpty());
        assertEquals(groupStage.getGroups().size(), (teamList3.size()/teamsPerGroup));
        for (Group testGroup : groupStage.getGroups()) {
            for (Team testTeam : testGroup.getTeams()) {
                assertTrue(teamList3.contains(testTeam));
            }
        }
    }


    @Test
    @DisplayName("Test exceptions for advancingFromGroup variable lower than 1 and higher than teams in group")
    public void groupStageExceptionsTest() {
        assertThrows(IllegalArgumentException.class, () -> { GroupStage groupStage = new GroupStage(teamList3, 0,4, "point");});
        assertThrows(IllegalArgumentException.class, () -> { GroupStage groupStage = new GroupStage(teamList3, 5,4, "point");});
    }


    @Test
    void getWinnersFromGroups() {
        GroupStage groupStage1 = new GroupStage(teamList1, 2, 3, "pointMatch");
        //simulates a group stage for one group where team1 and team 2 finishes in the top two places
        Group testGroup = groupStage1.getGroups().get(0);
        for (Match match : testGroup.getMatches()) {
            if(match.getParticipants().contains(team1)) {
                match.setResult(team1, "4");
                for (Team team : match.getParticipants()) {
                    if (team != team1) {
                        match.setResult(team, "1");
                    }
                }
            }
            else {
                match.setResult(team2, "2");
                match.setResult(team3, "0");
            }
            //The getWinnersFromGroups method should return team1 and team2 but not team 3.
        }
        assertTrue(groupStage1.getWinnersFromGroups().contains(team1));
        assertTrue(groupStage1.getWinnersFromGroups().contains(team2));
        assertFalse(groupStage1.getWinnersFromGroups().contains(team3));
    }


    @Test
    @DisplayName("Tests that the isFinished method returns correct value based on if there's matches left or not")
    void isFinishedMethodTest() {
        GroupStage groupStage1 = new GroupStage(teamList1, 2, 3, "pointMatch");
        assertFalse(groupStage1.isFinished());
        //simulates a group stage for one group where team1 and team 2 finishes in the top two places
        Group testGroup = groupStage1.getGroups().get(0);
        for (Match match : testGroup.getMatches()) {
            if (match.getParticipants().contains(team1)) {
                match.setResult(team1, "4");
                for (Team team : match.getParticipants()) {
                    if (team != team1) {
                        match.setResult(team, "1");
                    }
                }
            } else {
                match.setResult(team2, "2");
                match.setResult(team3, "0");
            }
        }
        assertTrue(groupStage1.isFinished());
    }


    @Test
    @DisplayName("Test that isFinished does not report true if it has any matches left.")
    void isFinishedDoesNotTriggerEarly() {
        assertFalse(groupStage.isFinished());
        for (Group group : groupStage.getGroups()) for (Match match : group.getMatches()) {
            assertFalse(groupStage.isFinished());
            match.setFinished(true);
        }
        assertTrue(groupStage.isFinished());
    }


    @Nested
    @DisplayName("Test constructor for groupStages with various settings")
    public class ConstructorBulkTests {

        //generates n teams with unique names.
        private ArrayList<Team> generateTeams(int n) {
            ArrayList<Team> teams = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                int roll = i % 6;
                switch (roll) {
                    case 0: {
                        teams.add(new Team("pingas_" + i / 6));
                        break;
                    }
                    case 1: {
                        teams.add(new Team("luigi_" + i / 6));
                        break;
                    }
                    case 2: {
                        teams.add(new Team("princess_" + i / 6));
                        break;
                    }
                    case 3: {
                        teams.add(new Team("maiboi_" + i / 6));
                        break;
                    }
                    case 4: {
                        teams.add(new Team("stinker_" + i / 6));
                        break;
                    }
                    case 5: {
                        teams.add(new Team("frog_" + i / 6));
                        break;
                    }
                }
            }
            return teams;
        }

        public void constructWithFullGroups(int knockoutSize, int teamsPerGroup, int teamsAdvancing) {
            //String tournamentName, ArrayList<Team> teams, String matchType,int teamsPerMatch,int teamsPerGroup,int advancingPerGroup
            int teamCount = knockoutSize * teamsPerGroup / teamsAdvancing;
            GroupStage groupStage = new GroupStage(generateTeams(teamCount), teamsAdvancing, teamsPerGroup,
                    "pointMatch");
            for (int trueSize : groupStage.getGroups().stream().mapToInt(Group::getTeamCount).toArray()) {
                //Check every group is actually full.
                assertEquals(teamsPerGroup, trueSize);
                assertFalse(groupStage.hasPartialGroups());
            }
            //Check correct number of groups were generated in groupStage.
            assertEquals(teamCount, groupStage.getGroups().size() * teamsPerGroup);

            //Check rounds can be extracted without exception;
            groupStage.getGroupRounds();
        }

        @Test
        @DisplayName("Test constructor for groupStages with full groups and various settings")
        public void constructWithVariousFullGroups() {
            constructWithFullGroups(8, 2, 1);
            constructWithFullGroups(32, 3, 2);
            constructWithFullGroups(16, 4, 1);
            constructWithFullGroups(64, 4, 2);
            constructWithFullGroups(16, 5, 1);
            constructWithFullGroups(32, 5, 2);
            constructWithFullGroups(16, 6, 1);
            constructWithFullGroups(16, 6, 2);
            constructWithFullGroups(16, 7, 4);
        }

        public void constructWithPartialGroups(int teamCount, int teamsPerGroup, int teamsAdvancing) {
            //String tournamentName, ArrayList<Team> teams, String matchType,int teamsPerMatch,int teamsPerGroup,int advancingPerGroup
            GroupStage groupStage = new GroupStage(generateTeams(teamCount), teamsAdvancing, teamsPerGroup,
                    "pointMatch");
            for (int trueSize : groupStage.getGroups().stream().mapToInt(Group::getTeamCount).toArray()) {
                //Check no group is missing more than one team.
                assertTrue(teamsPerGroup == trueSize || teamsPerGroup == trueSize + 1);
            }
            assertTrue(groupStage.hasPartialGroups());

            //Check rounds can be extracted without exception;
            groupStage.getGroupRounds();
        }

        @Test
        @DisplayName("Test constructor with groupStages with non-full groups and various setting")
        public void constructWithVariousPartialGroups() {
            constructWithPartialGroups(15, 4, 1);
            constructWithPartialGroups(13, 4, 1);
            constructWithPartialGroups(13, 4, 2);
            constructWithPartialGroups(19, 5, 2);
            constructWithPartialGroups(29, 8, 2);
        }
    }
}