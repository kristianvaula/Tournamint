package edu.ntnu.idatt1002.k1g01;

import edu.ntnu.idatt1002.k1g01.matches.Match;
import edu.ntnu.idatt1002.k1g01.matches.PointMatch;
import edu.ntnu.idatt1002.k1g01.matches.TimeMatch;

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
    public ArrayList<Team> getRoundWinners(int amountOfWinners) {
        ArrayList<Team> roundWinners = new ArrayList<>();
        for(int i = 0; i < matches.size(); i++) {
            for(int j = 0; j < amountOfWinners; j++) {
                roundWinners.add(round.getMatches().get(i).getWinners(amountOfWinners).get(j));
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
    public void generateNextRound(int amountOfWinners, int numberOfParticipantsInMatches, int numberOfMatchesInRound, char matchType) {
        ArrayList<Team> allParticipants = new ArrayList<>(getRoundWinners(amountOfWinners));
        ArrayList<Match> matches = new ArrayList<>();
        Match match;
        for (int m = 0; m < numberOfMatchesInRound; m++) {
            ArrayList<Team> matchParticipants = new ArrayList<>(getRoundWinners(amountOfWinners));
            for (int i = 0; i < numberOfParticipantsInMatches; i++) {
                matchParticipants.add(allParticipants.get(i));
                allParticipants.remove(i);
            }
            if (matchType == 'P') {
                match = new PointMatch(matchParticipants);
                matches.add(match);
            }
            else if (matchType == 'T') {
                match = new TimeMatch(matchParticipants);
                matches.add(match);
            }
        }
        Round nextRound = new Round(matches, getRoundName());
    }
}
