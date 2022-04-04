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
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Controls the enter results page
 *
 * @author thomaniv
 * @author espjus
 * @author martdam
 */
public class EnterResultsController implements Initializable {

    //The nested controller for the menuBar
    @FXML private TopMenuBarController topMenuBarController;

    private TournamentDAO tournamentDAO;
    private Tournament tournament;
    private Match match;

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
    public void initialize(URL url, ResourceBundle resourceBundle){
    }

    @FXML
    public void initData(Match match, TournamentDAO tournamentDAO) throws IOException{
        this.tournamentDAO = tournamentDAO;
        this.tournament = tournamentDAO.load(); //TODO handle the exception from this better.
        topMenuBarController.setTournamentDAO(tournamentDAO);//Give menuBar controller access to the DAO.
        this.match = match;
        // Sets the team-names
        String team1 = match.getParticipants().get(0).getName();
        team1Text.setText(team1);
        String team2 = match.getParticipants().get(1).getName();
        team2Text.setText(team2);
        if(match.getMatchResult().size()>0 ){
            resultInputField1.setText(match.getMatchResult().get(match.getParticipants().get(0)));
            resultInputField2.setText(match.getMatchResult().get(match.getParticipants().get(1)));
        }else{
            resultInputField1.setText("0");
            resultInputField2.setText("0");
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
     * Updates all tye match data and information, then
     * Changes the scene to AdministrateTournament
     */
    @FXML
    public void confirmResultsAndGoBack(ActionEvent event) throws IOException,NoSuchFieldException{
        match.setResult(match.getParticipants().get(0),String.valueOf(resultInputField1.getText()));
        match.setResult(match.getParticipants().get(1),String.valueOf(resultInputField2.getText()));
        //System.out.println("result one: " + String.valueOf(resultInputField1));
        //System.out.println("result two: " + String.valueOf(resultInputField2));

        match.setMatchDate(dateField.getValue());
        match.setMatchInfo(infoField.getText());
        try{
            if(!(timeField.getText().isEmpty() || timeField.getText().isBlank())) {
                match.setStartTime(LocalTime.parse((CharSequence) timeField.getText()));
            }
        }catch (DateTimeParseException e){
            System.out.println(e.getMessage());
        }
        tournament.updateTournament();
        tournamentDAO.save(); //Save changes to tournament.

        returnToAdministrateTournament(event);
    }
}