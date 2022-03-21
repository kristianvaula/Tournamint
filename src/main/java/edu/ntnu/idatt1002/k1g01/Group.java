package edu.ntnu.idatt1002.k1g01;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import edu.ntnu.idatt1002.k1g01.matches.*;

/**
 * Contains a list of teams who all play one match against all others.
 * TODO Create groups with more than 2 teams per match.
 * Matches are divided among rounds so that every match in a round can be played concurrently.
 * @author Martin Dammerud
 */
public class Group {
    private final ArrayList<Team> teams;
    private final ArrayList<Round> rounds;
    private final ArrayList<Match> matches;

    /**
     * Creates a group of teams. Every team will be paired up in a match against every other team.
     * Matches will be grouped into rounds, so that every match in a round can be played concurrently.
     * @param matchType Class of matches to generate. Example: PointMatch.class
     * @param teams ArrayList of teams for the group.
     * @throws IllegalArgumentException if < 2 teams, or any duplicate teams in input.
     */
    public Group( Class matchType, ArrayList<Team> teams) {
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
                    if (matchType == PointMatch.class) {
                        tempMatch = new PointMatch(teamsForMatch);
                    }
                    else if (matchType == TimeMatch.class) {
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
        int maxMatchCount = 0;
        while (tempMatches.size() > 0) {
            ArrayList<Match> matchesInRound = new ArrayList<>();
            ArrayList<Team> teamsInRound = new ArrayList<>();
            ArrayList<Team> deferredTeams = new ArrayList<>();
            //First, check if any team has more matches so far than others.
            for (int i = 0; i < teams.size(); i++) {
                if (matchCountPerTeam[i] >= maxMatchCount) {
                    deferredTeams.add(teams.get(i));
                }
            }
            //Add as many matches as possible to this round.
            //Look for matches with no deferred teams.
            for (Match tempMatch : tempMatches) {
                boolean teamsFree = true;
                for (Team team : tempMatch.getParticipants()) {
                    if (teamsInRound.contains(team) || deferredTeams.contains(team)) { teamsFree = false; break; }
                }
                if (teamsFree) {
                    matchesInRound.add(tempMatch);
                    teamsInRound.addAll(tempMatch.getParticipants());
                    for (Team matchTeam: tempMatch.getParticipants()) {
                        matchCountPerTeam[teams.indexOf(matchTeam)]++;
                    }
                }
            }
            //Look for matches with only one deferred team.
            for (Match tempMatch : tempMatches) {
                int teamsFree = 2;
                for (Team team : tempMatch.getParticipants()) {
                    if (teamsInRound.contains(team)) { teamsFree = 0; break; }
                    else if(deferredTeams.contains(team)) { teamsFree--; }
                }
                if (teamsFree >= 1) {
                    matchesInRound.add(tempMatch);
                    teamsInRound.addAll(tempMatch.getParticipants());
                    for (Team matchTeam: tempMatch.getParticipants()) {
                        matchCountPerTeam[teams.indexOf(matchTeam)]++;
                    }
                }
            }
            //Look for matches with deferred teams.
            for (Match tempMatch : tempMatches) {
                boolean teamsFree = true;
                for (Team team : tempMatch.getParticipants()) {
                    if (teamsInRound.contains(team)) { teamsFree = false; break; }
                }
                if (teamsFree) {
                    matchesInRound.add(tempMatch);
                    teamsInRound.addAll(tempMatch.getParticipants());
                    for (Team matchTeam: tempMatch.getParticipants()) {
                        matchCountPerTeam[teams.indexOf(matchTeam)]++;
                    }
                }
            }
            //Update maxMatchCountPerTeam, used for deferring teams next round.
            for (int count : matchCountPerTeam) {
                if (count > maxMatchCount) {
                    maxMatchCount = count;
                    break;
                }
            }
            tempMatches.removeAll(matchesInRound);
            rounds.add(new Round(matchesInRound, "groupRound_" + groupRound));
            groupRound++;
        }
    }

    /**
     * Overloaded constructor. Does not require team list on the form of an ArrayList.
     * Creates a group of teams. Every team will be paired up in a match against every other team.
     * Matches will be grouped into rounds, so that every match in a round can be played concurrently.
     * @param matchType Class of matches to generate. Example: PointMatch.class
     * @param teams teams for the group.
     * @throws IllegalArgumentException if < 2 teams, or any duplicate teams in input.
     */
    public Group( Class matchType, Team ...teams) {
        this(matchType, new ArrayList<>(Arrays.asList(teams)));
    }

    //Dumb getters
    public ArrayList<Team> getTeams() { return teams; }
    public ArrayList<Round> getRounds() { return rounds; }
    public ArrayList<Match> getMatches() { return matches; }
    public int getTeamCount() { return teams.size(); }
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

    /**
     * Finds the n best performing teams in this group.
     * WARNING! Only accounts for single best team in each match, as this class currently only handles 2 teams per match.
     * TODO: Handle draws better! Currently awards group victory to team with lowest list index.
     * @param n Number of teams to fetch.
     * @return ArrayList of top teams in descending order. Null if some matches are not finished.
     * @throws NoSuchFieldException if not all matches in group are finished.
     */
    public ArrayList<Team> getTopTeams(int n) throws NoSuchFieldException {
        if (n > teams.size()) {
            throw new IndexOutOfBoundsException("Requested top "+n+" teams from group with only "+teams.size()+" teams!");
        }
        //Get number of wins per team.
        int[] points = new int[teams.size()];
        for (Match match : matches) {
            if (! match.isFinished()) {
                throw new NoSuchFieldException("Not all matches have finished");
            }
            Team winner = match.getWinners(1).get(0);
            points[teams.indexOf(winner)]++;
        }
        //Assemble output ArrayList.
        ArrayList<Team> output = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            int highScore = Integer.MIN_VALUE + 1;
            int bestTeamIndex = 0;
            for (int x = 0; x < points.length; x++) {
                if (points[x] > highScore) {
                    highScore = points[x];
                    bestTeamIndex = x;
                }
            }
            output.add(teams.get(bestTeamIndex));
            points[bestTeamIndex] = Integer.MIN_VALUE;
        }
        return output;
    }
}
