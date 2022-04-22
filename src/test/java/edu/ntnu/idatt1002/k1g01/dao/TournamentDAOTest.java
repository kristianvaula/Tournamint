package edu.ntnu.idatt1002.k1g01.dao;
import edu.ntnu.idatt1002.k1g01.model.Tournament;
import edu.ntnu.idatt1002.k1g01.model.Team;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class TournamentDAOTest {
    static final String filePath = "testSaveFile";

    @AfterEach
    public void deleteTestFile() {
        File file = new File(filePath + TournamentDAO.getFileExtension());
        if (file.exists()) {
            file.delete();
        }
    }


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
            File file = new File(filePath);
            TournamentDAO dao = new TournamentDAO(file);
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
        File file = new File(filePath);
        TournamentDAO dao = new TournamentDAO(file);
        try { loadedTournament = dao.load(); }
        catch (IOException ioException) {
            throw new AssertionError("failed to load tournament from file: " + ioException.getMessage());
        }
        assertEquals(savedTournament.getTournamentName(), loadedTournament.getTournamentName());
        assertEquals(savedTournament.getNumberOfTeams(), loadedTournament.getNumberOfTeams());
        for (int i = 0; i < savedTournament.getNumberOfTeams(); i++)
        {
            assertEquals(savedTournament.getTeams().get(i).getName(), loadedTournament.getTeams().get(i).getName());
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
        File file = new File(filePath);
        TournamentDAO dao = new TournamentDAO(file);
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
            File file = new File(filePath);
            TournamentDAO dao = new TournamentDAO(file);
            dao.save(oldTournament);
            oldTournament.setTournamentName("Some Other Name");
            dao.save();
            assertNotEquals(oldTournament.getTournamentName(), newTournament.getTournamentName());
        }
        catch (IOException ioException) {
            throw new AssertionError(ioException.getMessage());
        }
    }
}
