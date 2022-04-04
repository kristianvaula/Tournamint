package edu.ntnu.idatt1002.k1g01.controller;

import edu.ntnu.idatt1002.k1g01.dao.TournamentDAO;
import edu.ntnu.idatt1002.k1g01.model.Round;
import edu.ntnu.idatt1002.k1g01.model.Team;
import edu.ntnu.idatt1002.k1g01.model.Tournament;
import edu.ntnu.idatt1002.k1g01.model.matches.Match;
import edu.ntnu.idatt1002.k1g01.model.stages.KnockoutStage;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DisplayModeController implements Initializable {

    //The nested controller for the menuBar
    @FXML private TopMenuBarController topMenuBarController;

    //The tournament variables
    private Tournament tournament;
    private TournamentDAO tournamentDAO;

    @FXML
    private Text tournamentNameOutput;




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
        tournamentNameOutput.setText(tournament.getTournamentName());
    }

    public void fillRoundOf16() {
        ArrayList<Match> matchesInRoundOf16 = new ArrayList<>();
        for(Round round : this.tournament.getKnockoutStage().getRounds()){
            if(round.getRoundName().equals("ROUND OF 16")){
                matchesInRoundOf16.addAll(round.getMatches());
            }
        }




    }









}