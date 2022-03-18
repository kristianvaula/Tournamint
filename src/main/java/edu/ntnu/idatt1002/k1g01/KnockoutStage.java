package edu.ntnu.idatt1002.k1g01;

import edu.ntnu.idatt1002.k1g01.match.Match;

import java.util.ArrayList;

/**
 * Represents a KnockoutStage in the tournament (Subclass)
 */
public class KnockoutStage extends Stage {

    private Match match;
    static ArrayList<Match> matches = new ArrayList<>();
    static Round round = new Round(matches, getRoundName());
    static ArrayList<Round> rounds = new ArrayList<>();
    KnockoutStage knockoutStage = new KnockoutStage(rounds);

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
        Team winnerTeam = null;
        for(Round round : rounds) {
            if (round.getRoundName().equals("FINAL")) {
                winnerTeam = round.getMatches().get(0).getWinners(1).get(0);
            }
        }
        return winnerTeam;
    }

    /**
     * Get the teams that won the round and will participate in the next round
     * @return roundWinners, an ArrayList holding Team objects
     */
    public ArrayList<Team> getRoundWinners(int n) {
        ArrayList<Team> roundWinners = new ArrayList<>();
        for(int i = 0; i < matches.size(); i++) {
            for(int j = 0; j < n; j++) {
                roundWinners.add(round.getMatches().get(i).getWinners(n).get(j));
            }
        }
        return roundWinners;
    }

    /**
     * Get the name of the round
     * @return roundName, a String representing the name of the round
     */
    public static String getRoundName() {
        String roundName = "";
        for (int i = 0; i < rounds.size(); i++) {
            if (round.amountOfMatches() == 1) { roundName = "FINAL"; }
            else if (round.amountOfMatches() == 2) { roundName = "SEMIFINAL"; }
            else if (round.amountOfMatches() == 4) { roundName = "QUARTERFINAL"; }
            else if (round.amountOfMatches() == 8) { roundName = "ROUND OF 16"; }
            else { roundName = "GROUP MATCH"; }
        }
        return roundName;
    }

    /**
     * Generates the next round in the tournament
     */
    public void generateNextRound(int teamsInMatch) {

        

        ArrayList<Team> teams = getRoundWinners(n);

        Round nextRound = new Round(matches, getRoundName());

        // The matches in the next round (first parameter) will be the winners from the current round
        // The roundName should be automatically set depending on how many rounds from a final it is
    }

}
