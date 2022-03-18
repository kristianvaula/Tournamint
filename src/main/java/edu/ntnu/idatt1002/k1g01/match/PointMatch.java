package edu.ntnu.idatt1002.k1g01.match;

import edu.ntnu.idatt1002.k1g01.Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Represents a match where the winner(s)
 * are based on who has the most points.
 */
public class PointMatch extends Match{
    //Result of match based on points
    private HashMap<Team,Integer> pointResult;

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
     * Gets match result by as specific
     * team in that match.
     * @return The result of the match.
     */
    @Override
    public String getMatchResultByTeam(Team team) {
        return String.valueOf(pointResult.get(team));
    }

    /**
     * Gets match result
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
     * Gets n match winners in descending
     * orders.<br> Gets the keySet from
     * getMatchResultOrdered() and creates a
     * list of teams ranked ordered by result.
     * Then adds n best teams to winners list.
     * @return ArrayList of n winning teams
     */
    @Override
    public ArrayList<Team> getWinners(int n) {
        ArrayList<Team> winnerList = new ArrayList<>();

        Team[] teams = (Team[])getMatchResultOrdered().keySet().toArray();

        for (int i = 0; i < n; i++) {
            winnerList.add(teams[i]);
        }
        return winnerList;
    }

    /**
     * Sets result in pointsResult
     * @param team Team we add results for
     * @param points Points for the team in the match
     */
    @Override
    public void setResult(Team team, int points){
        pointResult.put(team,Integer.valueOf(points));
    }
}
