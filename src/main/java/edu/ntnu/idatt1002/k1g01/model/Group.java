package edu.ntnu.idatt1002.k1g01.model;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;

import edu.ntnu.idatt1002.k1g01.model.matches.*;

/**
 * Contains a list of teams who all play one match against all others.
 * TODO Create groups with more than 2 teams per match.
 * Matches are divided among rounds so that every match in a round can be played concurrently.
 * @author Martin Dammerud
 */
public class Group implements Serializable {
    private final ArrayList<Team> teams;
    private final ArrayList<Round> rounds;
    private final ArrayList<Match> matches;

    /**
     * Recursive nightmare method.
     * Uses recursion to navigate search space of possible match combinations until an optimal round is found.
     * Round is optimal when:
     *      0 or 1 team plays no matches this round.
     *      No team has played more than 1 match more than any other team so far this group.
     * @param tempMatches ArrayList of unallocated matches.
     * @param teamsInRound ArrayList of teams that have already played this round ,and are thus unavailable.
     * @param targetRoundSize Number of matches to add to round. (Also used to break out of recursion.)
     * @param matchCountPerTeam Array that tracks how many matches each team has been allocated so far this group.
     *                          (should be as even as possible)
     * @return
     *      null if no valid match combinations were found. (Should only be returned from recursive calls.)
     *      ArrayList of matches that make up an optimal round.
     */
    private ArrayList<Match> generateRound(ArrayList<Match> tempMatches, ArrayList<Team> teamsInRound,
                                           int targetRoundSize, int[] matchCountPerTeam) {
        if (targetRoundSize == 0) {return new ArrayList<>(); } //Break out of recursive loop
        int maxMatchCount = 0;
        for (int count : matchCountPerTeam) {
            if (count > maxMatchCount) { maxMatchCount = count; }
        }
        //Sort tempMatch, prioritizing marches between teams with fewer allocated matches.
        //Disregard matches with teams that are already busy this round.
        ArrayList<Match> sortedTempMatches = new ArrayList<>();
        for (int n = 2; n >= 0; n--) {
            for (Match match : tempMatches) {
                int teamsFree = 2;
                for (Team team : match.getParticipants()) {
                    if (teamsInRound.contains(team)) { teamsFree = -9001; break; }
                    if (matchCountPerTeam[teams.indexOf(team)] >= maxMatchCount) {
                        teamsFree--;
                    }
                }
                if (teamsFree == n) {
                    sortedTempMatches.add(match);
                }
            }
        }

        //Attempt to assemble complete round from sortedTempMatches.
        for (Match match : sortedTempMatches) {
            ArrayList<Match> sendMatches = new ArrayList<>(tempMatches);
            sendMatches.remove(match);
            ArrayList<Team> sendTeams = new ArrayList<>(teamsInRound);
            sendTeams.addAll(match.getParticipants());
            int[] sendMatchCountPerTeam = matchCountPerTeam.clone();
            for (Team team : match.getParticipants()) {
                sendMatchCountPerTeam[teams.indexOf(team)]++;
            }
            // Recursive shenanigans. Calls self to continue assembling round until targetRoundSize == 0;
            ArrayList<Match> gotMatches = generateRound(sendMatches, sendTeams, targetRoundSize-1,
                    sendMatchCountPerTeam);
            if (gotMatches != null) { // Check if lower level of this method was able to assemble valid round.
                gotMatches.add(match);
                return gotMatches; // Pass partially or fully assembled round up the stack.
            }
        }
        return null; // Tell higher level of this method that no valid round could be found.
    }

    /**
     * Creates a group of teams. Every team will be paired up in a match against every other team.
     * Matches will be grouped into rounds, so that every match in a round can be played concurrently.
     * @param matchType Type of match as a string. Accepted: [pointMatch, timeMatch]
     * @param teams ArrayList of teams for the group.
     * @throws IllegalArgumentException if < 2 teams, or any duplicate teams in input.
     * @throws ClassCastException if matchType string does not match any known types.
     * @throws IllegalStateException if round generation algorithm fails.
     * TODO WARNING! Round generation algorithm currently fails for groups with > 12 teams.
     */
    public Group( String matchType, ArrayList<Team> teams) {
        //validate input.
        if (teams.size() < 2) {
            throw new IllegalArgumentException("Attempted to create group with " + teams.size() + " teams. Minimum group size: 2!");
        }
        HashSet<Team> teamSet = new HashSet<>(teams);
        if (teamSet.size() != teams.size())  {
            throw new IllegalArgumentException("Attempted to create group with duplicate team entries!");
        }

        //Initialize class fields.
        rounds = new ArrayList<>();
        ArrayList<Match> tempMatches = new ArrayList<>();
        matches = new ArrayList<>();
        this.teams = teams;

        //Find all unique unordered match pairs.
        for (Team teamA : teams) {
            for (Team teamB: teams) {
                if (teamA != teamB) {
                    ArrayList<Team> teamsForMatch = new ArrayList<>();
                    teamsForMatch.add(teamA); teamsForMatch.add(teamB);
                    Match tempMatch;
                    if (matchType.equals("pointMatch")) {
                        tempMatch = new PointMatch(teamsForMatch);
                    }
                    else if (matchType.equals("timeMatch")) {
                        tempMatch = new TimeMatch(teamsForMatch);
                    }
                    else {throw new ClassCastException("Error in Group constructor: Unknown Match type!"); }
                    boolean unique = true;
                    for (Match match : matches) {
                        if (tempMatch.equals(match)) { unique = false; break; }
                    }
                    if (unique) { matches.add(tempMatch); tempMatches.add(tempMatch); }
                }
            }
        }

        //Divide match pairs into minimum number of rounds so that each team has <= 1 entry per round.
        int groupRound = 1;
        int[] matchCountPerTeam = new int[teams.size()];
        while (tempMatches.size() > 0) {
            ArrayList<Match> matchesInRound;
            //Get matches for round from spooky recursive method.
            matchesInRound = generateRound(tempMatches, new ArrayList<>(), teams.size()/2,
                    matchCountPerTeam);
            if (matchesInRound == null) {
                throw new IllegalStateException("Critical round generation failure!");
            }
            tempMatches.removeAll(matchesInRound);
            for (Match match : matchesInRound) {
                for (Team team : match.getParticipants()) {
                    matchCountPerTeam[teams.indexOf(team)]++;
                }
            }
            rounds.add(new Round(matchesInRound, "groupRound_" + groupRound));
        }
    }

