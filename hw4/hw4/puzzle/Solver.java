package hw4.puzzle;

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

public class Solver {
    private int totalMoves = 0;
    private SearchNode goal;
    private Stack<WorldState> seqOfWorldState = new Stack<>();

    private class SearchNode implements Comparable<SearchNode> {
        private WorldState worldState;
        private int moves;
        private SearchNode prev;
        private int priority;

        private SearchNode(WorldState ws, int m, SearchNode p) {
            worldState = ws;
            moves = m;
            prev = p;
            priority = moves + ws.estimatedDistanceToGoal();
        }

        @Override
        public int compareTo(SearchNode o) {
            return priority - o.priority;
        }
    }

    /**
     * Constructor which solves the puzzle, computing everything necessary for
     * moves() and solution() to not have to solve the problem again. Solves the
     * puzzle using the A* algorithm. Assumes a solution exists.
     */
    public Solver(WorldState initial) {
        MinPQ<SearchNode> pq = new MinPQ<>();
        SearchNode initialSearchNode = new SearchNode(initial, 0, null);
        pq.insert(initialSearchNode);

        while (!pq.isEmpty()) {
            SearchNode X = pq.delMin();
            if (X.worldState.isGoal()) {
                goal = X;
                totalMoves = goal.moves;
                break;
            }

            for (WorldState n : X.worldState.neighbors()) {
                if (X.prev == null || !n.equals(X.prev.worldState)) {
                    pq.insert(new SearchNode(n, X.moves + 1, X));
                }
            }
        }

        while (goal != null) {
            seqOfWorldState.push(goal.worldState);
            goal = goal.prev;
        }
    }

    /**
     * Returns the minimum number of moves to solve the puzzle starting
     * at the initial WorldState.
     */
    public int moves() {
        return totalMoves;
    }

    /**
     * Returns a sequence of WorldStates from the initial WorldState
     * to the solution.
     */
    public Iterable<WorldState> solution() {
        return seqOfWorldState;
    }
}
