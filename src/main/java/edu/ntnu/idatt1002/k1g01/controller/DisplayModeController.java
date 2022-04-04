package edu.ntnu.idatt1002.k1g01.controller;

import edu.ntnu.idatt1002.k1g01.model.Round;
import edu.ntnu.idatt1002.k1g01.model.Team;
import edu.ntnu.idatt1002.k1g01.model.Tournament;
import edu.ntnu.idatt1002.k1g01.model.matches.Match;
import edu.ntnu.idatt1002.k1g01.model.stages.KnockoutStage;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DisplayModeController implements Initializable {

    //The nested controller for the menuBar
    @FXML private TopMenuBarController topMenuBarController;

    @FXML
    private Text nameField;

    private Tournament tournament;

    //TextFields for the quarterfinals
    @FXML
    private TextField quarterfinalSlot1, quarterfinalSlot2, quarterfinalSlot3, quarterfinalSlot4, quarterfinalSlot5, quarterfinalSlot6, quarterfinalSlot7, quarterfinalSlot8;
    @FXML private TextField quarterfinalPoints1, quarterfinalPoints2, quarterfinalPoints3, quarterfinalPoints4, quarterfinalPoints5, quarterfinalPoints6, quarterfinalPoints7, quarterfinalPoints8;

    //TextFields for the semifinals
    @FXML private TextField semifinalSlot1, semifinalSlot2, semifinalSlot3, semifinalSlot4;
    @FXML private TextField semifinalPoints1, semifinalPoints2, semifinalPoints3, semifinalPoints4;

    //TextFields for the final
    @FXML private TextField finalSlot1, finalSlot2;
    @FXML private TextField finalPoints1, finalPoints2;

    public ArrayList<TextField> arrangeTextFields() {
        ArrayList<TextField> slots = new ArrayList<>(); slots.add(quarterfinalPoints1); slots.add(quarterfinalPoints2);
        slots.add(quarterfinalSlot3); slots.add(quarterfinalSlot4); slots.add(quarterfinalSlot5); slots.add(quarterfinalSlot7);
        slots.add(quarterfinalSlot8);
        return slots;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameField.setText(tournament.getTournamentName());
    }

    @FXML
    public void initData(Tournament tournament) {
        this.tournament = tournament;
    }










}