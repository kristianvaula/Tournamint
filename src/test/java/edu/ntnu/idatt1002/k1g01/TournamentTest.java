package edu.ntnu.idatt1002.k1g01;

import edu.ntnu.idatt1002.k1g01.model.stages.KnockoutStage;
import edu.ntnu.idatt1002.k1g01.model.Team;
import edu.ntnu.idatt1002.k1g01.model.Tournament;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TournamentTest {

    private Team team1,team2,team3, team4;
    private ArrayList<Team> teamList1, teamList2, teamList3;
    private KnockoutStage knockoutStage1, knockoutStage2;

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
    }


    @Nested
    @DisplayName("Tests the constructor(s)")
    public class ConstructorTest{

        @Test
        @DisplayName("Tests the initiation of a tournament")
        public void InitiatesATournament(){
            String testName = "testName";

            Tournament tournament = new Tournament(testName,teamList3,"pointMatch",2,4,2);

            assertEquals(testName,tournament.getTournamentName());
        }

        @Test
        @DisplayName("Tests that the initiation of a tournament with empty team list throws exception")
        public void InitiatesATournamentWithEmptyTeamList(){
            ArrayList<Team> teamList = new ArrayList<>();
            String testName = "testName";

            try{
                Tournament tournament = new Tournament(testName,teamList,"PointMatch",2,4,2);
                fail();
            }catch(IllegalArgumentException e){
                assertEquals(e.getClass(), IllegalArgumentException.class);
            }
        }
    }


    @Nested
    @DisplayName("Tests tournament methods")
    public class MethodTesting{

        @Test
        @DisplayName("Tests that the getTeams method gets a list of teams")
        public void GetTeamsGetsCorrectTeamList(){
            Tournament tournament = new Tournament("testName",teamList3,"pointMatch",2,4,2);

            ArrayList<Team> checkList = tournament.getTeams();

            assertEquals(teamList3, checkList);
        }

        @Test
        @DisplayName("Tests that the addTeam method adds a new team")
        public void AddTeamWithNewTeam(){
            Tournament tournament = new Tournament("testName",teamList3,"pointMatch",2,4,2);

            Team newTeam = new Team("NewTeam");
            tournament.addTeam(newTeam);

            assertEquals(newTeam,tournament.getTeams().get(tournament.getNumberOfTeams()-1));
        }

        @Test
        @DisplayName("Tests that the addTeam method throws IllegalArgumentException when trying to add the same team ")
        public void AddTeamWithSameTeam(){
            Team testTeam = new Team("TestTeam");
            teamList3.add(testTeam);

            Tournament tournament = new Tournament("testName",teamList3,"pointMatch",2,3,1);

            try{
                tournament.addTeam(testTeam);
                fail();
            }catch(IllegalArgumentException e){
                assertEquals(e.getClass(), IllegalArgumentException.class);
            }
        }

        @Test
        @DisplayName("Tests that the getNumberOfTeam method returns correct amount of teams")
        public void GetCorrectNumberOfTeams(){
            Tournament tournament = new Tournament("testName",teamList3,"pointMatch",2,4,2);

            int tournamentSizeBefore = tournament.getNumberOfTeams();
            teamList3.add(new Team("NewTeam"));
            int tournamentSizeAfter = tournament.getNumberOfTeams();

            assertEquals(tournamentSizeBefore + 1, tournamentSizeAfter);
        }
    }
}
