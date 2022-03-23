package edu.ntnu.idatt1002.k1g01.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controls the create a tournament page
 */
public class CreateATournamentController implements Initializable {

    @FXML private TextField nameInputField;
    @FXML private ChoiceBox<String> tournamentTypeInput;
    @FXML private ChoiceBox<Integer> teamsPerGroupInput;
    @FXML private ChoiceBox<Integer> teamsAdvancingFromGroupInput;
    @FXML private ChoiceBox<String> matchTypeInput;

    /**
     * Changes the scene to CreateATournamentWindow
     */
    @FXML
    public void cancelBackToHomePage(ActionEvent event)throws IOException {
        try {
            Parent createTournament = FXMLLoader.load(getClass().getResource("../HomePage.fxml"));
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
        //Adding tournamentTypes values to choicebox
        tournamentTypeInput.getItems().add("Knockout Stage");
        tournamentTypeInput.getItems().add("Group Stage + Knockout Stage");
        tournamentTypeInput.setValue("Knockout Stage");

        //Adding teamsPerGroup values to choicebox
        teamsPerGroupInput.getItems().add(2);
        teamsPerGroupInput.getItems().add(4);
        teamsPerGroupInput.getItems().add(6);
        teamsPerGroupInput.getItems().add(8);
        teamsPerGroupInput.setValue(4);

        //Adding teamsAdvancingFromGroup values to choicebox
        teamsAdvancingFromGroupInput.getItems().add(1);
        teamsAdvancingFromGroupInput.getItems().add(2);
        teamsAdvancingFromGroupInput.getItems().add(3);
        teamsAdvancingFromGroupInput.getItems().add(4);
        teamsAdvancingFromGroupInput.setValue(2);

        //Adding matchType values to choicebox
        matchTypeInput.getItems().add("Point match");
        matchTypeInput.getItems().add("Time match");
        matchTypeInput.setValue("Point match");
    }

    public void addTeam(ActionEvent actionEvent) {
    }

    public void createTournament(ActionEvent actionEvent) {
        String tournamentName =  nameInputField.getText();
        String tournamentType = tournamentTypeInput.getValue();
        
        System.out.println(tournamentName);
        System.out.println(tournamentType);

    }
}
