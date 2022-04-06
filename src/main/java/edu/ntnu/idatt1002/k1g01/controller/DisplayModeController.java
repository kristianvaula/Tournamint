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

    //The tournament variables
    private Tournament tournament;
    private TournamentDAO tournamentDAO;
    @FXML private HBox outerHbox;
    private ArrayList<BracketRoundContainerController> roundControllers = new ArrayList<>();

    @FXML
    private Text tournamentNameOutput;




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("displayMode initialize start");
        System.out.println("    tournament ptr = " + tournament);
    }

    /**
     * Starts session for administrating a tournament with TournamentDAO.
     * Makes file containing tournament accessible so that it can be easily updated frequently.
     * TODO give user reassuring feedback whenever tournament file is updated.
     * TODO better user feedback for errors.
     * @param tournamentDAO DAO for tournament object. Must be non-null.
     */
    public void initData(TournamentDAO tournamentDAO) {
        System.out.println("    display initData");
        this.tournamentDAO = tournamentDAO;
        topMenuBarController.setTournamentDAO(tournamentDAO); //Pass DAO pointer to nested controller.
        try {
            System.out.println("    try load");
            this.tournament = tournamentDAO.load();
            System.out.println("    tournament ptr = " + tournament);
        }
        catch (IOException ioException) {
            System.out.println("Error in initData: " + ioException.getMessage());
            //TODO handle exception if loading somehow fails. Should not be possible at this point.
        }
        tournamentNameOutput.setText(tournament.getTournamentName());
        System.out.println("    try set text");

        if (!(tournament.getKnockoutStage() == null && tournament.getKnockoutStage().getRounds().isEmpty())) {
            try {
                System.out.println("    trying setBracketRoundContainers");
                setBracketRoundContainers();
                System.out.println("    setBracketRoundContainers done");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * method for setting round bracket containers in the display mode for knockout stage based on
     * number of rounds in the knockout stage.
     * @throws IOException
     */
    public void setBracketRoundContainers() throws IOException {
        System.out.println("        setBracketRoundContainers start");
       int numberOfRounds = this.tournament.getKnockoutStage().getRounds().size();
        if (numberOfRounds == 0) throw new IllegalArgumentException("       Number of rounds in knockout stage is zero");
        else if (numberOfRounds > 4) {
            for (int i = numberOfRounds - 4; i < numberOfRounds; i++) {
                addBracketRoundContainer(this.tournament.getKnockoutStage().getRounds().get(i));
            }
        } else {
            for (int i = 0; i < numberOfRounds; i++) {
                addBracketRoundContainer(this.tournament.getKnockoutStage().getRounds().get(i));
            }
        }
        System.out.println("        setBracketRoundContainers done");
    }

    /**
     * method for adding a BracketRoundContainer to the display section.
     * @throws IOException
     */
    public void addBracketRoundContainer(Round round) throws IOException {
        System.out.println("            Adding BracketRoundContainer");
        FXMLLoader loader = new FXMLLoader();
        System.out.println("            FXML loaded");
        System.out.println("            DLM = " + DLM);
        VBox BracketRoundContainer = loader.load(Objects.requireNonNull(getClass().getResource("../view/BracketRoundContainer.fxml")).openStream());
        System.out.println("            BracketRoundContainer ptr = " + BracketRoundContainer);
        outerHbox.getChildren().add(BracketRoundContainer);
        BracketRoundContainerController controller = loader.getController();
        System.out.println("            controller ptr = " + controller);
        controller.setMatchesInRoundContainers(round);
        roundControllers.add(controller);
    }




}