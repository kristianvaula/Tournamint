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
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Controls the edit tournament page
 *
 * @author kristvje
 */
public class EditTournamentController implements Initializable {

    //The nested controller for the menuBar and pop-up menu
    @FXML private TopMenuBarController topMenuBarController;

    //The tournament
    private Tournament tournament;
    //The tournament DAO for that will connect it to its corresponding file.
    private TournamentDAO tournamentDAO;
    //Teams the user adds
    private ArrayList<Team> teamList = new ArrayList<>();

    //Tournament Settings
    @FXML private TextField tournamentNameField;
    @FXML private Label tournamentErrorOutput;

    //Team settings
    @FXML private TextField teamNameInputField;
    @FXML private TableView<Team> teamTableOutput;
    @FXML private TableColumn<Team,String> teamNameColumn;
    @FXML private Label editTeamErrorOutput;
    private Team selectedTeam = null;

    /**
     * Initializes Edit Tournament
     *
     * @param url url
     * @param resourceBundle resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Setting up table
        teamNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        //Make Table rows editable
        teamTableOutput.setEditable(true);
        //teamNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        //If we want the user to select single row at once
        teamTableOutput.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        //Adding name to input field if we click on it
        teamTableOutput.setRowFactory(TableView -> {
            TableRow<Team> row = new TableRow<>();

            row.setOnMouseClicked(event -> {
                //If we select team we display it in the box and "select" it
                if(!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1){
                    selectedTeam = row.getItem();
                    teamNameInputField.setText(selectedTeam.getName());
                }
            });
            return row;
        });
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
        tournamentNameField.setText(tournament.getTournamentName());
        //Need to do a hard copy of teams so that we can make
        //changes without affecting the real team objects
        for(Team team : tournament.getTeams()){
            teamList.add(new Team(team.getName()));
        }
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
     * Changes the scene to administrate tournament page
     */
    public void cancelBackToAdministrate(ActionEvent event)throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../view/AdministrateTournament.fxml"));
            Parent administrateParent = loader.load();

            Scene administrateScene = new Scene(administrateParent);
            administrateScene.setUserData(loader);
            //Access the controller and call a method
            AdministrateTournamentController controller = loader.getController();
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
            tournamentDAO.save(tournament); // Get DAO with user provided path.
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
     * Gets called when user presses [Save]
     * Saves the name of team if it is changed.
     *
     * @param event the actionEvent.
     */
    @FXML
    public void editTeamName(ActionEvent event) {
        String teamNameInput = teamNameInputField.getText().trim();
        teamNameInputField.setText("");
        tournamentErrorOutput.setText("");

        if(selectedTeam != null ){
            if(!teamNameInput.equals(selectedTeam.getName())){

                if(teamNameInput.isEmpty() || teamNameInput.isBlank()){
                    editTeamErrorOutput.setText("Please enter a team name");
                }
                else if(teamNameInput.length() > 15){
                    editTeamErrorOutput.setText("Team name too long");
                }
                else{
                    selectedTeam.setName(teamNameInput);
                    System.out.println("");
                }
            }
        }
        else{
            editTeamErrorOutput.setText("Please select team first");
        }
        updateTeamTable();
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
     * Saves changes if there are any and
     * returns to administrate tournament
     *
     * @param event the button click
     */
    @FXML
    public void saveChanges(ActionEvent event) throws Exception{
        String tournamentNameInput = tournamentNameField.getText().trim();
        if(tournamentNameInput.isBlank() || tournamentNameInput.isEmpty()){
            tournamentErrorOutput.setText("Please enter a tournament name");
        }
        else{
            if(teamList.size() != tournament.getTeams().size()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("An error occoured, please try again");
                alert.show();
                alert.setOnCloseRequest(event1 -> {
                    try{
                        cancelBackToAdministrate(event);
                    }
                    catch(IOException e){
                        e.printStackTrace();
                    }
                });
            }
            else{
                if(!tournamentNameInput.equals(tournament.getTournamentName())){
                    tournament.setTournamentName(tournamentNameInput);
                }
                ArrayList<Team> tournamentTeams = tournament.getTeams();
                for (int i = 0; i < teamList.size(); i++) {
                    tournamentTeams.get(i).setName(teamList.get(i).getName());
                }
                try{
                    changeSceneToAdministrateTournament(event);
                }
                catch (IOException e){
                    e.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("An error occurred while loading administrate page, please try again");
                    alert.show();
                }
            }
        }
    }

    /**
     * Updates team table by setting all teams
     */
    @FXML
    public void updateTeamTable(){
        teamTableOutput.getItems().clear();
        ObservableList<Team> teamsObservable = FXCollections.observableArrayList();
        teamsObservable.addAll(getTeams());
        if(teamsObservable.size() > 0) {
            System.out.println("SETTING ITEMS ");
            teamTableOutput.setItems(teamsObservable);
        }
    }
}