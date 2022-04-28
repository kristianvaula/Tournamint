package edu.ntnu.idatt1002.k1g01.controller;

import edu.ntnu.idatt1002.k1g01.dao.TournamentDAO;
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

import java.awt.Desktop;
import java.net.URI;

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
    @FXML MenuItem topMenuBarEdit;
    @FXML MenuItem topMenuBarClose;
    @FXML MenuItem topMenuBarSaveAs;
    @FXML Menu topMenuBarView;
    @FXML MenuItem topMenuBarDisplay;
    @FXML MenuItem topMenuBarAdministrate;
    @FXML MenuItem topMenuBarAbout;
    @FXML MenuItem topMenuBarManual;

    private TournamentDAO tournamentDAO;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}

    /**
     * Gives this controller access to another controllers tournamentDAO.
     * If the pointer to the DAO is changed the new variable should be passed here as well or the menuBar may behave unpredictably.
     * @param tournamentDAO the DAO
     */
    public void setTournamentDAO(TournamentDAO tournamentDAO) {
        this.tournamentDAO = tournamentDAO;
        topMenuBarSaveAs.setDisable(false);
        topMenuBarEdit.setDisable(false);
        topMenuBarClose.setDisable(false);
        topMenuBarDisplay.setDisable(false);
        topMenuBarView.setDisable(false);
    }

    /**
     * Opens another tournament from file.
     */
    @FXML
    public void openFile() {
        Stage stage = (Stage) topMenuBar.getScene().getWindow();
        try {
            tournamentDAO = FileController.openFromFile(stage);
            if (tournamentDAO == null) return;
            tournamentDAO.load();
            changeSceneToAdministrateTournament();
        }
        catch (Exception e) {
            System.out.println("ERROR ALERT");
            System.out.println(e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage() + "\nFile may be corrupted, or created by outdated version of Tournamint.");
            alert.show();
        }
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
            stopClockThread();
            Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../view/HomePage.fxml")));
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Changes the scene to administrate tournament.
     * Initializes controller with TournamentDAO instance.
     */
    @FXML
    public void changeSceneToAdministrateTournament(){
        //First check if a tournament is actually loaded so that it can be administrated.
        if (tournamentDAO == null) { System.out.println("TopMenuBarController.TournamentDAO == null"); return; }

        Stage stage = (Stage) topMenuBar.getScene().getWindow();

        try {
            stopClockThread();
            //Load new scene from FXML document.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../view/AdministrateTournament.fxml"));
            Parent sceneParent = loader.load();
            Scene scene = new Scene(sceneParent);
            scene.setUserData(loader);
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

    /**
     * Changes the scene to display mode.
     * Initializes controller with TournamentDAO instance.
     */
    @FXML
    public void changeSceneToDisplayMode(){
        //First check if a tournament is actually loaded so that it can be administrated.
        if (tournamentDAO == null) { System.out.println("TopMenuBarController.TournamentDAO == null"); return; }

        Stage stage = (Stage) topMenuBar.getScene().getWindow();

        try {
            stopClockThread();
            //Load new scene from FXML document.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../view/DisplayMode.fxml"));

            //Load file.
            Parent sceneParent = loader.load();
            Scene scene = new Scene(sceneParent);
            scene.setUserData(loader);
            //Initiate new scene controller with loaded DAO as data.
            DisplayModeController controller = loader.getController();
            controller.initData(tournamentDAO);

            //Switch to new scene.
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            System.out.println("" + e.getCause());
        }
    }

    /**
     * Changes the scene to edit tournament
     * Initializes controller with TournamentDAO instance.
     */
    @FXML
    public void changeSceneToEditTournament(){
        //First check if a tournament is actually loaded so that it can be administrated.
        if (tournamentDAO == null) { System.out.println("TopMenuBarController.TournamentDAO == null"); return; }

        Stage stage = (Stage) topMenuBar.getScene().getWindow();

        try {
            stopClockThread();
            //Load new scene from FXML document.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../view/EditTournament.fxml"));

            //Load file.
            Parent sceneParent = loader.load();
            Scene scene = new Scene(sceneParent);
            scene.setUserData(loader);

            //Initiate new scene controller with loaded DAO as data.
            EditTournamentController controller = loader.getController();
            controller.initData(tournamentDAO);

            //Switch to new scene.
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println("" + e.getCause());
        }
    }

    /**
     * Stops the corner clock.
     */
    public void stopClockThread(){
        FXMLLoader loader = (FXMLLoader) topMenuBar.getScene().getUserData();
        if (loader == null) return; //In case this scene has no clock.
        Class controllerClass = loader.getController().getClass();

        if(controllerClass.equals(DisplayModeController.class)){
            DisplayModeController controller = loader.getController();
            controller.stopClock();
        }else if(controllerClass.equals(AdministrateTournamentController.class)){
            AdministrateTournamentController controller = loader.getController();
            controller.stopClock();
        }
    }

    /**
     * Opens the User Manual PDF using the system's default browser
     */
    public void openManual() {
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI("https://gitlab.stud.idi.ntnu.no/kristvje/idatt1002_2022_k1-g01/-/wikis/uploads/0294c71d9b6a32c67b2732e6f78145f8/User_Manual.pdf"));
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.show();
        }
    }
}
