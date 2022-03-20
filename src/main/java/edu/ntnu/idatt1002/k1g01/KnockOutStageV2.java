package edu.ntnu.idatt1002.k1g01;

import edu.ntnu.idatt1002.k1g01.matches.Match;
import edu.ntnu.idatt1002.k1g01.matches.PointMatch;
import edu.ntnu.idatt1002.k1g01.matches.TimeMatch;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.DuplicateFormatFlagsException;
import java.util.Random;

/**
 * Represents a KnockoutStage in the tournament (Subclass)
 */
public class KnockoutStageV2 extends Stage{

    private ArrayList<Team> teams = new ArrayList<>();
    private int teamsPerMatch;
    private int advancingPerMatch;
    private String tournamentType;

    /**
     * Initiates a KnockoutStage
     * @param teams ArrayList of teams we will include in groupStage.
     */
    public KnockoutStageV2(ArrayList<Team> teams,int teamsPerMatch,String tournamentType) throws IllegalArgumentException{
        super(generateKnockOutRounds(teams.size(),teamsPerMatch));
        this.teams = teams;
        this.teamsPerMatch = teamsPerMatch;
        this.advancingPerMatch =teamsPerMatch/2;
        this.tournamentType = tournamentType;
        updateKnockoutStage();
    }

    public ArrayList<Team> getTeams() {
        return teams;
    }

    public void setTeams(ArrayList<Team> teams) {
        this.teams = teams;
    }

    /**
     * Get the team that won the final and the tournament
     * @return winnerTeam, an object of type Team
     */
    public Team getWinnerFromKnockouts() {
        Team winnerTeam = null;
        for(Round round : getRounds()) {
            if (round.getRoundName().equals("FINAL")) {
                winnerTeam = round.getMatches().get(0).getWinners(1).get(0);
            }
        }
        return winnerTeam;
    }

    /**
     * Generates the rounds needed in a knockout
     * stage based on the amount of teams. Checks if the
     * number of teams is compatible, and then generates
     * the rounds.
     * @param amountOfTeams the amount of teams that will play the first round
     * @return Rounds of knockout stage.
     */
    private static ArrayList<Round> generateKnockOutRounds(int amountOfTeams,int teamsPerMatch){
        int numberOfRounds = getNumberOfRounds(amountOfTeams,teamsPerMatch);
        if(numberOfRounds == 0) {
            throw new IllegalArgumentException("Amount of teams must be divisible by four");
        }

        ArrayList<Round> rounds = new ArrayList<>();

        for (int i = numberOfRounds; i > 0; i--) {
            rounds.add(new Round(new ArrayList<>(),generateRoundName(i)));
        }

        return rounds;
    }

    /**
     * Checks which power of teamsPerRound equals the
     * amount of teams. Gives us the amount of
     * rounds we need in our tournament. Returns
     * 0 if we dont get a match, which means that
     * the amount of teams is not compatible.
     * @param n Number of teams
     * @return Int number of rounds
     */
    private static int getNumberOfRounds(int numberOfTeams,int teamsPerRound){
        for (int i = 0; i < 12; i++) {
            if(Math.pow(teamsPerRound,i) == numberOfTeams && numberOfTeams <= 256) return i;
        }
        return 0;
    }

    /**
     * Generates a round name
     * @param roundNumber Which round it is counting from final
     * @return String round name
     */
    private static String generateRoundName(int roundNumber) {
        if(roundNumber == 1) {
            return "FINAL";
        }
        else if(roundNumber == 2){
            return "SEMIFINAL";
        }
        else if(roundNumber == 2){
            return "QUARTERFINAL";
        }
        else {
            return "ROUND OF " + Math.pow(2,roundNumber);
        }
    }

    public void createFirstRound(){
        ArrayList<Round> rounds = getRounds();
        ArrayList<Team> teams = new ArrayList<>(getTeams());
        int matches = teams.size()/teamsPerMatch;

        Round firstRound = rounds.get(0);

        for (int i = 0; i < matches; i++) {
            ArrayList<Team> participants = new ArrayList<>();

            for (int j = 0; j < teamsPerMatch; j++) {
                Random rand = new Random();
                int index = rand.nextInt(teams.size());
                participants.add(teams.get(index));
                teams.remove(index);
            }
            if(tournamentType.equals("PointMatch")){
                firstRound.addMatch(new PointMatch(participants));
            }else{
                firstRound.addMatch(new TimeMatch(participants));
            }
        }
    }

    public void updateKnockoutStage(){
        ArrayList<Round> rounds = getRounds();
        int matches = teams.size()/teamsPerMatch;

        for (int i = 0; i < rounds.size(); i++) {
            //Loops through until we find round(i) is finished but
            //next round does not have matches.
            if(rounds.get(i).isFinished() && rounds.get(i+1).getMatches().isEmpty()){
                Round lastRound = rounds.get(i);
                Round nextRound = rounds.get(i+1);

                ArrayList<Match> nextRoundMatches = new ArrayList<>();

                ArrayList<Team> winners = lastRound.getWinners(advancingPerMatch);
                nextRound.setMatches();
            }
        }
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

    /**
     * Gets the Tournament winner
     * @return Team winner
     */
    public Team getTournamentWinner() {
        for(Round round : getRounds()){
            if(round.getRoundName().equals("FINAL")){
                return round.getMatches().get(0).getWinners(1).get(0);
            }
        }
        return null;
    }

}