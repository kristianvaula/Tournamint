package edu.ntnu.idatt1002.k1g01.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * This method is used to go from HomePage to CreateATournament page when the "Create new tournament"
 * button is pressed.
 */
public class HomePageController {
    public void CreateButtonPressed(ActionEvent event) throws IOException {

        Parent tableViewParent = FXMLLoader.load(getClass().getResource("CreateATournament.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);


        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();
    }
}
