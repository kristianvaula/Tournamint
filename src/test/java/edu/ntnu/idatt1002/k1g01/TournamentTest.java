package edu.ntnu.idatt1002.k1g01;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TournamentTest {

    @Nested
    @DisplayName("Tests the constructor(s)")
    public class ConstructorTest{

        @Test
        @DisplayName("Initiates a tournament")
        public void InitiatesATournament(){
            ArrayList<Team> teamlist = new ArrayList<>();
            teamlist.add(new Team("TestTeam"));
            String testName = "testName";

            Tournament tournament = new Tournament(testName,teamlist);

            assertEquals(testName,tournament.getTournamentName());
        }

        @Test
        @DisplayName("Initiates a tournament with empty team list throws exception")
        public void InitiatesATournamentWithEmptyTeamList(){
            ArrayList<Team> teamlist = new ArrayList<>();
            String testName = "testName";

            try{
                Tournament tournament = new Tournament(testName,teamlist);
                fail();
            }catch(IllegalArgumentException e){
                assertEquals(e.getClass(),new IllegalArgumentException().getClass());
            }
        }
    }

    @Nested
    @DisplayName("Tests tournament methods")
    public class MethodTesting{

        @Test
        @DisplayName("getTeams() gets list of teams")
        public void GetTeamsGetsCorrectTeamList(){
            ArrayList<Team> teamList = new ArrayList<>();
            Team team = new Team("TestTeam");
            teamList.add(team);
            Tournament tournament = new Tournament("testName",teamList);

            ArrayList<Team> checkList = tournament.getTeams();

            assertEquals(checkList.get(0),team);
        }

        @Test
        @DisplayName("addTeam() with a new team")
        public void AddTeamWithNewTeam(){
            ArrayList<Team> teamList = new ArrayList<>();
            teamList.add(new Team("TestTeam"));
            Tournament tournament = new Tournament("testName",teamList);


            Team newTeam = new Team("NewTeam");
            tournament.addTeam(newTeam);

            assertTrue(tournament.getTeams().size() == 2);
        }

        @Test
        @DisplayName("addTeam() with the same team throws IllegalArgumentException")
        public void AddTeamWithSameTeam(){
            ArrayList<Team> teamList = new ArrayList<>();
            Team testTeam = new Team("TestTeam");
            teamList.add(testTeam);
            Tournament tournament = new Tournament("testName",teamList);

            try{
                tournament.addTeam(testTeam);
                fail();
            }catch(IllegalArgumentException e){
                assertEquals(e.getClass(),new IllegalArgumentException().getClass());
            }
        }

        @Test
        @DisplayName("getNumberOfTeam() returns correct amount of teams")
        public void GetCorrectNumberOfTeams(){
            ArrayList<Team> teamList = new ArrayList<>();
            teamList.add(new Team("TestTeam"));
            Tournament tournament = new Tournament("testName",teamList);

            int tournamentSizeBefore = tournament.getNumberOfTeams();
            teamList.add(new Team("NewTeam"));
            int tournamentSizeAfter = tournament.getNumberOfTeams();

            assertTrue(tournamentSizeBefore + 1 == tournamentSizeAfter);
        }
    }
}
