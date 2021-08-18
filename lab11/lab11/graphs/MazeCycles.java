package lab11.graphs;

/**
 *  @author Yu-Te Kuo
 */
public class MazeCycles extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private Maze maze;
    private int s;
    private boolean isCycleExist = false;
    private int[] parent;

    public MazeCycles(Maze m) {
        super(m);
        maze = m;
        s = maze.xyTo1D(1, 1);
        distTo[s] = 0;
        parent = new int[maze.V()];
        for (int i = 0; i < maze.V(); i++) {
            parent[i] = i;
        }
    }

    @Override
    public void solve() {
        dfs(s);
    }

    private void dfs(int v) {
        if (isCycleExist) {
            return;
        }

        marked[v] = true;
        announce();

        for (int w : maze.adj(v)) {
            if (!marked[w]) {
                distTo[w] = distTo[v] + 1;
                parent[w] = v;
                dfs(w);
            }

            if (parent[v] != w) {
                isCycleExist = true;
                int u = v;
                while (u != w) {
                    edgeTo[u] = parent[u];
                    u = parent[u];
                }
                edgeTo[w] =v;
                announce();
                return;
            }
        }
    }
}

