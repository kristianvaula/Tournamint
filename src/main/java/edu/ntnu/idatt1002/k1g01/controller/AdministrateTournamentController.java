package edu.ntnu.idatt1002.k1g01.controller;

import edu.ntnu.idatt1002.k1g01.model.Tournament;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TabPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
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

    //TabPane setting
    @FXML TabPane matchesTabPaneOutput;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    public void initData(Tournament tournament){
        this.tournament = tournament;
        tournamentNameOutput.setText(tournament.getTournamentName());
        initTable();
    }

    @FXML
    public void initTable(){

    }
}