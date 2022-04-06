package edu.ntnu.idatt1002.k1g01.model.stages;

import edu.ntnu.idatt1002.k1g01.model.Round;
import edu.ntnu.idatt1002.k1g01.model.Team;
import edu.ntnu.idatt1002.k1g01.model.matches.Match;
import edu.ntnu.idatt1002.k1g01.model.matches.PointMatch;
import edu.ntnu.idatt1002.k1g01.model.matches.TimeMatch;

import java.util.ArrayList;
import java.util.Random;

/**
 * Represents a KnockoutStage in the tournament
 *
 * @author kristvje
 * @author thomaniv
 */
public class KnockoutStage extends Stage {

    private ArrayList<Team> teams;
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
        update();
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
    private static ArrayList<Round> generateKnockOutRounds(int amountOfTeams, int teamsPerMatch, int advancingPerMatch){
        int numberOfRounds = getNumberOfRounds(amountOfTeams,teamsPerMatch,advancingPerMatch);
        if(numberOfRounds == 0) {
            throw new IllegalArgumentException("Combination of teams and tournament settings is invalid");
        }

        ArrayList<Round> rounds = new ArrayList<>();

        for (int i = numberOfRounds; i > 0; i--) {
            rounds.add(new Round(new ArrayList<>(),generateRoundName(i)));
        }

        return rounds;
    }

    /**
     * Checks which power of teamsPerMatch equals the
     * amount of teams. Gives us the amount of
     * rounds we need in our tournament. Returns
     * 0 if we dont get a match, which means that
     * the amount of teams is not compatible.
     * @param numberOfTeams Number of teams
     * @param teamsPerMatch Teams per round
     * @return Int number of rounds
     */
    public static int getNumberOfRounds(int numberOfTeams,int teamsPerMatch,int advancingPerMatch){
        System.out.println("calculating number of knockout rounds: ");
        System.out.println("numberOfTeams: " + numberOfTeams);
        System.out.println("teamsPerMatch: " + teamsPerMatch);
        System.out.println("advancingPerMatch: " + advancingPerMatch);
        // If 2 teams per match
        if(teamsPerMatch == 2 && advancingPerMatch == 1){
            for (int i = 0; i < 12; i++) {
                if(Math.pow(teamsPerMatch,i) == numberOfTeams) return i;
            }
        }// If 4 teams per match
        else if(teamsPerMatch == 4){
            for (int i = 0; i < 12; i++) {
                if(Math.pow((teamsPerMatch/advancingPerMatch),i) == numberOfTeams){
                    return i - (advancingPerMatch-1);
                }
            }
        }// If 8 teams per match
        else if(teamsPerMatch == 8){
            for (int i = 0; i < 12; i++) {
                if(teamsPerMatch * Math.pow((teamsPerMatch/advancingPerMatch),i) == numberOfTeams){
                    return i + 1;
                }
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
            if(tournamentType.equals("timeMatch")){
                Match match = new TimeMatch(participants);
                match.setMatchInfo(round.getRoundName());
                round.addMatch(match);
            } else{
                Match match = new PointMatch(participants);
                match.setMatchInfo(round.getRoundName());
                round.addMatch(new PointMatch(participants));
            }
        }
    }

    /**
     * Generates new rounds if necessary.
     * Generates new matches for next round if previous round is finished.
     */
    @Override
    public void update(){
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
                if(round.isFinished()){
                    return round.getMatches().get(0).getWinners(1).get(0);
                }
            }
        }
        return null;
    }

}