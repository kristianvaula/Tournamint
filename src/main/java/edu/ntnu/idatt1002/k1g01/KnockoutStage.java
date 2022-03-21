package edu.ntnu.idatt1002.k1g01;

import edu.ntnu.idatt1002.k1g01.matches.PointMatch;
import edu.ntnu.idatt1002.k1g01.matches.TimeMatch;

import java.util.ArrayList;
import java.util.Random;

/**
 * Represents a KnockoutStage in the tournament
 *
 * @author kristvje
 * @author thomaniv
 */
public class KnockoutStage extends Stage{

    private ArrayList<Team> teams = new ArrayList<>();
    private int teamsPerMatch;
    private int advancingPerMatch;
    private String tournamentType;

    /**
     * Initiates a KnockoutStage
     * @param teams ArrayList of teams we will include in groupStage.
     */
    public KnockoutStage(ArrayList<Team> teams,int teamsPerMatch,String tournamentType) throws IllegalArgumentException{
        super(generateKnockOutRounds(teams.size(),teamsPerMatch,teamsPerMatch/2));
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

    public int getTeamsPerMatch() {
        return teamsPerMatch;
    }

    public int getAdvancingPerMatch() {
        return advancingPerMatch;
    }

    /**
     * Generates the rounds needed in a knockout
     * stage based on the amount of teams. Checks if the
     * number of teams is compatible, and then generates
     * the rounds.
     * @param amountOfTeams the amount of teams that will play the first round
     * @return Rounds of knockout stage.
     */
    private static ArrayList<Round> generateKnockOutRounds(int amountOfTeams,int teamsPerMatch,int advancingPerMatch){
        int numberOfRounds = getNumberOfRounds(amountOfTeams,teamsPerMatch,advancingPerMatch);
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
     * @param numberOfTeams Number of teams
     * @param teamsPerRound Teams per round
     * @return Int number of rounds
     */
    private static int getNumberOfRounds(int numberOfTeams,int teamsPerRound,int advancingPerMatch){
        if(advancingPerMatch == 2){
            for (int i = 0; i < 12; i++) {
                if(Math.pow(teamsPerRound,i) == numberOfTeams && numberOfTeams <= 256) return i;
            }
        }
        else{
            for (int i = 0; i < 12; i++) {
                if(Math.pow(teamsPerRound,i +(advancingPerMatch/4)) == numberOfTeams && numberOfTeams <= 256) return i;
            }
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

    /**
     * Sets up a round.
     * Takes a round and a list of teams. Creates
     * matches in the round using the list of
     * teams.
     * @param round Round
     * @param teamList
     */
    public void roundSetUp(Round round,ArrayList<Team> teamList){
        ArrayList<Team> teams = new ArrayList<>(teamList);
        int amountOfMatches = teams.size()/getTeamsPerMatch();

        for (int i = 0; i < amountOfMatches; i++) {
            ArrayList<Team> participants = new ArrayList<>();

            for (int j = 0; j < getTeamsPerMatch(); j++) {
                Random rand = new Random();
                int index = rand.nextInt(teams.size());
                participants.add(teams.get(index));
                teams.remove(index);
            }
            if(tournamentType.equals("PointMatch")){
                round.addMatch(new PointMatch(participants));
            } else if(tournamentType.equals("TimeMatch")){
                round.addMatch(new TimeMatch(participants));
            }
        }
    }

    public void updateKnockoutStage(){
        ArrayList<Round> rounds = getRounds();

        if(rounds.get(0).getMatches().isEmpty()){
            this.roundSetUp(rounds.get(0),getTeams());
        }
        for (int i = 0; i < rounds.size()-1; i++) {
            //Loops through until we find round(i) is finished but
            //next round does not have matches.
            if(rounds.get(i).isFinished() && rounds.get(i+1).getMatches().isEmpty()){
                Round lastRound = rounds.get(i);
                Round nextRound = rounds.get(i+1);

                ArrayList<Team> winners = lastRound.getWinners(advancingPerMatch);
                this.roundSetUp(nextRound,winners);
            }
        }
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