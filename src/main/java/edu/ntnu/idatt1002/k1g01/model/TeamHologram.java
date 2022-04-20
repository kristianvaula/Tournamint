package edu.ntnu.idatt1002.k1g01.model;

import edu.ntnu.idatt1002.k1g01.model.matches.Match;

/**
 * A nameless placeholder team.
 * When input match is finished, this team will take the name of the specified winner of that match
 * and make the true Match.getWinner() available.
 * Handy when matches need to be constructed before their teams are ready.
 * @author Martin Dammerud
 */
public class TeamHologram extends Team{
    Match match;
    Group group;
    int winner;

    /**
     * Creates this placeholder team.
     * @param match Match that this team will eventually get its name from.
     * @param winner index for Match.getWinner(winner).
     */
    public TeamHologram(Match match, int winner) {
        if (match == null) throw new NullPointerException("Match cannot be null!");
        if (winner > match.getParticipants().size()) {
            throw new IndexOutOfBoundsException("Impossible to get winner: " + winner + " from match with "
                    + match.getParticipants().size() + " teams");
        }
        this.match = match;
        this.winner = winner;
        this.name = " - ";
    }

    /**
     * Creates this placeholder team.
     * @param group Group that this team will eventually get its name from.
     * @param winner index for Group.getTopTeams(winner).
     */
    public TeamHologram(Group group, int winner) {
        if (group == null) throw new NullPointerException("Match cannot be null!");
        if (winner > group.size()) {
            throw new IndexOutOfBoundsException("Impossible to get winner: " + winner + " from group with "
                    + group.size() + " teams");
        }
        this.group = group;
        this.winner = winner;
        this.name = " - ";
    }

    /**
     * @return "?" if input match is not finished. Name of match winner if match is finished.
     */
    @Override
    public String getName() {
        Team trueTeam = getTrueTeam();
        if (trueTeam != null) this.name = trueTeam.getName();
        return name;
    }

    /**
     * Attempts to return the specified winner of input match.
     * @return Match winner Team, or null if match in not finished.
     */
    public Team getTrueTeam() {
        if (match != null && match.isFinished()) return match.getWinner(winner);
        if (group != null && group.isFinished()) return group.getWinner(winner);
        return null;
    }
}
