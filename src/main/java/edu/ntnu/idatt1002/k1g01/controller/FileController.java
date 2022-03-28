package edu.ntnu.idatt1002.k1g01.controller;

import edu.ntnu.idatt1002.k1g01.dao.TournamentDAO;
import edu.ntnu.idatt1002.k1g01.model.Tournament;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;

/**
 * Bundle of static, commonly used file handling code.
 */
public interface FileController {

    static TournamentDAO saveToFile(Tournament tournament, Stage window) throws IOException {

        //Initialize file selection dialog.
        //TODO Consider more readable file extension name. Using .qxz because it does not collide with any known extensions.
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Tournamint Files (*.qxz)", "*.qxz");
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(extensionFilter);
        fileChooser.setSelectedExtensionFilter(extensionFilter);
        fileChooser.setInitialFileName(tournament.getTournamentName());
        fileChooser.setTitle("Save Tournament");

        //TODO Consider setting a default path to save new tournaments.
        try {
            //Show file selection dialog and get tournamentDAO from file.
            File file = fileChooser.showSaveDialog(window);
            TournamentDAO tournamentDAO = new TournamentDAO(file);

            //Save tournament to given file path.
            tournamentDAO.save(tournament);
            return tournamentDAO;
        }
        catch (Exception e) {
            throw new IOException("Error saving tournament to file: " + e.getMessage());
        }
    }

    /**
     * Opens another tournament from file.
     * TODO Consider more readable file extension name.
     */
    static TournamentDAO openFromFile(Stage window) throws IOException{

        //Initialize file selection dialog.
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Tournamint Files (*.qxz)", "*.qxz");
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(extensionFilter);
        fileChooser.setSelectedExtensionFilter(extensionFilter);
        fileChooser.setTitle("Open Tournament");

        //Show file selection dialog and get tournamentDAO from file.
        try {
            File file = fileChooser.showOpenDialog(window);
            TournamentDAO tournamentDAO = new TournamentDAO(file);
            tournamentDAO.load();
            return tournamentDAO;
        }
        catch (Exception e) {
            throw new IOException("Error loading tournament from file: " + e.getMessage());
        }
    }
}
