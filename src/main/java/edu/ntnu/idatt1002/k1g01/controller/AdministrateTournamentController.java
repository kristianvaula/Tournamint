package edu.ntnu.idatt1002.k1g01.controller;

import edu.ntnu.idatt1002.k1g01.dao.TournamentDAO;
import edu.ntnu.idatt1002.k1g01.model.Round;
import edu.ntnu.idatt1002.k1g01.model.Tournament;
import edu.ntnu.idatt1002.k1g01.model.matches.Match;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Controls administration of tournament
 *
 * @author kristjve
 * @author martdam
 */
public class AdministrateTournamentController implements Initializable {

    //The nested controller for the menuBar
    @FXML private TopMenuBarController topMenuBarController;

    //The tournament variables
    private Tournament tournament;
    private TournamentDAO tournamentDAO;

    //General Settings
    @FXML Text tournamentNameOutput;

    //TabPane and TableView settings
    @FXML TableView<Match> matchTable;
    @FXML TableColumn<Match,String> teamsColumn;
    @FXML TableColumn<Match,String> resultColumn;
    @FXML TableColumn<Match,String> dateColumn;
    @FXML TableColumn<Match,String> timeColumn;
    @FXML TableColumn<Match,String> infoColumn;

    @FXML MenuBar topMenuBar;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){

        //Preparing match table
        teamsColumn.setCellValueFactory(new PropertyValueFactory<>("participantsAsString"));
        resultColumn.setCellValueFactory(new PropertyValueFactory<>("resultAsString"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("MatchDateAsString"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("StartTimeAsString"));
        infoColumn.setCellValueFactory(new PropertyValueFactory<>("matchInfo"));
        matchTable.setEditable(false);

        matchTable.setRowFactory(tableView -> {
            TableRow<Match> row = new TableRow<>();

            row.setOnMouseClicked(event ->{
                if (!row.isEmpty() && event.getButton()== MouseButton.PRIMARY
                        && event.getClickCount() == 2) {
                    Match match = row.getItem();
                    enterResultEvent(event, match);
                }
            });
            return row;
        });
    }

    /**
     * Switches back to the start page.
     */
    @FXML
    public void switchToHomePage() {
        try {
            Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../view/HomePage.fxml")));
            Scene scene = new Scene(parent);

            //This line gets the Stage information
            Stage window = (Stage) topMenuBar.getScene().getWindow();
            window.setScene(scene);
            window.show();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Opens another tournament from file.
     */
    @FXML
    public void openFile() {
        try { tournamentDAO = FileController.openFromFile( (Stage)topMenuBar.getScene().getWindow()); }
        catch (Exception e) { System.out.println(e.getMessage()); }
        initData(tournamentDAO);
    }

    /**
     * Saves tournament to new file and switches working directory.
     */
    @FXML
    public void saveFile() {
        try {  tournamentDAO = FileController.saveToFile(tournament, (Stage)topMenuBar.getScene().getWindow()); }
        catch (Exception e) { System.out.println(e.getMessage()); }
    }


    /**
     * Starts session for administrating a tournament with TournamentDAO.
     * Makes file containing tournament accessible so that it can be easily updated frequently.
     * TODO give user reassuring feedback whenever tournament file is updated.
     * TODO better user feedback for errors.
     * @param tournamentDAO DAO for tournament object. Must be non-null.
     */
    @FXML
    public void initData(TournamentDAO tournamentDAO){
        this.tournamentDAO = tournamentDAO;
        topMenuBarController.setTournamentDAO(tournamentDAO); //Pass DAO pointer to nested controller.
        try {
            this.tournament = tournamentDAO.load();
        }
        catch (IOException ioException) {
            System.out.println("Error in initData: " + ioException.getMessage());
            //TODO handle exception if loading somehow fails. Should not be possible at this point.
        }
        tournamentNameOutput.setText(tournament.getTournamentName());
        displayAllMatches();
    }

    /**
     * Whenever a result is added we call the
     * updateTournament function so that the
     * tournament can generate new matches based
     * on previous result.
     */
    public void updateTournament(){
        this.tournament.updateTournament();
    }

    /**
     * Sets table rows to all matches
     */
    @FXML
    public void displayAllMatches(){
        ObservableList<Match> matchesObservable = FXCollections.observableArrayList();

        for(Round round : tournament.getAllRounds()){
            matchesObservable.addAll(round.getMatches());
        }
        matchTable.setItems(matchesObservable);
    }

    /**
     * Sets table rows to all games without results
     */
    @FXML
    public void displayUpcomingMatches(){
        ObservableList<Match> matchesObservable = FXCollections.observableArrayList();

        for(Round round : tournament.getAllRounds()){
            ArrayList<Match> matches = round.getMatches();
            for(Match match : matches){
                if(match.getMatchResult() == null){
                    matchesObservable.add(match);
                }
                else if(match.getMatchResult().isEmpty()){
                    matchesObservable.add(match);
                }
            }
        }
        matchTable.setItems(matchesObservable);
    }

    /**
     * Sets table rows to all games with results
     */
    @FXML
    public void displayPreviousMatches(){
        ObservableList<Match> matchesObservable = FXCollections.observableArrayList();

        for(Round round : tournament.getAllRounds()){
            ArrayList<Match> matches = round.getMatches();
            for(Match match : matches){
                if(match.getMatchResult() != null){
                    if(!match.getMatchResult().isEmpty()){
                        matchesObservable.add(match);
                    }
                }
            }
        }
        matchTable.setItems(matchesObservable);
    }

    /**
     * Changes the scene to changeSceneToEnterMatchResult
     * Also uses the EnterResultsController tournament controller to
     * send the match instance.
     */
    @FXML
    public void enterResultEvent(MouseEvent event,Match match){
        try {
            FXMLLoader loader = new FXMLLoader();
            System.out.println(tournament.getMatchType());
            if(tournament.getMatchType().equals("timeMatch")){ // Which controller we are using
                loader.setLocation(getClass().getResource("../view/EnterTimeResults.fxml"));
            }else{
                loader.setLocation(getClass().getResource("../view/EnterPointResults.fxml"));
            }

            Parent administrateParent = loader.load();
            Scene administrateScene = new Scene(administrateParent);

            //Access the controller and call a method
            if(tournament.getMatchType().equals("timeMatch")){ // Which controller we are using
                EnterTimeResultsController controller = loader.getController();
                controller.initData(match,tournamentDAO);
            }else{
                EnterPointResultsController controller = loader.getController();
                controller.initData(match,tournamentDAO);
            }

            Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
            window.setScene(administrateScene);
            window.show();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}