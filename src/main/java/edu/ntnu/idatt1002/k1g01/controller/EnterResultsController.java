package edu.ntnu.idatt1002.k1g01.controller;

import edu.ntnu.idatt1002.k1g01.model.matches.Match;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EnterResultsController implements Initializable {

    Match match;

    @FXML private Text team1Text;
    @FXML private Text team2Text;

    @FXML
    public void cancelAndGoBack (ActionEvent event)throws IOException {
        try {
            Parent enterResults = FXMLLoader.load(getClass().getResource("../AdministrateTournament.fxml"));
            Scene enterResultsScene = new Scene(enterResults);

            //This line gets the Stage information
            Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
            window.setScene(enterResultsScene);
            window.show();

        } catch (IOException e) {
            System.out.println(e.getCause());
            throw e;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String team1 = match.getParticipants().get(0).getName();
        team1Text.setText(team1);
        String team2 = match.getParticipants().get(1).getName();
        team2Text.setText(team2);
    }
}