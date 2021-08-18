package lab11.graphs;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 *  @author Yu-Te Kuo
 */
public class MazeBreadthFirstPaths extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private int s;
    private int t;
    private boolean targetFound = false;
    private Maze maze;

    public MazeBreadthFirstPaths(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
    }

    /** Conducts a breadth first search of the maze starting at the source. */
    private void bfs() {
        Queue<Integer> fringe = new ArrayDeque<>();
        fringe.add(s);
        marked[s] = true;
        announce();

        while (!fringe.isEmpty()) {
            int v = fringe.remove();
            if (v == t) {
                targetFound = true;
                return;
            }

            for (int w : maze.adj(v)) {
                if (!marked[w]) {
                    marked[w] = true;
                    fringe.add(w);
                    distTo[w] = distTo[v] + 1;
                    edgeTo[w] = v;
                    announce();
                }
            }
        }
    }

    @Override
    public void solve() {
        bfs();
    }
}

