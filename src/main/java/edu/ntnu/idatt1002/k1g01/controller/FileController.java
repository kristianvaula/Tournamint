package edu.ntnu.idatt1002.k1g01.controller;

import edu.ntnu.idatt1002.k1g01.dao.TournamentDAO;
import edu.ntnu.idatt1002.k1g01.model.Tournament;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;

/**
 * Bundle of static file handling code.
 * Uses the Operating systems GUI to let the user easily save and load files.
 * //TODO Consider setting a default path to save new tournaments and more readable file extension.
 * @author Martin Dammerud
 * @implNote Split off as separate object vs TournamentDAO because this interface handles the user interaction.
 */
public interface FileController {

    /**
     * Uses OS GUI to create save file, and then saves given Tournament to that file.
     *
     * @param tournament of type Tournament
     * @param window active window
     * @return TournamentDAO, a link between the tournament, and the file that stores it.
     * @throws IOException if saving fails or is cancelled.
     */
    static TournamentDAO saveToFile(Tournament tournament, Stage window) throws IOException {

        //Initialize file selection dialog.
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Tournamint Files (*.qxz)", "*.qxz");
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(extensionFilter);
        fileChooser.setSelectedExtensionFilter(extensionFilter);
        fileChooser.setInitialFileName(tournament.getTournamentName());
        fileChooser.setTitle("Save Tournament");


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
     * Uses OS GUI to find and open a file containing a previously save tournament.
     *
     * @param window active window
     * @return TournamentDAO, a link between the tournament, and the file that stores it.
     * @throws IOException if loading fails or is cancelled.
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
            if (file == null) return null; //If cancelled by user
            TournamentDAO tournamentDAO = new TournamentDAO(file);
            tournamentDAO.load();
            return tournamentDAO;
        }
        catch (Exception e) {
            throw new IOException("Error loading tournament from file: " + e.getMessage());
        }
    }
}
