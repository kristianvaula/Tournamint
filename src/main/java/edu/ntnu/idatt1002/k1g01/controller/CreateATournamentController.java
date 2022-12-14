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

    //Somewhat arbitrary team cap. Application poorly tested beyond this limit.
    public static final int MAX_TEAM_COUNT = 256;

    //Constants used for testing
    private static final Boolean TEST_MODE = false; // true -> activate testMode.
    private static final int DUMMY_TEAM_COUNT = 12; //Number of dummy teams to populate teamList.

    //The nested controller for the menuBar and pop-up menu
    @FXML private TopMenuBarController topMenuBarController;
    @FXML private MenuController popUpMenuController;

    //The tournament
    private Tournament tournament;
    //The tournament DAO for that will connect it to its corresponding file.
    private TournamentDAO tournamentDAO;
    //Teams the user adds
    private ArrayList<Team> teamList = new ArrayList<>();

    //Stores previous non-critical warning, so that each new warning will only interrupt Tournament creation once.
    private String warningFlag = "";

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
     * Adds dummy Teams and dummy Tournament name for rapid testing.
     * @author Martin Dammerud
     */
    private void addDummyData(){
        tournamentNameInputField.setText("DummyTournament");
        String[] dummyNames = {"pingas", "luigi", "princess", "maiboi", "stinker", "frog", "guttaBoys", "sennep inc", "din mor",
                "covfefe", "mousie", "faceDesks", "snibs", "mumu", "bobo", "powercromp"};

        //Cycle through list of dummyNames as many times as needed.
        //Append number from second loop and onward to keep all names unique.
        //Add Dummy teams to teamList.
        for (int i = 0; i < DUMMY_TEAM_COUNT; i++) {
            String number = String.valueOf(i/dummyNames.length + 1);
            if (number.equals("1")) number = "";
            this.teamList.add(new Team(dummyNames[i % dummyNames.length] + number));
            updateTeamTable();
        }
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

        //Adds dummy data if testMode is set to true.
        if(TEST_MODE) addDummyData();
    }

    /**
     * Asks the user for a file path and file name to save the new tournament.
     * Creates TournamentDAO with user provided path.
     *
     * @param event the event
     * @return TournamentDAO which links tournament object with persistent file.
     * @throws IOException If saving is not completed.
     * @author Martin Dammerud
     */
    @FXML
    private TournamentDAO saveTournamentToFile (ActionEvent event) throws IOException {
        try { return FileController.saveToFile(tournament, (Stage) ((Node)event.getSource()).getScene().getWindow()); }
        catch (Exception e) { System.out.println(e.getMessage()); return null; }
    }

    /**
     * Changes the scene to administrate tournament
     * Also uses the administrate tournament controller to
     * send the tournament instance.
     *
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
        //First clear error/warning messages
        tournamentErrorOutput.setText("");
        tournamentErrorOutput.setStyle("-fx-text-fill: red");

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
                    this.warningFlag = ""; //Reset warningFlag.
                    tournamentErrorOutput.setText(e.getMessage());
                    return null;
                }
            }
            else{
                try{
                    tournament = new Tournament(tournamentName,teamList,matchType,teamsPerMatch);
                }catch (IllegalArgumentException e){
                    this.warningFlag = ""; //Reset warningFlag.
                    tournamentErrorOutput.setText(e.getMessage());
                    return null;
                }
            }
        }

        //Warn the user if some groups in groupStage are not full.
        if (tournament!= null && tournament.partialGroupCount() != 0) {
            int countMinSize = tournament.partialGroupCount();
            int minGroupSize = tournament.getGroupStage().minGroupTeamCount();
            int groupCount = tournament.getGroupStage().getGroups().size();

            //Assemble useful warning message.
            String warning = "Warning: Cannot fill all groups.\n";
            if (countMinSize == 1) {
                warning += "1 of "+ groupCount + " groups has only " + minGroupSize + " teams. ";
            }
            else {
                if (countMinSize == groupCount) {
                    warning += "No groups with " + teamsPerGroup + " teams!\n";
                }
                countMinSize = (int)tournament.getGroupStage().getGroups().stream()
                        .filter(g -> g.size() == minGroupSize).count();
                warning += countMinSize + " of " + groupCount + " groups have only " + minGroupSize + " teams. ";
            }
            warning += "\nCreate anyway?";

            //Use the Error output field to display assembled warning, and interrupt creation once.
            tournamentErrorOutput.setStyle("-fx-text-fill: orange");
            tournamentErrorOutput.setText(warning);

            //Use warningFlag to check if user chose to ignore previous warning.
            if (! this.warningFlag.equals(warning)) tournament = null; System.out.println(warningFlag);
            this.warningFlag = warning;
        }
        else this.warningFlag = ""; //Reset warningFlag.
        return tournament;
    }

    /**
     * Gets called when user presses Create Tournament
     * If successful, proceeds to save and administrate tournament.
     *
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
     *
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
        else if(teamList.size()>MAX_TEAM_COUNT){
            addTeamErrorOutput.setText("Too many teams! Maximum team number is " + MAX_TEAM_COUNT + " teams.");
        }else{
            teamList.add(new Team(teamName));
            teamNameInputField.setText("");
            updateTeamTable();
        }
    }

    /**
     * Allows user to edit teams in list
     *
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
        teamsObservable.addAll(teamList);
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
     *
     * @param event the actionEvent.
     */
    public void deleteSelectedTeam(ActionEvent event){
        ObservableList<Team> selectedRows, tableRows;
        //Get all rows
        tableRows = teamTableOutput.getItems();
        //Get all selected rows
        selectedRows = teamTableOutput.getSelectionModel().getSelectedItems();

        //Loop through selected and remove from lists.
        for(Team team : selectedRows){
            teamList.remove(team); //TODO A soft error happens when the last team is deleted from the list.
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
            teamsPerGroupInput.setDisable(true);
            teamsPerGroupLabel.setStyle("-fx-opacity:0.4");
            teamsAdvancingFromGroupInput.setDisable(true);
            teamsAdvancingFromGroupLabel.setStyle("-fx-opacity:0.4");
            teamsPerMatchInput.setDisable(false);
            teamsPerMatchLabel.setStyle("-fx-opacity:1");
        }
        else if (radioButton.equals(groupStageButton)) {
            teamsPerGroupInput.setDisable(false);
            teamsPerGroupLabel.setStyle("-fx-opacity:1");
            teamsAdvancingFromGroupInput.setDisable(false);
            teamsAdvancingFromGroupLabel.setStyle("-fx-opacity:1");
            teamsPerMatchInput.setValue(2);
            teamsPerMatchInput.setDisable(true);
            teamsPerMatchLabel.setStyle("-fx-opacity:0.4");
        }
    }
    /**
     * Displays the pop-up menu
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
