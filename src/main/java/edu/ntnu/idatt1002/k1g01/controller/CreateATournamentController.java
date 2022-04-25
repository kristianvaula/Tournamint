package edu.ntnu.idatt1002.k1g01.controller;

import edu.ntnu.idatt1002.k1g01.dao.TournamentDAO;
import edu.ntnu.idatt1002.k1g01.model.Team;
import edu.ntnu.idatt1002.k1g01.model.Tournament;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Controls the create a tournament page
 *
 * @author kristvje
 * @author martdam
 */
public class CreateATournamentController implements Initializable {

    //The nested controller for the menuBar and pop-up menu
    @FXML private TopMenuBarController topMenuBarController;
    @FXML private MenuController popUpMenuController;

    //The tournament
    private Tournament tournament;
    //The tournament DAO for that will connect it to its corresponding file.
    private TournamentDAO tournamentDAO;
    //Teams the user adds
    private ArrayList<Team> teamList = new ArrayList<>();

    //Tournament Settings
    @FXML private TextField tournamentNameInputField;
    @FXML private RadioButton knockoutStageButton;
    @FXML private RadioButton groupStageButton;
    private ToggleGroup stageToggleGroup;
    @FXML private ChoiceBox<Integer> teamsPerGroupInput;
    @FXML private Label teamsPerGroupLabel;
    @FXML private ChoiceBox<Integer> teamsAdvancingFromGroupInput;
    @FXML private Label teamsAdvancingFromGroupLabel;
    @FXML private ChoiceBox<Integer> teamsPerMatchInput;
    @FXML private Label teamsPerMatchLabel;
    @FXML private RadioButton pointMatchButton;
    @FXML private RadioButton timeMatchButton;
    private ToggleGroup matchToggleGroup;
    @FXML private Label tournamentErrorOutput;
    @FXML private Label tournamentWarningOutput;

    //Team settings
    @FXML private Button addTeamButton;
    @FXML private ImageView addTeamButtonIcon;
    @FXML private TextField teamNameInputField;
    @FXML private TableView<Team> teamTableOutput;
    @FXML private TableColumn<Team,String> teamNameColumn;
    @FXML private Label addTeamErrorOutput;
    @FXML private Label teamCounterOutput;

    //Pop-up menu
    @FXML private StackPane menuStackPane;
    @FXML private Node menuVBox;

