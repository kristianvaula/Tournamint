package edu.ntnu.idatt1002.k1g01.controller;

import edu.ntnu.idatt1002.k1g01.model.Round;
import edu.ntnu.idatt1002.k1g01.model.matches.Match;
import edu.ntnu.idatt1002.k1g01.model.stages.KnockoutStage;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class BracketRoundContainerController implements Initializable {

    private final static String DLM = File.separator;
    private VBox BracketRound;
    private ArrayList<BracketMatchBlockController> matchControllers;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    /**
     * method for adding MatchBlocks to roundContainers depending on how many matches in each round
     * @param round round of the knockout stage
     * @throws IOException
     */
    public void setMatchesInRoundContainers(Round round) throws IOException {
        for (Match match : round.getMatches()) {
            FXMLLoader loader = new FXMLLoader();
            VBox roundContainer = loader.load(Objects.requireNonNull(getClass().getResource("edu" + DLM + "ntnu" + DLM + "idatt1002" + DLM + "k1g01" + DLM + "view" + DLM + "BracketMatchBlock.fxml")));
            BracketRound.getChildren().add(roundContainer);
            BracketMatchBlockController controller = loader.getController();
            matchControllers.add(controller);
        }
    }




}
