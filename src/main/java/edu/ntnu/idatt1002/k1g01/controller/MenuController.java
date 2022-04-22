package edu.ntnu.idatt1002.k1g01.controller;

import edu.ntnu.idatt1002.k1g01.dao.TournamentDAO;
import edu.ntnu.idatt1002.k1g01.model.Tournament;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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
public class MenuController implements Initializable{

    private TournamentDAO tournamentDAO;

    @FXML private HBox outerHBox;
    @FXML private VBox menuVBox;
    @FXML private Button displayModeButton;
    @FXML private Button administrateTournamentButton;
    @FXML private Button editTournamentButton;
    @FXML private Button mainMenuButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    /**
     * Gives this controller access to another controllers tournamentDAO.
     * If the pointer to the DAO is changed the new variable should be passed here as well or the menuBar may behave unpredictably.
     * @param tournamentDAO the DAO
     */
    public void setTournamentDAO(TournamentDAO tournamentDAO) {
        this.tournamentDAO = tournamentDAO;
    }

    /**
     * Changes scene to the home page.
     */
    @FXML
    public void changeSceneToHomePage() {
        Stage stage = (Stage) menuVBox.getScene().getWindow();
        try {
            if(tournamentDAO != null){
                stopClockThread();
            }
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

        Stage stage = (Stage) menuVBox.getScene().getWindow();

        try {
            stopClockThread();
            //Load new scene from FXML document.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../view/AdministrateTournament.fxml"));
            //Load file.
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
     * Changes the scene to administrate tournament
     * Also uses the administrate tournament controller to
     * send the tournament instance.
     */
    @FXML
    public void changeSceneToDisplayMode(){
        //First check if a tournament is actually loaded so that it can be administrated.
        if (tournamentDAO == null) { System.out.println("TopMenuBarController.TournamentDAO == null"); return; }

        Stage stage = (Stage) menuVBox.getScene().getWindow();

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
     * Also uses the edit tournament controller to
     * send the tournament instance.
     */
    @FXML
    public void changeSceneToEditTournament(){
        //First check if a tournament is actually loaded so that it can be administrated.
        if (tournamentDAO == null) { System.out.println("TopMenuBarController.TournamentDAO == null"); return; }

        Stage stage = (Stage) menuVBox.getScene().getWindow();

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
     * Disables DisplayMode button,Edit Tournament and Administrate Tournament Button
     */
    public void disableDisplayAndAdministrate(){
        displayModeButton.setDisable(true);
        editTournamentButton.setDisable(true);
        administrateTournamentButton.setDisable(true);
    }

    /**
     * Hides menu when user presses close button
     */
    public void hideMenu(){
        StackPane parent = (StackPane) outerHBox.getParent();
        parent.setVisible(false);
        parent.setDisable(true);
    }

    /**
     * Stops the clock thread when we exit
     */
    public void stopClockThread(){
        FXMLLoader loader = (FXMLLoader) menuVBox.getScene().getUserData();
        Class controllerClass = loader.getController().getClass();

        if(controllerClass.equals(DisplayModeController.class)){
            DisplayModeController controller = loader.getController();
            controller.stopClock();
        }else if(controllerClass.equals(AdministrateTournamentController.class)){
            AdministrateTournamentController controller = loader.getController();
            controller.stopClock();
        }
    }
}
