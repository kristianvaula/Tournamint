package edu.ntnu.idatt1002.k1g01.controller;

import edu.ntnu.idatt1002.k1g01.dao.TournamentDAO;
import edu.ntnu.idatt1002.k1g01.model.Tournament;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Module with all controllers for the top MenuBar.
 *
 *      Include the following in the scene.fxml:
 *          " <fx:include fx:id="topMenuBar" source="TopMenuBar.fxml"/> "
 *      This replaces the menuBar code and loads it from a separate file.
 *
 *      Include the following in the SceneController.java:
 *          " @FXML private TopMenuBarController topMenuBarController; "
 *      This makes this controller available to the main scene controller as a nested controller.

 *      If possible, include this in the main controller's init() method:
 *          " topMenuBarController.setTournamentDAO(tournamentDAO); "
 *      This makes it possible to save tournaments from the topMenuBar.
 *
 * @author Martin Dammerud
 */
public class TopMenuBarController implements Initializable{
    @FXML MenuBar topMenuBar;
    @FXML MenuItem topMenuBarOpen;
    @FXML MenuItem topMenuBarClose;
    @FXML MenuItem topMenuBarSaveAs;
    private TournamentDAO tournamentDAO;

    /**
     * Gives this controller access to another controllers tournamentDAO.
     * If the pointer to the DAO is changed the new variable should be passed here as well or the menuBar may behave unpredictably.
     * @param tournamentDAO the DAO
     */
    public void setTournamentDAO(TournamentDAO tournamentDAO) {
        this.tournamentDAO = tournamentDAO;
    }

    public void printState() {
        System.out.println("State of TopMenuBarController:");
        System.out.println("    topMenuBar: " + topMenuBar);
        System.out.println("    topMenuBarOpen: " + topMenuBarOpen);
        System.out.println("    topMenuBarClose: " + topMenuBarClose);
        System.out.println("    topMenuBarSaveAs: " + topMenuBarSaveAs);
        System.out.println("    tournamentDAO: " + tournamentDAO);
    }

    /**
     * Opens another tournament from file.
     */
    @FXML
    public void openFile() {
        Stage stage = (Stage) topMenuBar.getScene().getWindow();
        try {
            tournamentDAO = FileController.openFromFile(stage);
            tournamentDAO.load();
            changeSceneToAdministrateTournament();
        }
        catch (Exception e) { System.out.println(e.getMessage()); }
    }

    /**
     * Saves tournament to new file and switches working directory for tournamentDAO.
     */
    @FXML
    public void saveFile() {
        Stage stage = (Stage) topMenuBar.getScene().getWindow();
        try {
            tournamentDAO.mimic(FileController.saveToFile(tournamentDAO.load(), stage));
        }
        catch (Exception e) { System.out.println(e.getMessage()); }
    }

    /**
     * Changes scene to the home page.
     */
    @FXML
    public void changeSceneToHomePage() {
        Stage stage = (Stage) topMenuBar.getScene().getWindow();
        try {
            Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../view/HomePage.fxml")));
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Changes the scene to administrate tournament
     * Also uses the administrate tournament controller to
     * send the tournament instance.
     */
    @FXML
    public void changeSceneToAdministrateTournament(){
        //First check if a tournament is actually loaded so that it can be administrated.
        if (tournamentDAO == null) { System.out.println("TopMenuBarController.TournamentDAO == null"); return; }

        Stage stage = (Stage) topMenuBar.getScene().getWindow();

        try {
            //Load new scene from FXML document.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../view/AdministrateTournament.fxml"));
            Parent sceneParent = loader.load();
            Scene scene = new Scene(sceneParent);

            //Initiate new scene controller with loaded DAO as data.
            AdministrateTournamentController controller = loader.getController();
            controller.initData(tournamentDAO);

            //Switch to new scene.
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            System.out.println("" + e.getCause());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}
}
