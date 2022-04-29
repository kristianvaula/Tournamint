package edu.ntnu.idatt1002.k1g01.controller;

import edu.ntnu.idatt1002.k1g01.dao.TournamentDAO;
import edu.ntnu.idatt1002.k1g01.model.Team;
import edu.ntnu.idatt1002.k1g01.model.Tournament;
import edu.ntnu.idatt1002.k1g01.model.matches.Match;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;


/**
 * Controls the enter multi team results page
 * @author kristvje
 */
public class EnterMultiTeamResultsController implements Initializable {

    //The nested controller for the menuBar
    @FXML private TopMenuBarController topMenuBarController;

    //Attributes
    private TournamentDAO tournamentDAO;
    private Tournament tournament;
    private Match match;

    //Table fields
    @FXML private VBox teamVBox;

    //General Input Fields
    @FXML private DatePicker dateField;
    @FXML private TextField timeField;
    @FXML private TextField infoField;

    /**
     * Initializes page
     *
     * @param url url
     * @param resourceBundle src bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

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
        this.tournament = tournamentDAO.load(); //TODO handle the exception from this better.
        topMenuBarController.setTournamentDAO(tournamentDAO);//Give menuBar controller access to the DAO.
        this.match = match;

        //Other fields
        timeField.setText(match.getStartTimeAsString());
        if(match.getMatchDate() != null){
            dateField.setValue(match.getMatchDate());
        }
        infoField.setText(match.getMatchInfo());

        HBox hbox = new HBox();
        hbox.setStyle("-fx-background-color: gray");
        hbox.prefHeight(22.0);
        Label teams = new Label("  Teams");
        teams.prefHeight(22.0);
        teams.setMinWidth((teamVBox.getPrefWidth()*40)/100);
        Label results = new Label("  Results");
        results.prefHeight(22.0);
        results.setMinWidth((teamVBox.getPrefWidth()*50)/100);
        results.setStyle("-fx-background-color: gray");
        hbox.getChildren().add(teams);
        hbox.getChildren().add(results);
        teamVBox.getChildren().add(hbox);

        displayAllTeams();
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
     * Updates all tye match data and information, then
     * Changes the scene to AdministrateTournament
     */
    @FXML
    public void confirmResultsAndGoBack(ActionEvent event) throws IOException,NoSuchFieldException{
        if(checkIfAllResults()){
            try {
                updateAndSetResults();
                if(!(timeField.getText().isEmpty() || timeField.getText().isBlank())) {
                    match.setStartTime(LocalTime.parse(timeField.getText()));
                }
                match.setMatchDate(dateField.getValue());
                match.setMatchInfo(infoField.getText());

                tournamentDAO.save(); //Save changes to tournament.
                returnToAdministrateTournament(event);

            }catch (NumberFormatException e){
                System.out.println(e.getMessage());
                Alert alert = new Alert(Alert.AlertType.ERROR);
                if(tournament.getMatchType().equals("timeMatch")){
                    alert.setContentText("Result must be on format HH:MM:SS:NN");
                }
                else{
                    alert.setContentText("Result must be a number");
                }
                alert.show();
            }catch (DateTimeParseException e){
                System.out.println(e.getMessage());
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Date was not valid");
                alert.show();
            }catch (Exception e){
                System.out.println(e.getMessage());
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText(e.getMessage());
                alert.show();
        }

        }else {
            System.out.println("ERROR ALERT");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please enter a value for all teams");
            alert.show();
        }
    }

    @FXML
    public boolean checkIfAllResults(){
        boolean allResults = true;
        for(Node hBox : teamVBox.getChildren()){
            if(hBox instanceof HBox){
                for(Node textField : ((HBox)hBox).getChildren()){
                    if(textField instanceof TextField){
                        if(((TextField) textField).getText()== null){
                            allResults = false;
                        }
                    }
                }
            }
        }
        return allResults;
    }

    /**
     * Runs through all match contestants and displays their
     * name as well as their result
     */
    private void displayAllTeams() {
        int i = 1;
        for(Team team : match.getParticipants()){
            Label teamName = new Label(team.getName());
            TextField result = new TextField();
            if (!match.isPlayable()) result.setDisable(true); // Disable if match is not yet playable.
            if(match.getMatchResult() != null){
                if(match.getMatchResult().get(team) != null){
                    result.setText(match.getMatchResult().get(team));
                }
            }
            if(tournament.getMatchType().equals("timeMatch")){
                result.setPromptText(" HH:MM:SS:NN ");
            }
            else if(tournament.getMatchType().equals("pointMatch")){
                result.setPromptText(" score ");
            }

            HBox hbox = new HBox();
            hbox.setPrefHeight(30.0);
            teamName.setPrefHeight(30.0);
            teamName.setMinWidth((teamVBox.getPrefWidth()*40)/100);
            teamName.getStyleClass().add("label-multiTeam");

            teamName.setPadding(new Insets(5,0,5,10));

            result.setPrefHeight(30.0);
            result.setMinWidth((teamVBox.getPrefWidth()*50)/100);
            result.setId(team.getName());
            result.getStyleClass().add("label-multiTeam");
            result.setPadding(new Insets(5,0,5,0));

            String background;
            if(i%2==0){
                background = "-fx-background-color: #ffffff";
            }else {
                background = "-fx-background-color: #f2f2f2";
            }
            hbox.setStyle(background);
            result.setStyle(background);
            hbox.getChildren().add(teamName);
            hbox.getChildren().add(result);
            teamVBox.getChildren().add(hbox);
            i++;
        }
    }

    @FXML
    public void updateAndSetResults() throws NumberFormatException, IllegalStateException{
        //Check if any results have been set, and if all results are set.
        boolean hasBlank = false;
        boolean hasResult = false;
        for(Node hBox : teamVBox.getChildren()){
            if(hBox instanceof HBox) {
                for (Node textField : ((HBox) hBox).getChildren()) {
                    if (textField instanceof TextField) {
                        if (((TextField) textField).getText().isBlank()) hasBlank = true;
                        else hasResult = true;
                    }
                }
            }
        }

        //Attempt to actually save results if there are no blank fields.
        //Throw error if some fields are blank.
        if (hasResult) {
            if (hasBlank) throw new IllegalStateException("Cannot save if only some results are entered");
            for(Node hBox : teamVBox.getChildren()){
                if(hBox instanceof HBox){
                    for(Node textField : ((HBox)hBox).getChildren()){
                        if(textField instanceof TextField){
                            Team team = match.getTeamByName(textField.getId());
                            String value = ((TextField) textField).getText();
                            match.setResult(team, value);
                        }
                    }
                }
            }
        }
    }
}
