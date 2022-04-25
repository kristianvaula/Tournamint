package edu.ntnu.idatt1002.k1g01.controller;

import edu.ntnu.idatt1002.k1g01.dao.TournamentDAO;
import edu.ntnu.idatt1002.k1g01.model.Group;
import edu.ntnu.idatt1002.k1g01.model.Round;
import edu.ntnu.idatt1002.k1g01.model.Tournament;
import edu.ntnu.idatt1002.k1g01.model.matches.Match;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

public class DisplayModeController implements Initializable {

    //The tournament variables
    private Tournament tournament;
    private TournamentDAO tournamentDAO;

    private ArrayList<BracketRoundContainerController> roundControllers = new ArrayList<>();
    private ArrayList<BracketGroupContainerController> groupStageControllers = new ArrayList<>();

    private final static String DLM = File.separator;
    private final static int AUTO_DISPLAY_TIMER = 5;

    //The nested controller for the menuBar and pop up menu
    @FXML private TopMenuBarController topMenuBarController;
    @FXML private MenuController popUpMenuController;

    //Clock and auto display button
    @FXML private TextField clock;
    @FXML private volatile boolean stopClock = false;
    @FXML private volatile boolean autoDisplay = false;
    @FXML private Button autoButton;


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
    @FXML private HBox groupStageHBox;

    //Tab knockoutStage
    @FXML private Tab knockoutStageTab;
    @FXML private HBox outerHbox;
    @FXML private Label tournamentNameOutput;

    //Pop-up menu
    @FXML private StackPane menuStackPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("displayMode initialize start");
        System.out.println("    tournament ptr = " + tournament);

        //Pop-up menu
        menuStackPane.setDisable(true);
        menuStackPane.setVisible(false);

        //Preparing match table
        upcomTeamsColumn.setCellValueFactory(new PropertyValueFactory<>("participantsAsString"));
        upcomResultColumn.setCellValueFactory(new PropertyValueFactory<>("resultAsString"));
        upcomDateColumn.setCellValueFactory(new PropertyValueFactory<>("MatchDateAsString"));
        upcomTimeColumn.setCellValueFactory(new PropertyValueFactory<>("StartTimeAsString"));
        upcomInfoColumn.setCellValueFactory(new PropertyValueFactory<>("matchInfo"));
        upcomingMatchesTable.setEditable(false);
        upcomingMatchesTable.setSelectionModel(null);

        //Preparing match table
        prevTeamsColumn.setCellValueFactory(new PropertyValueFactory<>("participantsAsString"));
        prevResultColumn.setCellValueFactory(new PropertyValueFactory<>("resultAsString"));
        prevDateColumn.setCellValueFactory(new PropertyValueFactory<>("MatchDateAsString"));
        prevTimeColumn.setCellValueFactory(new PropertyValueFactory<>("StartTimeAsString"));
        prevInfoColumn.setCellValueFactory(new PropertyValueFactory<>("matchInfo"));
        previousMatchesTable.setEditable(false);
        previousMatchesTable.setSelectionModel(null);

        Timenow();
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
        popUpMenuController.setTournamentDAO(tournamentDAO);
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

        if(!tournament.hasGroupStage()){
            groupStageTab.setDisable(true);
        }
        changeToNextTab();
    }

    /**
     * Changes the tab to the next display tab in the order
     */
    @FXML
    public void changeToNextTab(){
        SingleSelectionModel<Tab> selectionModel = displayTabPane.getSelectionModel();
        Tab selectedTab = selectionModel.getSelectedItem();

        if(selectedTab == upcomingMatchesTab){
            if(previousMatchesTab.disableProperty().get()){
                if(tournament.hasGroupStage()){
                    selectionModel.select(groupStageTab);
                }
                else{
                    selectionModel.select(knockoutStageTab);
                }
            }
            else{
                selectionModel.select(previousMatchesTab);
            }
        }
        else if(selectedTab == previousMatchesTab) {
            if(tournament.getTeamsPerMatch() != 2){
                selectionModel.select(upcomingMatchesTab);
            }
            else if(tournament.hasGroupStage()){
                selectionModel.select(groupStageTab);
            }
            else{
                selectionModel.select(knockoutStageTab);
            }
        }
        else if(selectedTab == groupStageTab){
            if(tournament.hasGroupStage() && tournament.getGroupStage().isFinished()){
                selectionModel.select(knockoutStageTab);
            }
            else{
                selectionModel.select(upcomingMatchesTab);
            }
        }
        else if(selectedTab == knockoutStageTab){
            selectionModel.select(upcomingMatchesTab);
        }
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
        if(matchesObservable.size()<1){
            previousMatchesTab.setDisable(true);
            changeToNextTab();
        }
        else {
            previousMatchesTable.setItems(matchesObservable);
        }
    }

    /**
     * Loads group stage tab
     */
    @FXML
    public void loadGroupStageTab()  {
        if(groupStageHBox.getChildren().isEmpty()){
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
    }

    /**
     * Loads knockout stage tab
     */
    @FXML
    public void loadKnockOutStage(){
        outerHbox.getChildren().clear();
        if(tournament.getTeamsPerMatch() == 2){
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

    /**
     * Switches on or off the auto display function
     */
    public void autoDisplaySwitch(){
        if(autoDisplay == false){
            this.autoDisplay = true;
            autoButton.setStyle("-fx-border-color: green");
            System.out.println("Autodisplay on");
        } else {
            this.autoDisplay = false;
            autoButton.setStyle("-fx-border-color: red");
            System.out.println("Autodisplay off");
        }
    }

    /**
     * Stops clock
     */
    @FXML
    public void stopClock() {
        this.stopClock = false;
        System.out.println("Stopped clock at " + new SimpleDateFormat("hh:mm:ss").format(new Date()));
    }

    /**
     * Running clock
     * Has to be stopped before changing scene.
     */
    @FXML
    public void Timenow(){
        Thread thread = new Thread(() -> {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            while(!stopClock){
                try{
                    Thread.sleep(1000);
                }catch(Exception e){
                    System.out.println(e);
                }
                Calendar calendar = Calendar.getInstance();
                final String timeNow = sdf.format(calendar.getTime());
                Platform.runLater(()->{
                    if(calendar.get(Calendar.SECOND) % AUTO_DISPLAY_TIMER == 0 && autoDisplay){
                        changeToNextTab();
                    }
                    clock.setText(timeNow);
                });
            }
        });
        thread.start();
    }

    /**
     * Displays the pop up menu
     */
    @FXML
    public void displayPopUpMenu(){
        System.out.println("Displayed Pop-Up Menu");
        menuStackPane.setDisable(false);
        menuStackPane.setVisible(true);
    }
}