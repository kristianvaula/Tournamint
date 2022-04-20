package edu.ntnu.idatt1002.k1g01.model.matches;

import edu.ntnu.idatt1002.k1g01.model.Group;
import edu.ntnu.idatt1002.k1g01.model.Team;

import java.time.DateTimeException;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.time.format.DateTimeFormatter;

/**
 * Represents a match where the winner(s)
 * are based on who has the most points.
 *
 * @author kristjve
 */
public class TimeMatch extends Match{

    //Result of match based on points
    private HashMap<Team, LocalTime> timeResult = new HashMap<>();
    //DateTimeFormatter for formatting string output of LocalTime
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss:nn");

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
     * Preemptively creates match from other matches.
     * This match will eventually get its teams from the winners of these input matches.
     * @param previousMatches Match[] that will feed their winners to this match.
     * @implNote
     *          compiler doesn't like overloading with signature difference inside generic class.
     *          Therefore, raw array instead of ArrayList<?>
     * @author Martin Dammerud
     */
    public TimeMatch(int i, Match[] previousMatches) {
        super(i, previousMatches);
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
    public TimeMatch(int i, Group[] previousGroups) {
        super(i, previousGroups);
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
        if(!isFinished()) return null;
        HashMap<Team,String> results = new HashMap<>();
        for (Team team : timeResult.keySet()){
            results.put(team,TIME_FORMATTER.format(timeResult.get(team)));
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
                .sorted((k1,k2) -> k1.getValue().compareTo(k2.getValue()))
                .forEach(k -> sortedMap.put(k.getKey(),TIME_FORMATTER.format(timeResult.get(k.getKey()))));

        return sortedMap;
    }

    /**
     * Gets match result by as specific
     * team in that match.
     * @return The result of the match.
     */
    @Override
    public String getMatchResultByTeam(Team team) {
        return TIME_FORMATTER.format(timeResult.get(team));
    }

    /**
     * Sets result in timeResults.
     * Takes a String value for the result time.
     * For the result to be set it is important
     * that the result value is properly formatted.
     * The formatting happens in timeResultParser().
     * After setting result method calls for
     * updateIsFinished().
     *
     * @param team Team we add results for
     * @param value
     * @throws ClassCastException if match still contains TeamHolograms.
     */
    @Override
    public void setResult(Team team, String value) throws DateTimeParseException,DateTimeException{
        if (!playable()) throw new ClassCastException("Match needs winners from unfinished matches");

        //used to be inside a try block that did nothing.
        LocalTime result = this.timeResultParser(value);
        timeResult.put(team,result);

        updateIsFinished();
        updateMatchAsString(getParticipants(),getMatchResultOrdered());
    }

    /**
     * Checks if there is registered a
     * result on all participants in the match.
     * If all participants have result, sets
     * isFinished to true;
     */
    @Override
    public void updateIsFinished(){
        boolean hasResult = true;
        for(Team team : getParticipants()){
            if(!timeResult.containsKey(team)){
                hasResult = false;
                break;
            }
        }
        setFinished(hasResult);
    }

    /**
     * Formats a LocalTime from a string.
     * The string input is represented with
     * the following format:
     * <p>{code}
     *        'hours:minutes:seconds:milliseconds'
     *
     *        Example:
     *        1#  01:23:30:00
     *        2#  00:00:23:21
     * {code}</p>
     * @param inputString String input
     * @return Localtime parsed
     */
    public LocalTime timeResultParser(String inputString) throws DateTimeParseException, DateTimeException {
        String[] inputTable = inputString.split(":");
        int hours = Integer.parseInt(inputTable[0]);
        int minutes = Integer.parseInt(inputTable[1]);
        int seconds = Integer.parseInt(inputTable[2]);
        int milliseconds = Integer.parseInt(inputTable[3]) * 100;

        //Used to be inside a try block that did nothing.
        return LocalTime.of(hours,minutes,seconds,milliseconds);

    }
}
