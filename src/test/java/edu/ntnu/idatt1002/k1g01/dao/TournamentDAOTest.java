package edu.ntnu.idatt1002.k1g01.dao;
import edu.ntnu.idatt1002.k1g01.dao.TournamentDAO;
import edu.ntnu.idatt1002.k1g01.model.Tournament;
import edu.ntnu.idatt1002.k1g01.model.Team;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class TournamentDAOTest {
    String path = "testSaveFile";

    private ArrayList<Team> generateTeams(int n){
        ArrayList<Team> teams = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            int roll = i % 6;
            switch (roll) {
                case 0: { teams.add(new Team("pingas_" + i/6 )); break; }
                case 1: { teams.add(new Team("luigi_" + i/6 )); break; }
                case 2: { teams.add(new Team("princess_" + i/6 )); break; }
                case 3: { teams.add(new Team("maiboi_" + i/6 )); break; }
                case 4: { teams.add(new Team("stinker_" + i/6 )); break; }
                case 5: { teams.add(new Team("frog_" + i/6 )); break; }
            }
        }
        return teams;
    }

    @Test
    public void saveTournamentToFile() {
        Tournament tournament = new Tournament("testTournament.csv", generateTeams(16), "point", 2, 4, 1);
        try {
            TournamentDAO dao = new TournamentDAO(path);
            dao.save(tournament);
        }
        catch (IOException ioException) {
            throw new AssertionError("failed to save tournament to file: " + ioException.getMessage());
        }
    }

    @Test
    public void loadTournamentFromFile() {
        saveTournamentToFile();
        Tournament savedTournament = new Tournament("testTournament.csv", generateTeams(16), "point",
                2, 4, 1);
        Tournament loadedTournament;
        TournamentDAO dao = new TournamentDAO(path);
        try { loadedTournament = dao.load(); }
        catch (IOException ioException) {
            throw new AssertionError("failed to load tournament from file: " + ioException.getMessage());
        }
        //TODO Perform more comprehensive comparison between Tournament objects to make sure they are indeed equivalent.
        assertEquals(savedTournament.getTournamentName(), loadedTournament.getTournamentName());
        assertEquals(savedTournament.getNumberOfTeams(), loadedTournament.getNumberOfTeams());
        assertEquals(savedTournament.getTeams().get(0), loadedTournament.getTeams().get(0));
    }
}
