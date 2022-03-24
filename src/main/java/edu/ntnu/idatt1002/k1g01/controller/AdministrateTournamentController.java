package edu.ntnu.idatt1002.k1g01.controller;

import edu.ntnu.idatt1002.k1g01.model.Round;
import edu.ntnu.idatt1002.k1g01.model.Tournament;
import edu.ntnu.idatt1002.k1g01.model.matches.Match;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controls administration of tournament
 */
public class AdministrateTournamentController implements Initializable {

    //The tournament
    private Tournament tournament;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Makes the Tab non closable
        allMatchesTab.setClosable(false);
        teamsColumn.setCellValueFactory(new PropertyValueFactory<Match,String>("matchAsString"));
        matchTable.setEditable(false);
    }

    @FXML
    public void initData(Tournament tournament){
        this.tournament = tournament;
        tournamentNameOutput.setText(tournament.getTournamentName());
        updateTable();
    }

    public ObservableList<Match> getMatches(){
        ObservableList<Match> matchesObservable = FXCollections.observableArrayList();

        for(Round round : tournament.getAllRounds()){
            System.out.println(round.getMatches());
            matchesObservable.addAll(round.getMatches());
        }

        return matchesObservable;
    }

    @FXML
    public void updateTable(){
        matchTable.setItems(getMatches());
    }
}