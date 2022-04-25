package edu.ntnu.idatt1002.k1g01.model.stages;

import edu.ntnu.idatt1002.k1g01.model.Round;
import edu.ntnu.idatt1002.k1g01.model.Team;
import edu.ntnu.idatt1002.k1g01.model.TeamHologram;
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

        ArrayList<Round> rounds = getRounds();
        roundSetUp(rounds.get(0), teams);
        int roundCount = getNumberOfRounds(teams.size(), teamsPerMatch, advancingPerMatch);
        for (int i = 1; i < roundCount; i++) {

            ArrayList<Team> holoTeams = new ArrayList<>();
            for (Match match : rounds.get(i-1).getMatches()) {
                for (int w = 0; w < advancingPerMatch; w++) {
                    holoTeams.add(new TeamHologram(match, w));
                }
            }
            roundSetUp(rounds.get(i), holoTeams);
        }
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
     * TODO This method is replaced by a new version. Delete this version eventually.
     */
    public static int getNumberOfRoundsOld(int numberOfTeams,int teamsPerMatch,int advancingPerMatch){
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
     * Calculates how many rounds are needed to create a valid stage.
     * Also checks if given parameters are compatible.
     * @param numberOfTeams Number of teams in the knockoutStage.
     * @param teamsPerMatch Number of teams competing in each match.
     * @param advancingPerMatch Number of teams advancing from each match.
     * @return Number of rounds in this groupStage. 0 if parameters are incompatible.
     * @throws IllegalArgumentException If parameters are incompatible.
     * @author Martin Dammerud
     */
    public static int getNumberOfRounds(int numberOfTeams,int teamsPerMatch,int advancingPerMatch) throws IllegalArgumentException{
        System.out.println("calculating number of knockout rounds: ");
        System.out.println("numberOfTeams: " + numberOfTeams);
        System.out.println("teamsPerMatch: " + teamsPerMatch);
        System.out.println("advancingPerMatch: " + advancingPerMatch);
        if(teamsPerMatch % advancingPerMatch != 0) {//Make sure teamsPerMatch is divisible by advancingPerMatch.
            throw new IllegalArgumentException("Cannot create matches with " + teamsPerMatch +
                    " teams and " + advancingPerMatch + " advancing from each match");
        }
        int advanceRatio = teamsPerMatch / advancingPerMatch; //Represents number of matches feeding winners to next match.
        int exponent = -1; //iterated to 0 at beginning of loop.
        int lowerTeamsNeeded = 2;
        int upperTeamsNeeded = 2;
        int remainder = Integer.MAX_VALUE;
        while (remainder > 0) {
            exponent++;
            lowerTeamsNeeded = upperTeamsNeeded;
            upperTeamsNeeded = teamsPerMatch * (int)Math.pow(advanceRatio, exponent);
            remainder = numberOfTeams - upperTeamsNeeded;

        }
        if (remainder == 0) return exponent + 1;
        else throw new IllegalArgumentException("knockout stage got " + numberOfTeams + " teams. Needs " + lowerTeamsNeeded + " or "+
                upperTeamsNeeded + " teams");
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
        else if(roundNumber == 3){
            return "QUARTERFINAL";
        }
        else {
            return "ROUND OF " + (int)(Math.floor(Math.pow(2,roundNumber)));
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
                round.addMatch(match);
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