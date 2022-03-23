package edu.ntnu.idatt1002.k1g01.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomePageController implements Initializable {

    /**
     * Changes the scene to CreateATournamentWindow
     */
    @FXML
    public void changeScreenToCreateTournament(ActionEvent event)throws IOException {
        try {
            Parent createTournament = FXMLLoader.load(getClass().getResource("../CreateATournament.fxml"));
            Scene createTournamentScene = new Scene(createTournament);

            //This line gets the Stage information
            Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
            window.setScene(createTournamentScene);
            window.show();

        } catch (IOException e) {
            System.out.println(e.getCause());
            throw e;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
