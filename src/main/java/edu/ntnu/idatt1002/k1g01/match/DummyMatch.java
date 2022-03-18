package edu.ntnu.idatt1002.k1g01.match;

import edu.ntnu.idatt1002.k1g01.Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class DummyMatch extends Match{

    public DummyMatch(ArrayList<Team> participants) { super(participants); }

    @Override
    public HashMap<Team, String> getMatchResult() {
        return null;
    }

    @Override
    public LinkedHashMap<Team,String> getMatchResultOrdered() {
        return null;
    }

    @Override
    public String getMatchResultByTeam(Team team) {
        return null;
    }

    @Override
    public ArrayList<Team> getWinners(int n) {
        return null;
    }

    @Override
    public void setResult(Team team, int result) {}
}
