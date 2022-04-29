package edu.ntnu.idatt1002.k1g01.model.stages;


import edu.ntnu.idatt1002.k1g01.model.Round;
import edu.ntnu.idatt1002.k1g01.model.Team;
import edu.ntnu.idatt1002.k1g01.model.matches.Match;
import edu.ntnu.idatt1002.k1g01.model.matches.PointMatch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;


import static org.junit.jupiter.api.Assertions.*;

class KnockoutStageTest {

    private Team team1,team2,team3, team4;
    private ArrayList<Team> teamList1, teamList2, teamList3;
    private PointMatch testMatch1, testMatch2;
    private KnockoutStage knockoutStage1, knockoutStage2;

    /**
     * Generated dummy Teams for testing
     * @param n number of teams to generate
     * @return ArrayList of teams.
     */
    private ArrayList<Team> generateTeams(int n){
        ArrayList<Team> teams = new ArrayList<>();
        String[] names = {"pingas", "luigi", "princess", "maiboi", "stinker", "frog", "guttaBoys", "sennep inc", "din mor", "covfefe", "mousie"};
        for (int i = 0; i < n; i++) {
            String number = String.valueOf(i/names.length + 1);
            if (number.equals("1")) number = "";
            teams.add(new Team(names[i % names.length] + number));
        }
        return teams;
    }

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
        ArrayList<Match> matches1 = rounds.get(1).getMatches();
        for(Match match: matches1) {
            for (int i = 0; i < match.getParticipants().size(); i++) {
                match.setResult(match.getParticipants().get(i), String.valueOf(i + 1));
            }
        }
        Team winner = knockoutStage2.getRounds().get(1)
                .getMatches().get(0)
                .getWinners(1).get(0);

        assertEquals(winner,knockoutStage2.getTournamentWinner());
    }

    @Nested
    @DisplayName("Tests if a groupStage can be completed with various settings")
    public class completionTests {

        /**
         * Attempts to complete a stage with given parameters using both timeMatch and pointMatch.
         * @param teamCount number of teams to use.
         * @param matchSize number of teams per match.
         */
        void canFinishWith(int teamCount, int matchSize) {
            KnockoutStage stage = new KnockoutStage(generateTeams(teamCount), matchSize, "pointMatch");
            for (Round round : stage.getRounds()) for (Match match : round.getMatches()) {
                ArrayList<Team> teams = match.getParticipants();
                for (Team team : teams) {
                    match.setResult(team, String.valueOf(teams.indexOf(team)));
                }
            }
            Team winner = stage.getTournamentWinner();
            assertNotNull(winner);

            stage = new KnockoutStage(generateTeams(teamCount), matchSize, "timeMatch");
            for (Round round : stage.getRounds()) for (Match match : round.getMatches()) {
                ArrayList<Team> teams = match.getParticipants();
                for (Team team : teams) {
                    match.setResult(team, "00:00:" + String.valueOf(teams.indexOf(team)) + ":00");
                }
            }
            winner = stage.getTournamentWinner();
            assertNotNull(winner);
        }

        @Test
        @DisplayName("Can finish a knockoutStage with 32 teams and 2 teams per match")
        void canFinishWit8and2() {
            canFinishWith(8, 2);
        }

        @Test
        @DisplayName("Can finish a knockoutStage with 64 teams and 2 teams per match")
        void canFinishWit64and2() {
            canFinishWith(64, 2);
        }

        @Test
        @DisplayName("Can finish a knockoutStage with 256 teams and 2 teams per match")
        void canFinishWit256and2() {
            canFinishWith(256, 2);
        }

        @Test
        @DisplayName("Can finish a knockoutStage with 64 teams and 4 teams per match")
        void canFinishWit64and4() {
            canFinishWith(64, 4);
        }

        @Test
        @DisplayName("Can finish a knockoutStage with 256 teams and 4 teams per match")
        void canFinishWit256and4() {
            canFinishWith(256, 4);
        }

        @Test
        @DisplayName("Can finish a knockoutStage with 256 teams and 8 teams per match")
        void canFinishWit256and8() {
            canFinishWith(256, 8);
        }

        @Test
        @DisplayName("Can finish a knockoutStage with 320 teams and 10 teams per match")
        void canFinishWit320and10() {
            canFinishWith(320, 10);
        }

        @Test
        @DisplayName("Can finish a knockoutStage with 384 teams and 12 teams per match")
        void canFinishWit384and12() {
            canFinishWith(384, 12);
        }
    }


}