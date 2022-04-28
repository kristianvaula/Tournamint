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

    // Increment buttons
    @FXML private Button team1IncrementHour, team1IncrementMin, team1IncrementSec, team1IncrementNano;
    @FXML private Button team1DecrementHour, team1DecrementMin, team1DecrementSec, team1DecrementNano;
    @FXML private Button team2IncrementHour, team2IncrementMin, team2IncrementSec, team2IncrementNano;
    @FXML private Button team2DecrementHour, team2DecrementMin, team2DecrementSec, team2DecrementNano;

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
            TextField[] resultFields = {team1Hours, team1Min, team1Sec, team1Nano, team2Hours, team2Min, team2Sec, team2Nano};
            for (TextField f : resultFields) f.setDisable(true);
            Button[] buttons = {team1IncrementHour, team1IncrementMin, team1IncrementSec, team1IncrementNano,
                    team1DecrementHour, team1DecrementMin, team1DecrementSec, team1DecrementNano,
                    team2IncrementHour, team2IncrementMin, team2IncrementSec, team2IncrementNano,
                    team2DecrementHour, team2DecrementMin, team2DecrementSec, team2DecrementNano};
            for (Button b : buttons) { b.setDisable(true); b.setStyle("-fx-opacity:0.4"); }
        }


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
    public void confirmResultsAndGoBack(ActionEvent event) throws IOException {
        try{
            String resultTeam1 = getResult("Team1");
            String resultTeam2 = getResult("Team2");

            if(resultTeam1 != null && resultTeam2 !=null){
                System.out.println(resultTeam1);
                System.out.println(resultTeam2);
                match.setResult(match.getParticipants().get(0),resultTeam1);
                match.setResult(match.getParticipants().get(1),resultTeam2);
            }

            match.setMatchDate(dateField.getValue());
            match.setMatchInfo(infoField.getText());
            try{
                if(!(timeField.getText().isEmpty() || timeField.getText().isBlank())) {
                    LocalTime newValue = LocalTime.parse(timeField.getText());
                    match.setStartTime(newValue);

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
        catch (IllegalArgumentException e){
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.show();
        }
    }

    /**
     * Gets result from input boxes and turns into
     * string. Checks all the different inputs for
     * bad values.
     *
     * @param team Which inputs we are checking
     * @return the result
     * @throws IllegalArgumentException if numbers are negative or to high
     * @throws NumberFormatException if there are illegal characters in inputs
     */
    public String getResult(String team) throws IllegalArgumentException, NumberFormatException{
        try{
            if(team.equals("Team1")){
                if(Integer.parseInt(team1Hours.getText()) > 23 || Integer.parseInt(team1Hours.getText()) < 0){
                    throw new IllegalArgumentException("Hour must be between 0 and 23");
                }
                else if(Integer.parseInt(team1Min.getText()) > 59 || Integer.parseInt(team1Min.getText()) < 0){
                    throw new IllegalArgumentException("Minutes must be between 0 and 59");
                }
                else if(Integer.parseInt(team1Sec.getText()) > 59 || Integer.parseInt(team2Sec.getText()) < 0){
                    throw new IllegalArgumentException("Seconds must be between 0 and 59");
                }
                else if(Integer.parseInt(team1Nano.getText()) > 9999 || Integer.parseInt(team2Nano.getText()) < 0){
                    throw new IllegalArgumentException("Nanoseconds must be between 0 and 9999");
                }
                String result = team1Hours.getText() + ":" + team1Min.getText() + ":" + team1Sec.getText() + ":" + team1Nano.getText();
                if(result.matches("0:")) return null;
                return result;
            }
            else if(team.equals("Team2")){
                if(Integer.parseInt(team2Hours.getText()) > 23 || Integer.parseInt(team2Hours.getText()) < 0){
                    throw new IllegalArgumentException("Hour must be between 0 and 23");
                }
                else if(Integer.parseInt(team2Min.getText()) > 59 || Integer.parseInt(team2Min.getText()) < 0){
                    throw new IllegalArgumentException("Minutes must be between 0 and 59");
                }
                else if(Integer.parseInt(team2Sec.getText()) > 59 || Integer.parseInt(team2Sec.getText()) < 0){
                    throw new IllegalArgumentException("Seconds must be between 0 and 59");
                }
                else if(Integer.parseInt(team2Nano.getText()) > 9999 || Integer.parseInt(team2Nano.getText()) < 0){
                    throw new IllegalArgumentException("Nanoseconds must be between 0 and 9999");
                }
                String result = team2Hours.getText() + ":" + team2Min.getText() + ":" + team2Sec.getText() + ":" + team2Nano.getText();
                if(result.matches("^[0:]+$")) return null;
                return result;
            }
        }catch (NumberFormatException e){
            e.printStackTrace();
            throw e;
        }

        return null;
    }
}