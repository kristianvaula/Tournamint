package edu.ntnu.idatt1002.k1g01.matches;

import edu.ntnu.idatt1002.k1g01.Team;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Represents a match between at least two teams.
 * <br>Keeps track of the teams in the match and
 * the scores between the teams.
 */
public abstract class Match {
    //The match participants
    private ArrayList<Team> participants;
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
    }

    /**
     * Gets match result
     * @return the match result
     */
    public abstract HashMap<Team,String> getMatchResult();

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
     * Gets n match winners in descending
     * orders.<br> Gets the keySet from
     * getMatchResultOrdered() and creates a
     * list of teams ranked ordered by result.
     * Then adds n best teams to winners list.
     * @return ArrayList of n winning teams
     */
     public ArrayList<Team> getWinners(int n) {
     ArrayList<Team> winnerList = new ArrayList<>();

     Team[] teams = (Team[])getMatchResultOrdered().keySet().toArray();

     for (int i = 0; i < n; i++) {
     winnerList.add(teams[i]);
     }
     return winnerList;
     }

    /**
     * Sets the match result
     */
    public abstract void setResult(Team team, String value);

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
     * Gets a string representation of the
     * match start time in format HH:MM
     * @return String start time
     */
    public String getStartTimeAsString() {
        return "" + startTime.getHour() + " : " + startTime.getMinute();
    }

    /**
     * Gets a string representation of the
     * match date in format D/M
     * @return String match date
     */
    public String getMatchDateAsString() {
        return "" + matchDate.getDayOfMonth() + " / " + matchDate.getMonth();
    }
}
