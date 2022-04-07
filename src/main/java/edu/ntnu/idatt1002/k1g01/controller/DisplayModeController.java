package edu.ntnu.idatt1002.k1g01.controller;

import edu.ntnu.idatt1002.k1g01.dao.TournamentDAO;
import edu.ntnu.idatt1002.k1g01.model.Group;
import edu.ntnu.idatt1002.k1g01.model.Round;
import edu.ntnu.idatt1002.k1g01.model.Team;
import edu.ntnu.idatt1002.k1g01.model.Tournament;
import edu.ntnu.idatt1002.k1g01.model.matches.Match;
import edu.ntnu.idatt1002.k1g01.model.stages.KnockoutStage;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class DisplayModeController implements Initializable {

    //The tournament variables
    private Tournament tournament;
    private TournamentDAO tournamentDAO;

    private ArrayList<BracketRoundContainerController> roundControllers = new ArrayList<>();
    private ArrayList<BracketGroupContainerController> groupStageControllers = new ArrayList<>();

    private final static String DLM = File.separator;

    //The nested controller for the menuBar
    @FXML private TopMenuBarController topMenuBarController;

    //Tabpane
    @FXML private TabPane displayTabPane;

    //Tab Upcoming Matches
    @FXML private Tab upcomingMatchesTab;
    @FXML private TableView<Match> upcomingMatchesTable;
    @FXML private TableColumn<Match,String> upcomTeamsColumn;
    @FXML private TableColumn<Match,String> upcomResultColumn;
    @FXML private TableColumn<Match,String> upcomDateColumn;
    @FXML private TableColumn<Match,String> upcomTimeColumn;
    @FXML private TableColumn<Match,String> upcomInfoColumn;

    //Tab Previous Matches
    @FXML private Tab previousMatchesTab;
    @FXML private TableView<Match> previousMatchesTable;
    @FXML private TableColumn<Match,String> prevTeamsColumn;
    @FXML private TableColumn<Match,String> prevResultColumn;
    @FXML private TableColumn<Match,String> prevDateColumn;
    @FXML private TableColumn<Match,String> prevTimeColumn;
    @FXML private TableColumn<Match,String> prevInfoColumn;

    //Tab GroupStage
    @FXML private Tab groupStageTab;

    //Tab knockoutStage
    @FXML private Tab knockoutStageTab;
    @FXML private HBox outerHbox;
    @FXML private HBox groupStageHBox;
    @FXML private Text tournamentNameOutput;




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("displayMode initialize start");
        System.out.println("    tournament ptr = " + tournament);

        //Preparing match table
        upcomTeamsColumn.setCellValueFactory(new PropertyValueFactory<>("participantsAsString"));
        upcomResultColumn.setCellValueFactory(new PropertyValueFactory<>("resultAsString"));
        upcomDateColumn.setCellValueFactory(new PropertyValueFactory<>("MatchDateAsString"));
        upcomTimeColumn.setCellValueFactory(new PropertyValueFactory<>("StartTimeAsString"));
        upcomInfoColumn.setCellValueFactory(new PropertyValueFactory<>("matchInfo"));
        upcomingMatchesTable.setEditable(false);

        //Preparing match table
        prevTeamsColumn.setCellValueFactory(new PropertyValueFactory<>("participantsAsString"));
        prevResultColumn.setCellValueFactory(new PropertyValueFactory<>("resultAsString"));
        prevDateColumn.setCellValueFactory(new PropertyValueFactory<>("MatchDateAsString"));
        prevTimeColumn.setCellValueFactory(new PropertyValueFactory<>("StartTimeAsString"));
        prevInfoColumn.setCellValueFactory(new PropertyValueFactory<>("matchInfo"));
        previousMatchesTable.setEditable(false);

    }

    /**
     * Starts session for administrating a tournament with TournamentDAO.
     * Makes file containing tournament accessible so that it can be easily updated frequently.
     * TODO give user reassuring feedback whenever tournament file is updated.
     * TODO better user feedback for errors.
     * @param tournamentDAO DAO for tournament object. Must be non-null.
     */
    public void initData(TournamentDAO tournamentDAO) {
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

        displayTabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> observable, Tab oldTab, Tab newTab) {
                if (oldTab == newTab) ;
                else if (newTab == knockoutStageTab) {
                    loadKnockOutStage();
                } else if (newTab == groupStageTab) {
                    loadGroupStageTab();
                } else if (newTab == previousMatchesTab) {
                    loadPreviousMatchesTab();
                } else {
                    loadUpcomingMatchesTab();
                }
            }
        });

        if(tournament.getGroupStage()!= null){
            if(tournament.getGroupStage().isFinished()){
                loadKnockOutStage();
            }
            else loadGroupStageTab();
        }
        else loadUpcomingMatchesTab();
    }

    /**
     * Loads upcoming matches tab
     */
    @FXML
    public void loadUpcomingMatchesTab(){
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
        upcomingMatchesTable.setItems(matchesObservable);
    }

    /**
     * Loads previous matches tab
     */
    @FXML
    public void loadPreviousMatchesTab(){
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
        previousMatchesTable.setItems(matchesObservable);
    }

    /**
     * Loads group stage tab
     */
    @FXML
    public void loadGroupStageTab()  {
       if (tournament.hasGroupStage()) {
           try {
               setBracketGroupContainers();
           } catch (IOException e) {
               e.printStackTrace();
           }
           int i = 0;
           for (Group group : tournament.getGroupStage().getGroups()) {
                   groupStageControllers.get(i%2).displayGroup(group);
                   i++;
           }
       }
    }




    /**
     * Loads knockout stage tab
     */
    @FXML
    public void loadKnockOutStage(){
        if (tournament.getKnockoutStage() != null){
            if(!tournament.getKnockoutStage().getRounds().isEmpty()){
                try {
                    setBracketRoundContainers();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * method for setting round bracket containers in the display mode for knockout stage based on
     * number of rounds in the knockout stage.
     * @throws IOException
     */
    public void setBracketRoundContainers() throws IOException {
       int numberOfRounds = this.tournament.getKnockoutStage().getRounds().size();
        if (numberOfRounds == 0) throw new IllegalArgumentException("       Number of rounds in knockout stage is zero");
        else if (numberOfRounds > 4) {
            for (int i = numberOfRounds - 4; i < numberOfRounds; i++) {
                addBracketRoundContainer(this.tournament.getKnockoutStage().getRounds().get(i));
            }
        } else {
            for (int i = 0; i < numberOfRounds; i++) {
                addBracketRoundContainer(this.tournament.getKnockoutStage().getRounds().get(i));
            }
        }
    }

    /**
     * method for adding a BracketRoundContainer to the display section.
     * @throws IOException
     */
    public void addBracketRoundContainer(Round round) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        VBox BracketRoundContainer = loader.load(Objects.requireNonNull(getClass().getResource("../view/BracketRoundContainer.fxml")).openStream());
        outerHbox.getChildren().add(BracketRoundContainer);
        BracketRoundContainerController controller = loader.getController();
        controller.setMatchesInRoundContainers(round);
        roundControllers.add(controller);
    }

    public void setBracketGroupContainers() throws IOException {
        if (this.tournament.hasGroupStage()) {
            int numberOfGroups = this.tournament.getGroupStage().getGroups().size();
            addBracketGroupContainer();
            if (numberOfGroups >= 2) {
                addBracketGroupContainer();
            }
        }


    }

    /**
     * method that adds a groupContainer to the Group stage tab
     * @throws IOException
     */
    public void addBracketGroupContainer() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        VBox BracketGroupContainer = loader.load(Objects.requireNonNull(getClass().getResource("../view/BracketGroupContainer.fxml")).openStream());
        groupStageHBox.getChildren().add(BracketGroupContainer);
        BracketGroupContainerController controller = loader.getController();
        groupStageControllers.add(controller);
    }




}