import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BaseballElimination {
    private final int n;
    private final int[] wins;
    private final int[] losses;
    private final int[] remaining;
    private final int[][] games;
    private final Map<String, Integer> teamIDs;
    private final Map<Integer, String> teamNames;
    private final boolean[] cached;
    private final boolean[] eliminated;
    private final Map<Integer, Set<String>> certificates;

    public BaseballElimination(String filename) {
        In input = new In(filename);
        n = input.readInt();
        wins = new int[n];
        losses = new int[n];
        remaining = new int[n];
        games = new int[n][n];

        teamNames = new HashMap<>();
        teamIDs = new HashMap<>();
        for (int i = 0; i < n; i++) {
            String name = input.readString();
            teamIDs.put(name, i);
            teamNames.put(i, name);

            // the number of games
            wins[i] = input.readInt();
            losses[i] = input.readInt();
            remaining[i] = input.readInt();

            // remaining games against other team
            for (int j = 0; j < n; j++) {
                games[i][j] = input.readInt();
            }
        }

        cached = new boolean[n];
        eliminated = new boolean[n];
        certificates = new HashMap<>();
    }

    public int numberOfTeams() {
        return n;
    }

    public Iterable<String> teams() {
        return teamIDs.keySet();
    }

    public int wins(String team) {
        if (!teamIDs.containsKey(team)) {
            throw new IllegalArgumentException("invalid team");
        }
        return wins[teamIDs.get(team)];
    }

    public int losses(String team) {
        if (!teamIDs.containsKey(team)) {
            throw new IllegalArgumentException("invalid team");
        }
        return losses[teamIDs.get(team)];
    }

    public int remaining(String team) {
        if (!teamIDs.containsKey(team)) {
            throw new IllegalArgumentException("invalid team");
        }
        return remaining[teamIDs.get(team)];
    }

    public int against(String team1, String team2) {
        if (!teamIDs.containsKey(team1) || !teamIDs.containsKey(team2)) {
            throw new IllegalArgumentException("invalid team");
        }
        return games[teamIDs.get(team1)][teamIDs.get(team2)];
    }

    public boolean isEliminated(String team) {
        if (!teamIDs.containsKey(team)) {
            throw new IllegalArgumentException("invalid team");
        }
        int teamID = teamIDs.get(team);
        if (!cached[teamID]) {
            maxflow(teamID);
        }
        return eliminated[teamID];
    }

    public Iterable<String> certificateOfElimination(String team) {
        if (!teamIDs.containsKey(team)) {
            throw new IllegalArgumentException("invalid team");
        }
        int teamID = teamIDs.get(team);
        if (!cached[teamID]) {
            maxflow(teamID);
        }
        return certificates.get(teamID);
    }

    private FlowNetwork createFlowNetwork(int id, int s, int t) {
        int m = t - n;

        FlowNetwork network = new FlowNetwork(t + 1);
        int count = 0;
        for (int i = 0; i < n; i++) {
            if (i == id) continue;
            for (int j = i + 1; j < n; j++) {
                if (j == id) continue;
                count += 1;
                network.addEdge(new FlowEdge(s, count, games[i][j]));
                network.addEdge(new FlowEdge(count, m + i, Double.POSITIVE_INFINITY));
                network.addEdge(new FlowEdge(count, m + j, Double.POSITIVE_INFINITY));
            }
        }
        for (int i = 0; i < n; i++) {
            if (i == id) continue;
            int maxWin = wins[id] + remaining[id] - wins[i];
            network.addEdge(new FlowEdge(m + i, t, maxWin));
        }
        return network;
    }

    private void maxflow(int teamID) {
        cached[teamID] = true;

        // trivial elimination
        int maxWin = wins[teamID] + remaining[teamID];
        for (int i = 0; i < n; i++) {
            if (i == teamID) continue;
            if (wins[i] > maxWin) {
                eliminated[teamID] = true;
                certificates.put(teamID, Set.of(teamNames.get(i)));
                return;
            }
        }

        // non-trivial elimination
        int numTeams = n - 1;
        int numGames = numTeams * (numTeams - 1) / 2;
        int s = 0;
        int t = numGames + n + 1;
        int m = numGames + 1;
        FlowNetwork network = createFlowNetwork(teamID, s, t);
        FordFulkerson maxflow = new FordFulkerson(network, s, t);
        for (int v = 1; v < m; v++) {
            if (maxflow.inCut(v)) {
                eliminated[teamID] = true;
                Set<String> teams = new HashSet<>();
                for (int i = 0; i < n; i++) {
                    if (maxflow.inCut(m + i)) {
                        teams.add(teamNames.get(i));
                    }
                    certificates.put(teamID, teams);
                }
                return;
            }
        }

        // not mathematical eliminated
        eliminated[teamID] = false;
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
