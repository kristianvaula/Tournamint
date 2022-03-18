package edu.ntnu.idatt1002.k1g01.matches;

import edu.ntnu.idatt1002.k1g01.Team;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.time.format.DateTimeFormatter;

/**
 * Represents a match where the winner(s)
 * are based on who has the most points.
 */
public class TimeMatch extends Match{

    //Result of match based on points
    private HashMap<Team, LocalTime> timeResult;

    //Sets constant time format to hour:minute:second:millisecond (f.eks: 00:10:04:1023)
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss:AA");

    /**
     * Initiates a new Match.
     * <br> Takes participants as argument.
     *
     * @param participants Team participants in the match
     */
    public TimeMatch(ArrayList<Team> participants) {
        super(participants);
    }

    /**
     * Gets all match result.
     * Returns a HashMap consisting of the
     * match participants as keys and their
     * times as values.
     * @return the match result
     */
    @Override
    public HashMap<Team, String> getMatchResult() {
        HashMap<Team,String> results = new HashMap<>();
        for (Team team : timeResult.keySet()){
            results.put(team,timeResult.get(team).toString());
        }
        return results;
    }

    /**
     * Gets match result in descending orders.
     * <br> Creates a LinkedHashMap sortedMap. Uses stream
     * on all entries of timeResult and compare the times,
     * then adds them to sortedMap.
     * @return LinkedHasMap results in descending order
     */
    @Override
    public LinkedHashMap<Team, String> getMatchResultOrdered() {
        LinkedHashMap<Team,String> sortedMap = new LinkedHashMap<>();
        timeResult.entrySet()
                .stream()
                .sorted((k1,k2) -> -k1.getValue().compareTo(k2.getValue()))
                .forEach(k -> sortedMap.put(k.getKey(),timeResult.get(k.getValue()).toString()));

        return sortedMap;
    }

    /**
     * Gets match result by as specific
     * team in that match.
     * @return The result of the match.
     */
    @Override
    public String getMatchResultByTeam(Team team) {
        return timeResult.get(team).toString();
    }

    /**
     * Gets n match winners in descending
     * orders.<br> Gets the keySet from
     * getMatchResultOrdered() and creates a
     * list of teams ranked ordered by result.
     * Then adds n best teams to winners list.
     * @return ArrayList of n winning teams

    @Override
    public ArrayList<Team> getWinners(int n) {
        ArrayList<Team> winnerList = new ArrayList<>();

        Team[] teams = (Team[])getMatchResultOrdered().keySet().toArray();

        for (int i = 0; i < n; i++) {
            winnerList.add(teams[i]);
        }
        return winnerList;
    }*/

    /**
     * Sets result in timeResults.
     * Takes a String value for the result time.
     * For the result to be set it is important
     * that the result value is properly formatted.
     * <p>{code}
     * Time format:
     *  'hours:minutes:seconds:milliseconds'
     *
     *  Example:
     *  1#  01:23:30:0000
     *  2#  00:00:23:2130
     * {code}</p>
     * @param team Team we add results for
     * @param value
     */
    @Override
    public void setResult(Team team, String value) throws DateTimeParseException{
        try{
            LocalTime result = LocalTime.parse(value,TIME_FORMAT);
            timeResult.put(team,result);
        }catch(DateTimeParseException e){
            throw e;
        }
    }
}
