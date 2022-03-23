package edu.ntnu.idatt1002.k1g01.controller;

import edu.ntnu.idatt1002.k1g01.model.Team;
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
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Controls the create a tournament page
 */
public class CreateATournamentController implements Initializable {

    //Teams the user adds
    private ArrayList<Team> teamList = new ArrayList<>();

    //Tournament Settings
    @FXML private TextField nameInputField;
    @FXML private ChoiceBox<String> tournamentTypeInput;
    @FXML private ChoiceBox<Integer> teamsPerGroupInput;
    @FXML private ChoiceBox<Integer> teamsAdvancingFromGroupInput;
    @FXML private RadioButton pointMatchButton;
    @FXML private RadioButton timeMatchButton;
    @FXML private Label tournamentErrorOutput;
    private ToggleGroup radioToggleGroup;

    //Team settings
    @FXML private TextField teamNameInputField;
    @FXML private TableView<Team> teamTableOutput;
    @FXML private TableColumn<Team,String> teamNameColumn;
    @FXML private Label addTeamErrorOutput;


    /**
     * Changes the scene to CreateATournamentWindow
     */
    @FXML
    public void cancelBackToHomePage(ActionEvent event)throws IOException {
        try {
            Parent createTournament = FXMLLoader.load(getClass().getResource("../view/HomePage.fxml"));
            Scene createTournamentScene = new Scene(createTournament);

            //This line gets the Stage information
            Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
            window.setScene(createTournamentScene);
            window.show();

        } catch (IOException e) {
            System.out.println(e.getCause());
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
        //Adding tournamentTypes values to choicebox
        tournamentTypeInput.getItems().add("Knockout Stage");
        tournamentTypeInput.getItems().add("Group Stage + Knockout Stage");
        tournamentTypeInput.setValue("Knockout Stage");

        //Adding teamsPerGroup values to choicebox
        teamsPerGroupInput.getItems().add(2);
        teamsPerGroupInput.getItems().add(4);
        teamsPerGroupInput.getItems().add(6);
        teamsPerGroupInput.getItems().add(8);
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
        teamNameColumn.setCellValueFactory(new PropertyValueFactory<Team,String>("name"));
        //Make Table rows editable
        teamTableOutput.setEditable(true);
        teamNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        //If we want the user to select multiple rows at once
        teamTableOutput.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    }

    /**
     * Gets called when user presses Create Tournament
     * @param event the actionEvent
     */
    public void createTournament(ActionEvent event) {
        String tournamentName =  nameInputField.getText();
        String tournamentType = tournamentTypeInput.getValue();
        int teamsPerGroup = teamsPerGroupInput.getValue();
        int teamsAdvancingFromGroup = teamsAdvancingFromGroupInput.getValue();
        String matchType;
        if(radioToggleGroup.getSelectedToggle().equals(timeMatchButton)){
            matchType = "time";
        }else matchType = "point";

        if(tournamentName.isBlank() || tournamentName.isEmpty()){
            tournamentErrorOutput.setText("Please enter a tournament name");
        }else if(teamList.size()<2){
            tournamentErrorOutput.setText("You need at least two teams to create a tournament");
        }else {

        }


    }

    /**
     * Gets called when user presses (+)
     * @param event the actionEvent.
     */
    public void addTeam(ActionEvent event) {
        String teamName = teamNameInputField.getText();

        if(teamName.isEmpty() || teamName.isBlank()){
            addTeamErrorOutput.setText("Please enter a team name");
        }
        else if(teamList.contains(new Team(teamName))){
            addTeamErrorOutput.setText("Teams cannot have the same name");
        }
        else if(teamList.size()>256){
            teamList.add(new Team(teamName));
            updateTeamTable(event);
        }else{
            addTeamErrorOutput.setText("Tournament cannot have more than 256 teams");
        }
    }

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
        return teamsObservable;
    }

    /**
     * Updates team table by setting all teams
     * @param event the actionEvent
     */
    public void updateTeamTable(ActionEvent event){
        teamTableOutput.setItems(getTeams());
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
}
