package edu.ntnu.idatt1002.k1g01.controller;

import edu.ntnu.idatt1002.k1g01.dao.TournamentDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import java.io.*;
import javafx.stage.FileChooser;

public class HomePageController implements Initializable {

    //The nested controller for the menuBar
    @FXML private TopMenuBarController topMenuBarController;

    /**
     * Changes the scene to CreateATournamentWindow
     */
    @FXML
    public void changeScreenToCreateTournament(ActionEvent event)throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../view/CreateATournament.fxml"));
            //Load file.
            Parent sceneParent = loader.load();
            Scene scene = new Scene(sceneParent);
            scene.setUserData(loader);

            //Initiate new scene controller with loaded DAO as data.
            CreateATournamentController controller = loader.getController();
            controller.disableMenuButtons();

            //This line gets the Stage information
            Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();

        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    /**
     * Shows a fileChooser dialog and ask user for path to file.
     * @param event of type ActionEvent
     * @author Martin Dammerud
     */
    @FXML
    public void OpenTournamentFromFile(ActionEvent event) {

        try {
            //Get active source window.
            Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();

            //Get TournamentDAO from user specified file.
            TournamentDAO tournamentDAO = FileController.openFromFile(window);
            if (tournamentDAO == null) return; //Cancelled by user.

            //Open administrate tournament with DAO.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../view/AdministrateTournament.fxml"));
            Parent administrateParent = loader.load();
            Scene administrateScene = new Scene(administrateParent);
            administrateScene.setUserData(loader);
            //Prepare controller and switch scene.
            AdministrateTournamentController controller = loader.getController();
            controller.initData(tournamentDAO);
            window.setScene(administrateScene);
            window.show();
        }
        catch (IOException e) {
            System.out.println("ERROR ALERT");
            System.out.println(e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage() + "\nFile may be corrupted, or created by outdated version of Tournamint.");
            alert.show();

        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
