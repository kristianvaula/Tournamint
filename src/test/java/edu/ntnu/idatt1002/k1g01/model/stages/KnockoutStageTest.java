package edu.ntnu.idatt1002.k1g01.model.stages;


import edu.ntnu.idatt1002.k1g01.model.Round;
import edu.ntnu.idatt1002.k1g01.model.Team;
import edu.ntnu.idatt1002.k1g01.model.matches.Match;
import edu.ntnu.idatt1002.k1g01.model.matches.PointMatch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;


import static org.junit.jupiter.api.Assertions.*;

class KnockoutStageTest {

    private Team team1,team2,team3, team4;
    private ArrayList<Team> teamList1, teamList2, teamList3;
    private PointMatch testMatch1, testMatch2;
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

        testMatch1 = new PointMatch(teamList1);
        testMatch1.setResult(team1,"2");
        testMatch1.setResult(team2,"1");
        testMatch2 = new PointMatch(teamList2);
        testMatch2.setResult(team3,"3");
        testMatch2.setResult(team4,"2");

        knockoutStage1 = new KnockoutStage(teamList1, 2, "PointMatch");
        knockoutStage2 = new KnockoutStage(teamList3, 2, "PointMatch");
    }

    @Test
    @DisplayName("Tests that the getTeams method returns the correct list")
    void getTeamsReturnsTeamList1() {
        assertEquals(teamList1, knockoutStage1.getTeams());
    }

    @Test
    @DisplayName("Tests the setTeams method")
    void setTeamsToContainTeamList2() {
        knockoutStage1.setTeams(teamList2);
        assertEquals(teamList2, knockoutStage1.getTeams());
    }

    @Test
    @DisplayName("Tests the getTeamsPerMatch method")
    void getTeamsPerMatchReturns2() {
        assertEquals(2, knockoutStage1.getTeamsPerMatch());
    }

    @Test
    @DisplayName("Tests the getAdvancingPerMatches method")
    void getAdvancingPerMatchReturns1() {
        assertEquals(1, knockoutStage1.getAdvancingPerMatch());
    }

    @Test
    @DisplayName("Tests the roundSetUp method for the final")
    void roundSetUpFinal() {
        Round round = new Round(new ArrayList<>(), "FINAL");
        knockoutStage1.roundSetUp(round, new ArrayList<>());
        assertEquals("FINAL", round.getRoundName());
    }

    @Test
    @DisplayName("Tests the updateKnockoutStage method")
    void updateKnockoutStage() {
        ArrayList<Round> rounds = knockoutStage2.getRounds();
        ArrayList<Match> matches = rounds.get(0).getMatches();
        for(Match match: matches) {
            for (int i = 0; i < match.getParticipants().size(); i++) {
                match.setResult(match.getParticipants().get(i), String.valueOf(i));
            }
        }
        knockoutStage2.update();
        System.out.println(rounds.get(1).getMatches().get(0).getParticipants().toString());
        assertTrue(rounds.get(1).getMatches().get(0).getParticipants().size() > 0);
    }

    @Test
    @DisplayName("Tests the getTournamentWinner method")
    void getTournamentWinner() {
        ArrayList<Round> rounds = knockoutStage2.getRounds();
        ArrayList<Match> matches = rounds.get(0).getMatches();
        for(Match match: matches) {
            for (int i = 0; i < match.getParticipants().size(); i++) {
                match.setResult(match.getParticipants().get(i), String.valueOf(i));
            }
        }
        knockoutStage2.update();
        ArrayList<Match> matches1 = rounds.get(1).getMatches();
        for(Match match: matches1) {
            for (int i = 0; i < match.getParticipants().size(); i++) {
                match.setResult(match.getParticipants().get(i), String.valueOf(i));
            }
        }
        Team winner = knockoutStage2.getRounds().get(1).getMatches().get(0).getWinners(1).get(0);

        assertEquals(winner,knockoutStage2.getTournamentWinner());
    }
}