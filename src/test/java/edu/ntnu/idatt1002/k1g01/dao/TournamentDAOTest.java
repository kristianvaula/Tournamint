package edu.ntnu.idatt1002.k1g01.dao;
import edu.ntnu.idatt1002.k1g01.model.Tournament;
import edu.ntnu.idatt1002.k1g01.model.Team;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class TournamentDAOTest {
    String filePath = "testSaveFile";

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
        Tournament tournament = new Tournament("testTournament", generateTeams(32), "pointMatch", 2, 4, 1);
        try {
            TournamentDAO dao = new TournamentDAO(filePath);
            dao.save(tournament);
        }
        catch (IOException ioException) {
            throw new AssertionError("failed to save tournament to file: " + ioException.getMessage());
        }
    }

    @Test
    public void loadTournamentFromFile() {
        saveTournamentToFile();
        Tournament savedTournament = new Tournament("testTournament", generateTeams(32), "pointMatch",
                2, 4, 1);
        Tournament loadedTournament;
        TournamentDAO dao = new TournamentDAO(filePath);
        try { loadedTournament = dao.load(); }
        catch (IOException ioException) {
            throw new AssertionError("failed to load tournament from file: " + ioException.getMessage());
        }
        //TODO Perform more comprehensive comparison between Tournament objects to make sure they are indeed equivalent.
        assertEquals(savedTournament.getTournamentName(), loadedTournament.getTournamentName());
        assertEquals(savedTournament.getNumberOfTeams(), loadedTournament.getNumberOfTeams());
        for (int i = 0; i < savedTournament.getNumberOfTeams(); i++)
        {
            assertEquals(savedTournament.getTeams().get(i), loadedTournament.getTeams().get(i));
        }
    }

    @Test
    public void fileExistsFindsFile() {
        saveTournamentToFile();
        assertTrue(TournamentDAO.fileExists(filePath));
    }

    @Test
    public void fileExistsAddsExtensionCorrectly() {
        saveTournamentToFile();
        assertTrue(TournamentDAO.fileExists(filePath.replace(TournamentDAO.getFileExtension(), "")));
    }

    @Test
    public void fileExistsReportsFalseProperly() {
        saveTournamentToFile();
        assertFalse(TournamentDAO.fileExists("pathToNothing.qxz"));
    }

    @Test
    public void fileDeletionWorks() {
        saveTournamentToFile();
        TournamentDAO dao = new TournamentDAO(filePath);
        try { dao.deleteFile(); }
        catch (IOException ioException) {
            throw new AssertionError(ioException.getMessage());
        }
        assertThrows(IOException.class, dao::deleteFile);
    }

    @Test
    public void fileUpdateWorks(){
        Tournament oldTournament = new Tournament("testTournament", generateTeams(32), "pointMatch", 2, 4, 1);
        Tournament newTournament = new Tournament("testTournament", generateTeams(32), "pointMatch", 2, 4, 1);
        try {
            TournamentDAO dao = new TournamentDAO(filePath);
            dao.save(oldTournament);
            assertEquals(dao.load().getTournamentName(), newTournament.getTournamentName());
            oldTournament.setTournamentName("Some Other Name");
            dao.save();
            assertNotEquals(oldTournament.getTournamentName(), newTournament.getTournamentName());
        }
        catch (IOException ioException) {
            throw new AssertionError(ioException.getMessage());
        }
    }
}
