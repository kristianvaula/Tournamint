package edu.ntnu.idatt1002.k1g01.controller;

import edu.ntnu.idatt1002.k1g01.dao.TournamentDAO;
import edu.ntnu.idatt1002.k1g01.model.Team;
import edu.ntnu.idatt1002.k1g01.model.Tournament;
import edu.ntnu.idatt1002.k1g01.model.matches.Match;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Controls the enter points results page
 * @author thomaniv
 * @author espjus
 * @author martdam
 * @author kristvje
 */
public class EnterPointResultsController implements Initializable {

    //The nested controller for the menuBar
    @FXML private TopMenuBarController topMenuBarController;

    //Attributes
    private TournamentDAO tournamentDAO;
    private Tournament tournament;
    private Match match;

    // TeamNames
    @FXML private Label team1Text;
    @FXML private Label team2Text;

    // Result Fields
    @FXML private TextField resultInputField1;
    @FXML private TextField resultInputField2;

    // Increment buttons
    @FXML private Button team1PlusButton;
    @FXML private Button team2PlusButton;
    @FXML private Button team1MinusButton;
    @FXML private Button team2MinusButton;

    //General Input Fields
    @FXML private DatePicker dateField;
    @FXML private TextField timeField;
    @FXML private TextField infoField;

    /**
     * Initializes EnterResult
     *
     * @param url url
     * @param resourceBundle resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
    }

    /**
     * Used by other controllers to initialize important
     * data before we open the page
     *
     * @param match match we are going to enter results to
     * @param tournamentDAO the TournamentDAO
     * @throws IOException if load fails
     */
    @FXML
    public void initData(Match match, TournamentDAO tournamentDAO) throws IOException{
        this.tournamentDAO = tournamentDAO;
        this.tournament = tournamentDAO.load();
        topMenuBarController.setTournamentDAO(tournamentDAO);//Give menuBar controller access to the DAO.
        this.match = match;

        // Disable all result inputs if match is not yet playable.
        if (!match.isPlayable()) {
            resultInputField1.setDisable(true);
            resultInputField2.setDisable(true);
            Button[] iterateButtons = {team1PlusButton, team2PlusButton, team1MinusButton, team2MinusButton};
            for (Button b : iterateButtons) { b.setDisable(true); b.setStyle("-fx-opacity:0.4"); }
        }

        // Sets up the current match info
        // Names
        Team team1 = match.getParticipants().get(0);
        Team team2 = match.getParticipants().get(1);
        team1Text.setText(team1.getName());
        team2Text.setText(team2.getName());
        //Results
        if(match.getMatchResult().size()>0 ){
            resultInputField1.setText(match.getMatchResult().get(team1));
            resultInputField2.setText(match.getMatchResult().get(team2));
        }
        //Other fields
        timeField.setText(match.getStartTimeAsString());
        if(match.getMatchDate() != null){
            dateField.setValue(match.getMatchDate());
        }
        infoField.setText(match.getMatchInfo());
    }

    /**
     * Sets any resultField not parsable as Integer to "0".
     */
    @FXML
    private void initResultFields() {
        TextField[] resultFields = {resultInputField1, resultInputField2};
        for (TextField field : resultFields) {
            try { Integer.parseInt(field.getText()); }
            catch (NumberFormatException e) {
                field.setText("0");
            }
        }
    }

    /**
     * Changes the scene to AdministrateTournament
     */
    @FXML
    public void returnToAdministrateTournament (ActionEvent event)throws IOException {
        try {
            // Goes back to TournamentAdministrator page
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../view/AdministrateTournament.fxml"));
            Parent enterResults = loader.load();

            Scene enterResultsScene = new Scene(enterResults);
            enterResultsScene.setUserData(loader);

            AdministrateTournamentController controller = loader.getController();
            controller.initData(tournamentDAO);

            Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
            window.setScene(enterResultsScene);
            window.show();

        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    /**
     * Increments team1 points by one
     */
    @FXML
    public void incrementTeam1PointByOne() {
        initResultFields();
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
        initResultFields();
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
        initResultFields();
        String result = resultInputField1.getText();
        int value = Integer.parseInt(result);
        if(value > 0) value--;
        resultInputField1.setText(String.valueOf(value));
    }

    /**
     * Decrements team2 points by one
     */
    @FXML
    public void decrementTeam2PointByOne() {
        initResultFields();
        String result = resultInputField2.getText();
        int value = Integer.parseInt(result);
        if(value > 0) value--;
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
     * Updates all the match data and information, then
     * Changes the scene to AdministrateTournament
     */
    @FXML
    public void confirmResultsAndGoBack(ActionEvent event) throws IOException {
        try{
            if (resultInputField1.getText().length() > 0 || resultInputField2.getText().length() > 0 ) {
                match.setResult(match.getParticipants().get(0),String.valueOf(resultInputField1.getText()));
                match.setResult(match.getParticipants().get(1),String.valueOf(resultInputField2.getText()));
            }

            match.setMatchDate(dateField.getValue());
            match.setMatchInfo(infoField.getText());
            try{
                if(!(timeField.getText().isEmpty() || timeField.getText().isBlank())) {
                    match.setStartTime(LocalTime.parse(timeField.getText()));
                }
            }catch (DateTimeParseException e){
                System.out.println(e.getMessage());
            }
            tournament.updateTournament();
            tournamentDAO.save(); //Save changes to tournament.

            returnToAdministrateTournament(event);
        }
        catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.show();
        }
    }
}