package edu.ntnu.idatt1002.k1g01.model.matches;

import edu.ntnu.idatt1002.k1g01.model.Group;
import edu.ntnu.idatt1002.k1g01.model.Team;
import edu.ntnu.idatt1002.k1g01.model.TeamHologram;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents a match between at least two teams.
 * <br>Keeps track of the teams in the match and
 * the scores between the teams.
 *
 * @author kristvje
 */
public abstract class Match implements Serializable {
    //The match participants
    private ArrayList<Team> participants;
    //String representation of the participants, implemented for GUI
    private String participantsAsString;
    //String representation of the result, implemented for GUI
    private String resultAsString;
    //The time at which the match starts
    private LocalTime startTime = null;
    //The date at which the match starts
    private LocalDate matchDate = null;
    //Additional information about the match
    private String matchInfo = "";
    //If the match is finished or not
    private boolean finished = false;

    /**
     * Initiates a new Match.
     * <br> Takes participants as argument.
     * @param participants Team participants in the match
     */
    public Match(ArrayList<Team> participants) {
        this.participants = participants;

        String[] matchAsString = generateMatchAsString(participants,new HashMap<>());
        this.participantsAsString = matchAsString[0];
    }

    /**
     * Preemptively creates match from other matches.
     * This match will eventually get its teams from the winners of these input matches.
     * @param previousMatches Match[] that will feed their winners to this match.
     * @implNote
 *          compiler doesn't like overloading with signature difference inside generic class.
 *          Therefore, raw array instead of ArrayList<?>
     * @author Martin Dammerud
     */
    public Match(int advancingPerMatch, Match[] previousMatches) {
        this.participants = new ArrayList<>();
        for (Match match : previousMatches) for (int winner = 0; winner < advancingPerMatch; winner++) {
            this.participants.add(new TeamHologram(match, winner));
        }
    }

    /**
     * Preemptively creates match from Groups.
     * This match will eventually get its teams from the winners of these input matches.
     * @param previousGroups Group[] that will feed their winners to this match.
     * @implNote
     *      compiler doesn't like overloading with signature difference inside generic class.
     *      Therefore, raw array instead of ArrayList<?>
     * @author Martin Dammerud
     */
    public Match( int advancingPerGroup, Group[] previousGroups) {
        this.participants = new ArrayList<>();
        for (Group group : previousGroups) for (int winner = 0; winner < advancingPerGroup; winner++) {
            this.participants.add(new TeamHologram(group, winner));
        }
    }

    /**
     * Checks if any of teams in this match are just holograms.
     * Unpacks normal teams from holograms if possible.
     * If there are any holograms, then the match can't yet be played.
     * @return true if there
     */
    public boolean playable() {
        boolean output = true;
        for (int i = 0; i < participants.size(); i++) {
            Team team = participants.get(i);
            if (team.getClass() == TeamHologram.class) {
                Team trueTeam = team.getTrueTeam();
                if (trueTeam != null) participants.set(i, trueTeam);
                else output = false;
            }
        }
        return output;
    }

    /**
     * Gets match result
     * @return the match result
     */
    public abstract HashMap<Team,String> getMatchResult();

    /**
     * Sets the match result
     */
    public abstract void setResult(Team team, String value);

    /**
     * Gets match result in descending orders
     * @return LinkedHasMap results in descending order
     */
    public abstract LinkedHashMap<Team,String> getMatchResultOrdered();

    /**
     * Gets match result by as specific
     * team in that match.
     * @return The result of the match.
     */
    public abstract String getMatchResultByTeam(Team team);

    /**
     * Checks if there is registered a
     * result on all participants in the match.
     * If all participants have result, sets
     * isFinished to true;
     */
    public abstract void updateIsFinished();

    /**
     * Gets n match winners in descending
     * orders.<br> Gets the keySet from
     * getMatchResultOrdered() and creates a
     * list of teams ranked ordered by result.
     * Then adds n best teams to winners list.
     * @return ArrayList of n winning teams
     */
     public ArrayList<Team> getWinners(int n) {
     ArrayList<Team> winnerList = new ArrayList<>();

     Object[] teams = getMatchResultOrdered().keySet().toArray();

     for (int i = 0; i < n; i++) {
     winnerList.add((Team)teams[i]);
     }
     return winnerList;
     }

    /**
     * Checks if 2 or more teams have the exact same result
     * @return
     *      true: if there is at least 2 participants have the same result-value.
     *      false: if there is no draw.
     * @author Martin Dammerud
     */
    public boolean containsDraw() {
        return (new HashSet<>(getMatchResult().values()).size() < participants.size());
     }

