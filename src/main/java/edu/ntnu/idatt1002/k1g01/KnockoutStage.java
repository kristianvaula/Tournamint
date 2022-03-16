package edu.ntnu.idatt1002.k1g01;

import java.util.ArrayList;

/**
 * Represents a KnockoutStage in the tournament (Subclass)
 */
public class KnockoutStage extends Stage {

    /**
     * Initiates a KnockoutStage
     * @param rounds an ArrayList of type Round
     */
    public KnockoutStage(ArrayList<Round> rounds) {
        super(rounds);
    }

    /**
     * Get the rounds in the KnockoutStage
     * @return rounds an ArrayList of type Round
     */
    @Override
    public ArrayList<Round> getRounds() {
        return super.getRounds();
    }

    /**
     * Get the team that won the final and the tournament
     * @return winnerTeam, an object of type Team
     */
    public Team getWinnerFromKnockouts() {
        // Pseudocode
        /*
        Team winnerTeam = null;
        for (Round : KnockoutStage) {
            if (Round.getName().equals("FINAL") {
            winnerTeam = Match.getWinner();
            }
        return winnerTeam;
        }
         */
        // Return the Winner of the last round in the knockout stage
        // This round should only contain one match - the final
    }

    /**
     * Get the teams that won the round and will participate in the next round
     * @return roundWinners, an ArrayList holding Team objects
     */
    public ArrayList<Team> getRoundWinners() {
        // Pseudocode
        /*
        ArrayList<Team> roundWinners = new ArrayList();
        for (Match : Round) {
            roundWinners.add(Match.getWinner();)
            }
        return roundWinners;
        }
         */
    }

    /**
     * Get the name of the round
     * @return roundName, a String representing the name of the round
     */
    public String getRoundName() {
        /*
        String[] roundNames = ["FINAL","SEMIFINAL","QUARTERFINAL","ROUND OF X"]; // X should return 16 for the ROUND OF 16, 32 for the ROUND OF 32, etc.
        String roundName = "";
        for (int i = 0; i < ...; i++) {
            roundName = roundNames[i];
        }
        return roundName;
         */
    }

    /**
     * Generates the next round in the tournament
     */
    public void generateNextRound() {
        // Pseudocode
        /*
        Round nextRound = new Round(getRoundWinners(), getRoundName());
         */
        // The matches in the next round (first parameter) will be the winners from the current round
        // The roundName should be automatically set depending on how many rounds from a final it is
    }

}
