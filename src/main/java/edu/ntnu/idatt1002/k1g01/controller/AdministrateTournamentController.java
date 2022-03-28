package edu.ntnu.idatt1002.k1g01.controller;

import edu.ntnu.idatt1002.k1g01.dao.TournamentDAO;
import edu.ntnu.idatt1002.k1g01.model.Round;
import edu.ntnu.idatt1002.k1g01.model.Tournament;
import edu.ntnu.idatt1002.k1g01.model.matches.Match;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.regex.MatchResult;

/**
 * Controls administration of tournament
 */
public class AdministrateTournamentController implements Initializable {

    //The tournament
    private Tournament tournament;
    private TournamentDAO tournamentDAO;

    //General Settings
    @FXML Text tournamentNameOutput;

    //TabPane and TableView settings
    @FXML TabPane matchesTabPaneOutput;
    @FXML Tab allMatchesTab;
    @FXML TableView matchTable;
    @FXML TableColumn teamsColumn;
    @FXML TableColumn dateColumn;
    @FXML TableColumn timeColumn;
    @FXML TableColumn infoColumn;
    @FXML MenuBar topMenuBar;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        //Makes the Tab non closable
        allMatchesTab.setClosable(false);
        teamsColumn.setCellValueFactory(new PropertyValueFactory<Match,String>("matchAsString"));
        matchTable.setEditable(false);

        matchTable.setRowFactory(tableView -> {
            TableRow<Match> row = new TableRow<>();
            row.setOnMouseClicked(event ->{
                if (! row.isEmpty() && event.getButton()== MouseButton.PRIMARY
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
     * @param event the event
     */
    @FXML
    public void switchToHomePage(ActionEvent event) {

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
        //Initialize file selection dialog.
        //TODO Consider more readable file extension name.
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Tournamint Files (*.qxz)", "*.qxz");
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(extensionFilter);
        fileChooser.setSelectedExtensionFilter(extensionFilter);
        fileChooser.setTitle("Open Tournament");

        //Show file selection dialog and get tournamentDAO from file.
        Stage window = (Stage) topMenuBar.getScene().getWindow();
        TournamentDAO tournamentDAO;
        try {
            File file = fileChooser.showOpenDialog(window);
            tournamentDAO = new TournamentDAO(file.getPath());
            tournamentDAO.load();
        }
        catch (Exception e) {
            System.out.println("Error loading tournament from file: " + e.getMessage());
            return;
        }
        initData(tournamentDAO);
    }

    /**
     * Saves tournament to new file and switches working directory.
     * TODO move save and load functionality to separate object to clean up UI code.
     */
    @FXML
    public void saveFile() {
        //Initialize file selection dialog.
        //TODO Consider more readable file extension name. Using .qxz because it does not collide with any known extensions.
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Tournamint Files (*.qxz)", "*.qxz");
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(extensionFilter);
        fileChooser.setSelectedExtensionFilter(extensionFilter);
        fileChooser.setInitialFileName(tournament.getTournamentName());
        fileChooser.setTitle("Save Tournament");
        //TODO Consider setting a default path to save new tournaments.

        //Show file selection dialog and get tournamentDAO from file.
        try {
            Stage window = (Stage) topMenuBar.getScene().getWindow();
            File file = fileChooser.showSaveDialog(window);
            tournamentDAO = new TournamentDAO(file.getPath());

            //Save tournament to given file path.
            tournamentDAO.save(tournament);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /*
    TODO avoid passing raw tournament objects; Pass DAO instead. Remove this block eventually!
    @FXML
    public void initData(Tournament tournament){
        this.tournament = tournament;
        tournamentNameOutput.setText(tournament.getTournamentName());
        updateTable();
    }
    */

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
        try { this.tournament = tournamentDAO.load(); }
        catch (IOException ioException) {
            System.out.println("Error in initData: " + ioException.getMessage());
            //TODO handle exception if loading somehow fails. Should not be possible at this point.
        }
        tournamentNameOutput.setText(tournament.getTournamentName());
        updateTable();
    }

    public void updateTournament() throws NoSuchFieldException{
        this.tournament.updateTournament();
    }



    public ObservableList<Match> getMatches(){
        ObservableList<Match> matchesObservable = FXCollections.observableArrayList();

        for(Round round : tournament.getAllRounds()){
            matchesObservable.addAll(round.getMatches());
        }

        return matchesObservable;
    }

    /**
     * Changes the scene to changeSceneToEnterMatchResult
     * Also uses the EnterResultsController tournament controller to
     * send the match instance.
     * @throws IOException if fxml file bad
     */
    @FXML
    public void enterResultEvent(MouseEvent event,Match match){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../view/EnterResults.fxml"));
            Parent administrateParent = loader.load();

            Scene administrateScene = new Scene(administrateParent);

            //Access the controller and call a method
            EnterResultsController controller = loader.getController();
            controller.initData(match,tournamentDAO);

            Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
            window.setScene(administrateScene);
            window.show();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void updateTable(){
        matchTable.setItems(getMatches());
    }
}