    /**
     * Returns single Team that got n'th place in this match.
     * @param n place to get.
     * @return single n'th place Team.
     */
     public Team getWinner(int n) {
         return getWinners(participants.size()).get(n);
     }

    /**
     * Gets a copy of the list of
     * participants
     * @return copy of participants list
     */
    public ArrayList<Team> getParticipants() {
        return new ArrayList<>(participants);
    }

    /**
     * Gets the match start time
     * @return The LocalTime start time
     */
    public LocalTime getStartTime() {
        return startTime;
    }

    /**
     * Sets match start time
     * @param startTime LocalTime match start time
     */
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    /**
     * Gets the match date
     * @return The LocalDate match date
     */
    public LocalDate getMatchDate() {
        return matchDate;
    }

    /**
     * Sets match date
     * @param matchDate LocalDate match date
     */
    public void setMatchDate(LocalDate matchDate) {
        this.matchDate = matchDate;
    }

    /**
     * Gets match info
     * @return String match info
     */
    public String getMatchInfo() {
        return matchInfo;
    }

    /**
     * Sets match info
     * @param matchInfo String match info
     */
    public void setMatchInfo(String matchInfo) {
        this.matchInfo = matchInfo;
    }

    /**
     * is finished
     * @return true if finished
     */
    public boolean isFinished() {
        return finished;
    }

    /**
     * Sets finished
     * @param finished Boolean finished
     */
    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    /**
     * Gets participants as String
     * @return String participants
     */
    public String getParticipantsAsString() {
        return participantsAsString;
    }


    /**
     * Gets the result as String
     * @return result as string
     */
    public String getResultAsString(){
        return resultAsString;
    }

    /**
     * Gets a string representation of the
     * match start time in format HH:MM
     * @return String start time
     */
    public String getStartTimeAsString() {
        if(getStartTime() == null){
            return "";
        }
        return getStartTime().toString();
    }

    /**
     * Gets a string representation of the
     * match date in format D/M
     * @return String match date
     */
    public String getMatchDateAsString() {
        if(matchDate == null) return "";
        String month = matchDate.getMonth().toString().toLowerCase();
        return "" + matchDate.getDayOfMonth() + " . " + month;
    }

    /**
     * Returns the match participants,
     * and if there is a result this is also
     * added. Participants is added to the first
     * indexation and result as the next.
     * @return String[] participants
     */
    public static String[] generateMatchAsString(ArrayList<Team> participants,HashMap<Team,String> result){
        String[] returnString = new String[2];  // [0] = Teams [1] = Result
        returnString[0] = "";
        returnString[1] = "";
        if(!result.isEmpty()){ // If match has result
            Set<Team> keySet = result.keySet();
            ArrayList<Team> participantsOrdered = new ArrayList<>(keySet);
            if(participantsOrdered.size() == 2){ // If two participant teams
                returnString[0] = participantsOrdered.get(0).getName() + " vs "
                                + participantsOrdered.get(1).getName();
                returnString[1] =  result.get(participantsOrdered.get(0)) + " - "
                                    + result.get(participantsOrdered.get(1));
            }
            else if(participantsOrdered.size() > 2 ){ // More than two participant teams
                for (Team team : participantsOrdered){
                    returnString[0] += team.getName() + "\n";
                    returnString[1] += result.get(team) + "\n";
                }
            }
        }else{ // If match does not have result
            if(participants.size() == 2){ // If two participant teams
                returnString[0] = participants.get(0).getName() + " vs "
                                + participants.get(1).getName();
            }
            else if(participants.size() > 2 ) { // If more than two participant teams
                for (Team team : participants) {
                    returnString[0] += team.getName() + "\n";
                }
            }
        }
        return returnString;
    }

    /**
     * Updates the match as a String
     * if changes are made.
     */
    public void updateMatchAsString(ArrayList<Team> participants,HashMap<Team,String> result){
        String[] updatedString = generateMatchAsString(participants,result);
        this.participantsAsString = updatedString[0];
        if(updatedString[1] != null) {
            this.resultAsString = updatedString[1];
        }
    }

    /**
     * Checks if this Match is equivalent to other Match.
     * Matches are equal if (same start date/time && same participants).
     * @param other Match
     * @return true if equivalent.
     */
    public boolean equals(Match other) {
        if (this.startTime != other.getStartTime()) { return false; }
        if (this.matchDate != other.getMatchDate()) { return false; }
        if (this.participants.size() != other.getParticipants().size()) { return false; }
        for (Team participant : this.participants) {
            if (! other.getParticipants().contains(participant)) { return false; }
        }
        return true;
    }
}
