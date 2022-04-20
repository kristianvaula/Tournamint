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
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.DateTimeException;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;

/**
 * Controls the enter time results page
 *
 * @author kristvje
 */
public class EnterTimeResultsController implements Initializable {
    //The nested controller for the menuBar
    @FXML private TopMenuBarController topMenuBarController;

    private TournamentDAO tournamentDAO;
    private Tournament tournament;
    private Match match;

    // Team 1 Result Fields
    @FXML private Text team1Text;
    @FXML private TextField team1Hours;
    @FXML private TextField team1Min;
    @FXML private TextField team1Sec;
    @FXML private TextField team1Nano;

    // Team 2 Result Fields
    @FXML private Text team2Text;
    @FXML private TextField team2Hours;
    @FXML private TextField team2Min;
    @FXML private TextField team2Sec;
    @FXML private TextField team2Nano;

    // General Input Fields
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

    /**
     * Used by other controllers to initialize important
     * data before we open the page
     * @param match match we are going to enter results to
     * @param tournamentDAO the TournamentDAO
     * @throws IOException if load fails
     */
    @FXML
    public void initData(Match match, TournamentDAO tournamentDAO) throws IOException{
        this.tournamentDAO = tournamentDAO;
        this.tournament = tournamentDAO.load(); //TODO handle the exception from this better.
        topMenuBarController.setTournamentDAO(tournamentDAO);//Give menuBar controller access to the DAO.
        this.match = match;

        // Sets up the current match info
        // Names
        Team team1 = match.getParticipants().get(0);
        Team team2 = match.getParticipants().get(1);
        team1Text.setText(team1.getName());
        team2Text.setText(team2.getName());
        //Results
        if(match.getMatchResult() != null){
            if(match.getMatchResult().get(team1) != null){
                String[] team1Result = match.getMatchResult().get(team1).split(":");
                team1Hours.setText(team1Result[0]);
                team1Min.setText(team1Result[1]);
                team1Sec.setText(team1Result[2]);
                team1Nano.setText(team1Result[3]);
            }
            if(match.getMatchResult().get(team2) != null){
                String[] team2Result = match.getMatchResult().get(team2).split(":");
                team2Hours.setText(team2Result[0]);
                team2Min.setText(team2Result[1]);
                team2Sec.setText(team2Result[2]);
                team2Nano.setText(team2Result[3]);
            }
        }else{
            team1Hours.setText("00");
            team1Min.setText("00");
            team1Sec.setText("00");
            team1Nano.setText("00");
            team2Hours.setText("00");
            team2Min.setText("00");
            team2Sec.setText("00");
            team2Nano.setText("00");
        }
        //Other fields
        timeField.setText(match.getStartTimeAsString());
        if(match.getMatchDate() != null){
            dateField.setValue(match.getMatchDate());
        }
        infoField.setText(match.getMatchInfo());
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
     * Increments or decrements teams values
     * by checking which button was clicked
     */
    @FXML
    public void incrementValueEvent(ActionEvent event) {
        Button buttonPressed = (Button)event.getSource();
        String buttonID = buttonPressed.getId();
        boolean decrement = !buttonID.contains("Increment");
        if(buttonID.contains("Hour")){
            if(buttonID.contains("team1")){
                if(decrement && Integer.parseInt(team1Hours.getText()) > 0){
                    team1Hours.setText(String.valueOf(Integer.parseInt(team1Hours.getText()) - 1));
                }else if(!decrement) {
                    team1Hours.setText(String.valueOf(Integer.parseInt(team1Hours.getText()) + 1));
                }

            }else{
                if(decrement && Integer.parseInt(team2Hours.getText()) > 0){
                    team2Hours.setText(String.valueOf(Integer.parseInt(team2Hours.getText()) - 1));
                }else if(!decrement) {
                    team2Hours.setText(String.valueOf(Integer.parseInt(team2Hours.getText()) + 1));
                }
            }
        }
        else if(buttonID.contains("Min")) {
            if (buttonID.contains("team1")){
                if (decrement && Integer.parseInt(team1Min.getText()) > 0) {
                    team1Min.setText(String.valueOf(Integer.parseInt(team1Min.getText()) - 1));
                } else if(!decrement){
                    team1Min.setText(String.valueOf(Integer.parseInt(team1Min.getText()) + 1));
                }

            } else {
                if (decrement && Integer.parseInt(team2Min.getText()) > 0) {
                    team2Min.setText(String.valueOf(Integer.parseInt(team2Min.getText()) - 1));
                } else if(!decrement){
                    team2Min.setText(String.valueOf(Integer.parseInt(team2Min.getText()) + 1));
                }
            }
        }
        else if(buttonID.contains("Sec")) {
            if (buttonID.contains("team1")) {
                if (decrement && Integer.parseInt(team1Sec.getText()) > 0) {
                    team1Sec.setText(String.valueOf(Integer.parseInt(team1Sec.getText()) - 1));
                } else if(!decrement){
                    team1Sec.setText(String.valueOf(Integer.parseInt(team1Sec.getText()) + 1));
                }

            } else {
                if (decrement && Integer.parseInt(team2Sec.getText()) > 0) {
                    team2Sec.setText(String.valueOf(Integer.parseInt(team2Sec.getText()) - 1));
                } else if(!decrement){
                    team2Sec.setText(String.valueOf(Integer.parseInt(team2Sec.getText()) + 1));
                }
            }
        }
        else if(buttonID.contains("Nano")) {
            if (buttonID.contains("team1")) {
                if (decrement && Integer.parseInt(team1Nano.getText()) > 0) {
                    team1Nano.setText(String.valueOf(Integer.parseInt(team1Nano.getText()) - 1));
                } else if(!decrement) {
                    team1Nano.setText(String.valueOf(Integer.parseInt(team1Nano.getText()) + 1));
                }

            } else {
                if (decrement && Integer.parseInt(team2Nano.getText()) > 0) {
                    team2Nano.setText(String.valueOf(Integer.parseInt(team2Nano.getText()) - 1));
                }else if(!decrement ){
                    team2Nano.setText(String.valueOf(Integer.parseInt(team2Nano.getText()) + 1));
                }
            }
        }
    }

    /**
     * Updates all tye match data and information, then
     * Changes the scene to AdministrateTournament
     */
    @FXML
    public void confirmResultsAndGoBack(ActionEvent event) throws IOException,NoSuchFieldException{
        String team1Result = team1Hours.getText() + ":" + team1Min.getText() + ":" + team1Sec.getText() + ":" + team1Nano.getText();
        String team2Result = team2Hours.getText() + ":" + team2Min.getText() + ":" + team2Sec.getText() + ":" + team2Nano.getText();

        try{
            match.setResult(match.getParticipants().get(0),team1Result);
            match.setResult(match.getParticipants().get(1),team2Result);

            match.setMatchDate(dateField.getValue());
            match.setMatchInfo(infoField.getText());
            try{
                if(!(timeField.getText().isEmpty() || timeField.getText().isBlank())) {
                    match.setStartTime(LocalTime.parse((CharSequence) timeField.getText()));
                }
                //EXIT FROM RESULT
                tournament.updateTournament();
                tournamentDAO.save(); //Save changes to tournament.
                returnToAdministrateTournament(event);
            }catch (DateTimeParseException e){
                System.out.println(e.getMessage());
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Please check time");
                alert.show();
            }
        }
        catch (NumberFormatException e){
            System.out.println(e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please check numbers");
            alert.show();
        }
        catch (DateTimeException e){
            System.out.println(e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Results cannot have negative values");
            alert.show();
        }


    }
}