package edu.ntnu.idatt1002.k1g01.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class DisplayModeController implements Initializable {

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}