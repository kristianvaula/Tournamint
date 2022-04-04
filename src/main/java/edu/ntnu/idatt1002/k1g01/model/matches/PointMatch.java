package edu.ntnu.idatt1002.k1g01.model.matches;

import edu.ntnu.idatt1002.k1g01.model.Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Represents a match where the winner(s)
 * are based on who has the most points.
 *
 * @author kristjve
 */
public class PointMatch extends Match{
    //Result of match based on points
    private HashMap<Team,Integer> pointResult = new HashMap<>();

    /**
     * Initiates a new Match.
     * <br> Takes participants as argument.
     *
     * @param participants Team participants in the match
     */
    public PointMatch(ArrayList<Team> participants) {
        super(participants);
    }

    /**
     * Gets all match result
     * Returns a HashMap consisting of the
     * match participants as keys and their
     * points as values.
     * @return the match result
     */
    @Override
    public HashMap<Team, String> getMatchResult() {
        HashMap<Team,String> results = new HashMap<>();
        for (Team team : pointResult.keySet()){
            results.put(team,String.valueOf(pointResult.get(team)));
        }
        return results;
    }

    /**
     * Gets match result in descending orders.
     * <br> Creates a LinkedHashMap sortedMap. Uses stream
     * on all entries of pointResult and compare the values,
     * then adds them to sortedMap.
     * @return LinkedHasMap results in descending order
     */
    @Override
    public LinkedHashMap<Team, String> getMatchResultOrdered() {
        LinkedHashMap<Team,String> sortedMap = new LinkedHashMap<>();
        pointResult.entrySet()
                .stream()
                .sorted((k1,k2) -> -k1.getValue().compareTo(k2.getValue()))
                .forEach(k -> sortedMap.put(k.getKey(),String.valueOf(k.getValue())));

        return sortedMap;
    }

    /**
     * Gets match result by as specific
     * team in that match.
     * @return The result of the match.
     */
    @Override
    public String getMatchResultByTeam(Team team) {
        return String.valueOf(pointResult.get(team));
    }

    /**
     * Sets result in pointsResult.
     * After setting result method calls for
     * updateIsFinished().
     * @param team Team we add results for
     * @param value String number value
     */
    @Override
    public void setResult(Team team, String value) throws NumberFormatException{
        try {
            pointResult.put(team,Integer.parseInt(value));
        } catch (NumberFormatException e){
            throw new NumberFormatException("Result not a number");
        }
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
            if(!pointResult.containsKey(team)){
                hasResult = false;
            }
        }
        setFinished(hasResult);
    }
}
