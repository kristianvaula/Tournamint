package edu.ntnu.idatt1002.k1g01.controller;

import edu.ntnu.idatt1002.k1g01.model.Team;
import edu.ntnu.idatt1002.k1g01.model.matches.Match;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class BracketMatchBlockController implements Initializable {

    @FXML private Label Team1;
    @FXML private Label Team2;
    @FXML private Label PointsTeam1;
    @FXML private Label PointsTeam2;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    /**
     * method that sets info of team names and results in a BracketMatchBlock
     * @param match of type Match
     */
    public void setMatchInfo(Match match) {
        Team firstTeam = match.getParticipants().get(0);
        Team secondTeam = match.getParticipants().get(1);
        Team1.setText(firstTeam.getName());
        Team2.setText(secondTeam.getName());
        if (match.isFinished()) {
            HashMap<Team, String> matchInfo = match.getMatchResult();
            PointsTeam1.setText(matchInfo.get(firstTeam));
            PointsTeam2.setText(matchInfo.get(secondTeam));
        }
    }


}