    /**
     * Overloaded constructor. Does not require team list on the form of an ArrayList.
     * Creates a group of teams. Every team will be paired up in a match against every other team.
     * Matches will be grouped into rounds, so that every match in a round can be played concurrently.
     * @param matchType Type of match as a string. Accepted: [point, time]
     * @param teams teams for the group.
     * @throws IllegalArgumentException if < 2 teams, or any duplicate teams in input.
     */
    public Group( String matchType, Team ...teams) {
        this(matchType, new ArrayList<>(Arrays.asList(teams)));
    }

    /**
     * Returns a LinkedHasMap with n teams sorted from highest to lowest score, and their current score.
     * @param n number of teams to get.
     * @return LinkedHashMap of Team, points.
     */
    public LinkedHashMap<Team, Integer> getStanding(int n) {
        if (n > teams.size()) {
            throw new IndexOutOfBoundsException("Requested top " + n + " teams from group with only " + teams.size() + " teams!");
        }

        //Get number of group points per team.
        int[] points = new int[teams.size()];
        for (Match match : matches) {
            if (match.isFinished()) {
                if (match.containsDraw()) {
                    for (Team team : match.getParticipants()) {
                        points[teams.indexOf(team)]++;
                    }
                }
                else {
                    Team winner = match.getWinner(0);
                    points[teams.indexOf(winner)] += 2;// 2 points for a win.
                }
            }
        }

        //Assemble output LinkedHashmap.
        LinkedHashMap<Team, Integer> output = new LinkedHashMap<>();
        for (int i = 0; i < n; i++) {
            int highScore = Integer.MIN_VALUE + 1;
            int bestTeamIndex = 0;
            for (int x = 0; x < points.length; x++) {
                if (points[x] > highScore) {
                    highScore = points[x];
                    bestTeamIndex = x;
                }
            }
            output.put(teams.get(bestTeamIndex), points[bestTeamIndex]);
            points[bestTeamIndex] = Integer.MIN_VALUE;
        }
        return output;
    }

    /**
     * Returns a LinkedHasMap of all teams sorted from highest to lowest score, and their current score.
     * @return LinkedHashMap of Team, points.
     */
    public LinkedHashMap<Team, Integer> getStanding() {
        return getStanding(size());
    }

    /**
     * Finds the n best performing teams in this group.
     * WARNING! Only accounts for single best team in each match, as this class currently only handles 2 teams per match.
     * TODO: Handle draws better! Currently awards group victory to team with lowest list index.
     * @param n Number of teams to fetch.
     * @return ArrayList of top teams in descending order. Null if some matches are not finished.
     */
    public ArrayList<Team> getTopTeams(int n) {
        if (size() < n) throw new IndexOutOfBoundsException("Requested top "+n+" teams from group with only "+ size()+" teams!");
        for (Match match : matches) if (!match.isFinished()) return null;
        return new ArrayList<>(getStanding(n).keySet());
    }

    /**
     * Returns single Team that got n'th place in this group.
     * @param n place to get.
     * @return single n'th place Team.
     */
    public Team getWinner(int n) {
        return getTopTeams(teams.size()).get(n);
    }

    //Dumb getters
    public ArrayList<Team> getTeams() {return teams; }
    public Team getTeam(int i) { return teams.get(i); }
    public ArrayList<Round> getRounds() { return rounds; }
    public Round getRound(int i) { return rounds.get(i); }
    public ArrayList<Match> getMatches() { return matches; }
    public Match getMatch(int i) { return matches.get(i); }
    public int size() { return teams.size(); }
    public int getRoundCount() { return rounds.size(); }
    public int getMatchCount() { return matches.size(); }

    /**
     * Checks if every match in the group has been played.
     * @return true if all matches are done.
     */
    public boolean isFinished() {
        for (Match match : matches) {
            if (! match.isFinished()) { return false; }
        }
        return true;
    }
}

