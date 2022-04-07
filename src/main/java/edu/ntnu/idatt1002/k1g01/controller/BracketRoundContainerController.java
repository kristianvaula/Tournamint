package edu.ntnu.idatt1002.k1g01.controller;

import edu.ntnu.idatt1002.k1g01.model.Round;
import edu.ntnu.idatt1002.k1g01.model.matches.Match;
import edu.ntnu.idatt1002.k1g01.model.stages.KnockoutStage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class BracketRoundContainerController implements Initializable {

    private final static String DLM = File.separator;
    @FXML private VBox BracketRoundContainer;
    private ArrayList<BracketMatchBlockController> matchControllers = new ArrayList<>();
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
            HBox matchBlock = loader.load(Objects
                    .requireNonNull(getClass().getResource("../view/BracketMatchBlock.fxml"))
                    .openStream());
            BracketRoundContainer.getChildren().add(matchBlock);
            BracketMatchBlockController controller = loader.getController();
            matchControllers.add(controller);
        }
    }




}
