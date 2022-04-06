package edu.ntnu.idatt1002.k1g01.controller;

import edu.ntnu.idatt1002.k1g01.dao.TournamentDAO;
import edu.ntnu.idatt1002.k1g01.model.Round;
import edu.ntnu.idatt1002.k1g01.model.Team;
import edu.ntnu.idatt1002.k1g01.model.Tournament;
import edu.ntnu.idatt1002.k1g01.model.matches.Match;
import edu.ntnu.idatt1002.k1g01.model.stages.KnockoutStage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class DisplayModeController implements Initializable {

    private final static String DLM = File.separator;
    //The nested controller for the menuBar
    @FXML private TopMenuBarController topMenuBarController;

    @FXML
    private Text nameField;

    private Tournament tournament;

    //labels for 1/8 finals
    @FXML private Label slot1, slot2, slot3, slot4,slot5, slot6, slot7, slot8, slot9, slot10, slot11, slot12, slot13, slot14, slot15, slot16;
    @FXML private Label points1, points2, points3, points4, points5, points6, points7, points8, points9, points10, points11, points12, points13, points14, points15, points16;

    //Labels for the quarterfinals
    @FXML private Label quarterfinalSlot1, quarterfinalSlot2, quarterfinalSlot3, quarterfinalSlot4, quarterfinalSlot5, quarterfinalSlot6, quarterfinalSlot7, quarterfinalSlot8;
    @FXML private Label quarterfinalPoints1, quarterfinalPoints2, quarterfinalPoints3, quarterfinalPoints4, quarterfinalPoints5, quarterfinalPoints6, quarterfinalPoints7, quarterfinalPoints8;

    //Labels for the semifinals
    @FXML private Label semifinalSlot1, semifinalSlot2, semifinalSlot3, semifinalSlot4;
    @FXML private Label semifinalPoints1, semifinalPoints2, semifinalPoints3, semifinalPoints4;

    //Labels for the final
    @FXML private Label finalSlot1, finalSlot2;
    @FXML private Label finalPoints1, finalPoints2;

    public ArrayList<Label> arrangeLabels() {
        ArrayList<Label> slots = new ArrayList<>(); slots.add(quarterfinalPoints1); slots.add(quarterfinalPoints2);
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