    /**
     * Changes the scene to CreateATournamentWindow
     */
    public void cancelBackToHomePage(ActionEvent event)throws IOException {
        try {
            Parent createTournament = FXMLLoader.load(getClass().getResource("../view/HomePage.fxml"));
            Scene createTournamentScene = new Scene(createTournament);

            //This line gets the Stage information
            Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
            window.setScene(createTournamentScene);
            window.show();

        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    /**
     * Generated dummy Teams for testing
     * @param n number of teams to generate
     * @return ArrayList of teams.
     */
    private ArrayList<Team> generateTeams(int n){
        ArrayList<Team> teams = new ArrayList<>();
        String[] names = {"pingas", "luigi", "princess", "maiboi", "stinker", "frog", "guttaBoys", "sennep inc", "din mor",
                "covfefe", "mousie", "faceDesks", "snibs", "mumu", "bobo", "powercromp"};
        for (int i = 0; i < n; i++) {
            String number = String.valueOf(i/names.length + 1);
            if (number.equals("1")) number = "";
            teams.add(new Team(names[i % names.length] + number));
        }
        return teams;
    }

    /**
     * Initializes CreateATournament
     * @param url url
     * @param resourceBundle resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Pop-up menu
        menuStackPane.setDisable(true);
        menuStackPane.setVisible(false);

        //Adding tournamentTypes values to choicebox
        stageToggleGroup = new ToggleGroup();
        knockoutStageButton.setToggleGroup(stageToggleGroup);
        groupStageButton.setToggleGroup(stageToggleGroup);
        stageToggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observableValue, Toggle oldVal, Toggle newVal) {
                tournamentTypeChangeEvent((RadioButton) newVal);
            }
        });
        stageToggleGroup.selectToggle(knockoutStageButton);


        //Adding teamsPerGroup values to choicebox
        for (int n = 2; n < 9; n++)
        teamsPerGroupInput.getItems().add(n);
        teamsPerGroupInput.setValue(4);

        //Adding teamsAdvancingFromGroup values to choicebox
        teamsAdvancingFromGroupInput.getItems().add(1);
        teamsAdvancingFromGroupInput.getItems().add(2);
        teamsAdvancingFromGroupInput.getItems().add(3);
        teamsAdvancingFromGroupInput.getItems().add(4);
        teamsAdvancingFromGroupInput.setValue(2);

        //Adding matchType radio button values
        matchToggleGroup = new ToggleGroup();
        pointMatchButton.setToggleGroup(matchToggleGroup);
        timeMatchButton.setToggleGroup(matchToggleGroup);
        matchToggleGroup.selectToggle(pointMatchButton);
        //Adding listeners to change options based on type.
        matchToggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> changed, Toggle oldVal, Toggle newVal) {
                if((RadioButton)newVal == pointMatchButton){
                    groupStageButton.setDisable(false);
                    groupStageButton.setStyle("-fx-opacity: 1");
                }
                else if((RadioButton)newVal == timeMatchButton){
                    stageToggleGroup.selectToggle(knockoutStageButton);
                    groupStageButton.setDisable(true);
                    groupStageButton.setStyle("-fx-opacity:0.4");
                }
            }
        });

        //Teams per match values
        teamsPerMatchInput.getItems().add(2);
        teamsPerMatchInput.getItems().add(4);
        teamsPerMatchInput.getItems().add(6);
        teamsPerMatchInput.getItems().add(8);
        teamsPerMatchInput.getItems().add(12);
        teamsPerMatchInput.setValue(2);

        //Setting up table
        teamNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        //Make Table rows editable
        teamTableOutput.setEditable(true);
        teamNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        //If we want the user to select multiple rows at once
        teamTableOutput.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        //For testing
        tournamentNameInputField.setText("Turnering");
        teamList = generateTeams(24);
        updateTeamTable();
    }

    /**
     * Asks the user for a file path and file name to save the new tournament.
     * Creates TournamentDAO with user provided path.
     * @param event the event
     * @return TournamentDAO which links tournament object with persistent file.
     * @throws IOException If saving is not completed.
     * @author Martin Dammerud
     */
    @FXML
    private TournamentDAO saveTournamentToFile(ActionEvent event) {
        try { return FileController.saveToFile(tournament, (Stage) ((Node)event.getSource()).getScene().getWindow()); }
        catch (Exception e) { System.out.println(e.getMessage()); return null; }
    }

    /**
     * Changes the scene to administrate tournament
     * Also uses the administrate tournament controller to
     * send the tournament instance.
     * @param event the event
     * @throws IOException if fxml file bad
     */
    @FXML
    public void changeSceneToAdministrateTournament(ActionEvent event)throws IOException{
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../view/AdministrateTournament.fxml"));
            Parent administrateParent = loader.load();

            Scene administrateScene = new Scene(administrateParent);
            administrateScene.setUserData(loader);
            //Access the controller and call a method
            AdministrateTournamentController controller = loader.getController();
            TournamentDAO tournamentDAO = saveTournamentToFile(event); // Get DAO with user provided path.
            if (tournamentDAO == null) { return; } // File save likely canceled.
            controller.initData(tournamentDAO); //Call controller with DAO rather than raw tournament object.

            Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
            window.setScene(administrateScene);
            window.show();

        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    /**
     * Generates a tournament from GUI parameters and displays any errors or warning to the user.
     * Does not automatically save the tournament or change scene.
     * Handy for giving user constant feedback for input.
     * @return User specified tournament.
     */
    public Tournament generateTournament(){
        String tournamentName =  tournamentNameInputField.getText();
        int teamsPerGroup = teamsPerGroupInput.getValue();
        int teamsPerMatch = teamsPerMatchInput.getValue();
        int teamsAdvancingFromGroup = teamsAdvancingFromGroupInput.getValue();

        String matchType;
        if(matchToggleGroup.getSelectedToggle().equals(timeMatchButton)){
            matchType = "timeMatch";
        }else matchType = "pointMatch";

        if(tournamentName.isBlank() || tournamentName.isEmpty()){
            tournamentErrorOutput.setText("Please enter a tournament name");
        }
        else if(teamList.size()<2 ||teamList.size() < teamsPerMatch ){
            tournamentErrorOutput.setText("Not enough teams");
        }
        else { // Create tournament
            if(stageToggleGroup.getSelectedToggle().equals(groupStageButton)){
                try{
                    tournament = new Tournament(tournamentName,teamList,matchType,teamsPerMatch,teamsPerGroup,teamsAdvancingFromGroup);
                }catch (IllegalArgumentException e){
                    tournamentErrorOutput.setText(e.getMessage());
                    return null;
                }
            }
            else{
                try{
                    tournament = new Tournament(tournamentName,teamList,matchType,teamsPerMatch);
                }catch (IllegalArgumentException e){
                    tournamentErrorOutput.setText(e.getMessage());
                    return null;
                }
            }
        }
        int countMinSize = tournament.partialGroupCount();
        if (tournament!= null && countMinSize != 0) {
            int minGroupSize = tournament.getGroupStage().minGroupTeamCount();
            tournamentWarningOutput.setText("Warning: " + countMinSize + " groups have only " + minGroupSize + " teams.");
        }
        return tournament;
    }

    /**
     * Gets called when user presses Create Tournament
     * If successful, proceeds to save and administrate tournament.
     * @param event the actionEvent
     */
    @FXML
    public void createTournament(ActionEvent event) throws IOException{
        try{
            tournament = generateTournament();
            changeSceneToAdministrateTournament(event);
        }catch (IllegalArgumentException e){
            tournamentErrorOutput.setText(e.getMessage());
        }
    }

    /**
     * Gets called when user presses (+)
     * @param event the actionEvent.
     */
    @FXML
    public void addTeam(ActionEvent event) {
        String teamName = teamNameInputField.getText().trim();
        tournamentErrorOutput.setText("");

        if(teamName.isEmpty() || teamName.isBlank()){
            addTeamErrorOutput.setText("Please enter a team name");
        }
        else if(teamList.stream().anyMatch(team -> team.getName().equals(teamName))){
            addTeamErrorOutput.setText("Teams cannot have the same name");
        }
        else if(teamList.size()>256){ //TODO consider removing this.
            addTeamErrorOutput.setText("Too many teams");
        }else{
            teamList.add(new Team(teamName));
            teamNameInputField.setText("");
            updateTeamTable();
        }
    }

    /**
     * Allows user to edit teams in list
     * @param editedCell the event
     */
    @FXML
    public void changeTeamNameCellEvent(TableColumn.CellEditEvent editedCell){
        Team team = teamTableOutput.getSelectionModel().getSelectedItem();
        team.setName(editedCell.getNewValue().toString());
    }

    /**
     * Gets all teams as ObservableList
     * @return the ObservableList
     */
    public ObservableList<Team> getTeams(){
        ObservableList<Team> teamsObservable = FXCollections.observableArrayList();
        for(Team team : teamList){
            teamsObservable.add(team);
        }
        if(teamsObservable.size() == 0) return null;
        return teamsObservable;
    }

    /**
     * Checks if input name is too long and warns user if it is.
     */
    public void checkNameLength() {
        if (teamNameInputField.getText().length() > Team.maxNameLength) {
            addTeamErrorOutput.setText("Name is too long");
            addTeamButton.setDisable(true);
            addTeamButtonIcon.setVisible(false);
        }
        else {
            addTeamErrorOutput.setText("");
            addTeamButton.setDisable(false);
            addTeamButtonIcon.setVisible(true);
        }

    }

    /**
     * Updates team table by setting all teams
     */
    @FXML
    public void updateTeamTable(){
        teamCounterOutput.setText("teams: " + getTeams().size());
        if(getTeams() != null) teamTableOutput.setItems(getTeams());
    }

    /**
     * Gets called when user presses delete button
     * Removes selected teams.
     * @param event the actionEvent.
     */
    public void deleteSelectedTeam(ActionEvent event){
        ObservableList<Team> selectedRows, tableRows;
        //Get all rows
        tableRows = teamTableOutput.getItems();
        //Get all selected rows
        selectedRows = teamTableOutput.getSelectionModel().getSelectedItems();

        //Loop through selected and remove from lists.
        for(Team team : selectedRows){ //TODO This causes a soft error if the last team is deleted from the list
            teamList.remove(team);
            tableRows.remove(team);
        }
        teamCounterOutput.setText("teams: " + teamList.size());
    }

    /**
     * Activates or greys out input fields depending on selected tournamentType.
     * @author Martin Dammerud
     */
    @FXML
    public void tournamentTypeChangeEvent(RadioButton radioButton) {
        System.out.println(radioButton.getText());
        if (radioButton.equals(knockoutStageButton)) {
            //teamsPerGroupInput.setVisible(false);
            teamsPerGroupInput.setDisable(true);
            //teamsPerGroupLabel.setVisible(false);
            teamsPerGroupLabel.setStyle("-fx-opacity:0.4");
            //teamsAdvancingFromGroupInput.setVisible(false);
            teamsAdvancingFromGroupInput.setDisable(true);
            //teamsAdvancingFromGroupLabel.setVisible(false);
            teamsAdvancingFromGroupLabel.setStyle("-fx-opacity:0.4");
            //teamsPerMatchInput.setVisible(true);
            teamsPerMatchInput.setDisable(false);
            //teamsPerMatchLabel.setVisible(true);
            teamsPerMatchLabel.setStyle("-fx-opacity:1");
        }
        else if (radioButton.equals(groupStageButton)) {
            //teamsPerGroupInput.setVisible(true);
            teamsPerGroupInput.setDisable(false);
            //teamsPerGroupLabel.setVisible(true);
            teamsPerGroupLabel.setStyle("-fx-opacity:1");
            //teamsAdvancingFromGroupInput.setVisible(true);
            teamsAdvancingFromGroupInput.setDisable(false);
            //teamsAdvancingFromGroupLabel.setVisible(true);
            teamsAdvancingFromGroupLabel.setStyle("-fx-opacity:1");
            //teamsPerMatchInput.setVisible(false);
            teamsPerMatchInput.setDisable(true);
            //teamsPerMatchLabel.setVisible(false);
            teamsPerMatchLabel.setStyle("-fx-opacity:0.4");
        }
    }
    /**
     * Displays the pop up menu
     */
    @FXML
    public void displayPopUpMenu(){
        menuStackPane.setDisable(false);
        menuStackPane.setVisible(true);
    }

    /**
     * Disables irrelevant buttons
     */
    @FXML
    public void disableMenuButtons(){
        popUpMenuController.disableDisplayAndAdministrate();
    }
}
