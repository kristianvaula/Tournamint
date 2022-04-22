package edu.ntnu.idatt1002.k1g01.controller;

import edu.ntnu.idatt1002.k1g01.dao.TournamentDAO;
import edu.ntnu.idatt1002.k1g01.model.Team;
import edu.ntnu.idatt1002.k1g01.model.Tournament;
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
    @FXML private Button addTeamButton;
    @FXML private ImageView addTeamButtonIcon;
    @FXML private ChoiceBox<String> tournamentTypeInput;
    @FXML private ChoiceBox<Integer> teamsPerGroupInput;
    @FXML private ChoiceBox<Integer> teamsAdvancingFromGroupInput;
    @FXML private RadioButton pointMatchButton;
    @FXML private RadioButton timeMatchButton;
    @FXML private Label tournamentErrorOutput;
    @FXML private Label tournamentWarningOutput;
    private ToggleGroup radioToggleGroup;

    //Team settings
    @FXML private TextField teamNameInputField;
    @FXML private TableView<Team> teamTableOutput;
    @FXML private TableColumn<Team,String> teamNameColumn;
    @FXML private Label addTeamErrorOutput;

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
        tournamentTypeInput.getItems().add("Knockout Stage");
        tournamentTypeInput.getItems().add("Group Stage + Knockout Stage");
        tournamentTypeInput.setValue("Knockout Stage");

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
        radioToggleGroup = new ToggleGroup();
        pointMatchButton.setToggleGroup(radioToggleGroup);
        timeMatchButton.setToggleGroup(radioToggleGroup);
        radioToggleGroup.selectToggle(pointMatchButton);

        //Setting up table
        teamNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        //Make Table rows editable
        teamTableOutput.setEditable(true);
        teamNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        //If we want the user to select multiple rows at once
        teamTableOutput.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        //For testing
        tournamentNameInputField.setText("Turnering");
        teamList.add(new Team("Gutta Krutt"));
        teamList.add(new Team("Pølse Magne"));
        teamList.add(new Team("Kameratene"));
        teamList.add(new Team("Sennep Inc"));
        teamList.add(new Team("Bilbo's Boys"));
        teamList.add(new Team("Samir & friend"));
        teamList.add(new Team("An Ananas"));
        teamList.add(new Team("Pingas"));
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
        String tournamentType = tournamentTypeInput.getValue();
        int teamsPerGroup = teamsPerGroupInput.getValue();
        int teamsPerMatch = 2;
        int teamsAdvancingFromGroup = teamsAdvancingFromGroupInput.getValue();
        String matchType;
        if(radioToggleGroup.getSelectedToggle().equals(timeMatchButton)){
            matchType = "timeMatch";
        }else matchType = "pointMatch";

        if(tournamentName.isBlank() || tournamentName.isEmpty()){
            tournamentErrorOutput.setText("Please enter a tournament name");
        }
        else if(teamList.size()<2 ||teamList.size() < teamsPerMatch ){
            tournamentErrorOutput.setText("Not enough teams");
        }
        else { // Create tournament
            if(tournamentType.equals("Group Stage + Knockout Stage")){
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
        String teamName = teamNameInputField.getText();
        tournamentErrorOutput.setText("");

        if(teamName.isEmpty() || teamName.isBlank()){
            addTeamErrorOutput.setText("Please enter a team name");
        }
        else if(teamList.contains(new Team(teamName))){
            addTeamErrorOutput.setText("Teams cannot have the same name");
        }
        else if(teamList.size()>256){
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
        for(Team team : selectedRows){
            teamList.remove(team);
            tableRows.remove(team);
        }
    }

    /**
     * Activates or greys out input fields depending on selected tournamentType.
     * @author Martin Dammerud
     */
    @FXML
    public void tournamentTypeInputEvent() {
        System.out.println(tournamentTypeInput.getValue());
        switch (tournamentTypeInput.getValue()){
            case ("Knockout Stage"):
                teamsPerGroupInput.setDisable(true);
                teamsAdvancingFromGroupInput.setDisable(true);
                break;
            case ("Group Stage + Knockout Stage"):
                teamsPerGroupInput.setDisable(false);
                teamsAdvancingFromGroupInput.setDisable(false);
                break;
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
