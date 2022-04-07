package edu.ntnu.idatt1002.k1g01.controller;

import edu.ntnu.idatt1002.k1g01.model.Round;
import edu.ntnu.idatt1002.k1g01.model.Tournament;
import edu.ntnu.idatt1002.k1g01.model.matches.Match;
import edu.ntnu.idatt1002.k1g01.model.stages.KnockoutStage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class BracketRoundContainerController implements Initializable {

    private Tournament tournament;
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
            double spacing = (BracketRoundContainer.getPrefHeight() - (round.amountOfMatches()* 64))/(2*round.amountOfMatches());
            double margin = (int) Math.floor(spacing);
            BracketRoundContainer.getChildren().add(matchBlock);
            if(spacing<0)BracketRoundContainer.setMargin(matchBlock,new Insets(0,5,0,0));
            else BracketRoundContainer.setMargin(matchBlock,new Insets(margin,5,margin,0));
            BracketMatchBlockController controller = loader.getController();
            matchControllers.add(controller);
        }
    }
}
