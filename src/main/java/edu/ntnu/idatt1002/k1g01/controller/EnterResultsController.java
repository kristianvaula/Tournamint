package edu.ntnu.idatt1002.k1g01.controller;

import edu.ntnu.idatt1002.k1g01.model.Team;
import edu.ntnu.idatt1002.k1g01.model.matches.Match;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Controls the enter results page
 */
public class EnterResultsController implements Initializable {

    private Match match;
    private ArrayList<Team> participants = new ArrayList<>();

    // TeamNames
    @FXML private Text team1Text;
    @FXML private Text team2Text;

    // InputFields
    @FXML private TextField resultInputField1;
    @FXML private TextField resultInputField2;
    @FXML private DatePicker dateField;
    @FXML private TextField timeField;
    @FXML private TextField infoField;

    /**
     * Initializes EnterResult
     * @param url url
     * @param resourceBundle resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Sets the team-names
        String team1 = match.getParticipants().get(0).getName();
        team1Text.setText(team1);
        String team2 = match.getParticipants().get(1).getName();
        team2Text.setText(team2);

        // Sets the initial result of both teams to 0
        String initialResult = "0";
        resultInputField1.setText(initialResult);
        resultInputField2.setText(initialResult);
    }

    /**
     * Changes the scene to AdministrateTournament
     */
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

    /**
     * Increments team1 points by one
     */
    @FXML
    public void incrementTeam1PointByOne() {
        String result = resultInputField1.getText();
        int value = Integer.parseInt(result);
        value++;
        resultInputField1.setText(String.valueOf(value));
    }

    /**
     * Decrements team1 points by one
     */
    @FXML
    public void incrementTeam2PointByOne() {
        String result = resultInputField2.getText();
        int value = Integer.parseInt(result);
        value++;
        resultInputField2.setText(String.valueOf(value));
    }

    /**
     * Increments team2 points by one
     */
    @FXML
    public void decrementTeam1PointByOne() {
        String result = resultInputField1.getText();
        int value = Integer.parseInt(result);
        value--;
        resultInputField1.setText(String.valueOf(value));
    }

    /**
     * Decrements team2 points by one
     */
    @FXML
    public void decrementTeam2PointByOne() {
        String result = resultInputField2.getText();
        int value = Integer.parseInt(result);
        value--;
        resultInputField2.setText(String.valueOf(value));
    }

    /**
     * Get the results from all participants in match in a list
     * @return results An ArrayList holding String objects
     */
    @FXML
    public ArrayList<String> getResults() {
        ArrayList<String> results = new ArrayList<>();
        results.add(String.valueOf(resultInputField1));
        results.add(String.valueOf(resultInputField2));
        return results;
    }

    /**
     * Get the date of the match
     * @return date. A LocalDate object
     */
    @FXML
    public LocalDate getDate() {
        LocalDate date = dateField.getValue();
        return date;
    }

    /**
     * Get the time of the match
     * @return time. A LocalTime object
     */
    @FXML
    public LocalTime getTime() {
        LocalTime time = LocalTime.parse((CharSequence) timeField);
        return time;
    }

    /**
     * Get match info
     * @return the text in the info input field
     */
    @FXML
    public String getInfo() {
        return infoField.getText();
    }

    /**
     * Updates all tye match data and information, then
     * Changes the scene to AdministrateTournament
     */
    @FXML
    public void confirmResultsAndGoBack(ActionEvent event) throws IOException{
        try {
            // Updates match data and information
            for (int i = 0; i < participants.size(); i++) {
                match.setResult(participants.get(i), getResults().get(i));
            }
            match.setMatchDate(getDate());
            match.setStartTime(getTime());
            match.setMatchInfo(getInfo());

            // Goes back to TournamentAdministrator page
            Parent enterResults = FXMLLoader.load(getClass().getResource("../AdministrateTournament.fxml"));
            Scene enterResultsScene = new Scene(enterResults);

            Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
            window.setScene(enterResultsScene);
            window.show();

        } catch (IOException e) {
            System.out.println(e.getCause());
            throw e;
        }
    }
}