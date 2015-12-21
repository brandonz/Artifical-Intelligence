/**********************************************************************************/
// Edited By: Brandon Zhou
// netID: brandonz
// Course: COS 402
/**********************************************************************************/

/**
 * This is the template for a class that performs A* search on a given
 * rush hour puzzle with a given heuristic.  The main search
 * computation is carried out by the constructor for this class, which
 * must be filled in.  The solution (a path from the initial state to
 * a goal state) is returned as an array of <tt>State</tt>s called
 * <tt>path</tt> (where the first element <tt>path[0]</tt> is the
 * initial state).  If no solution is found, the <tt>path</tt> field
 * should be set to <tt>null</tt>.  You may also wish to return other
 * information by adding additional fields to the class.
 */
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Comparator;

public class AStar {

    /** The solution path is stored here */
    public State[] path;

    /**
     * This is the constructor that performs A* search to compute a
     * solution for the given puzzle using the given heuristic.
     */
    public AStar(Puzzle puzzle, Heuristic heuristic) {

	// your code here
		Comparator<Node> comparator = new NodeComparator(heuristic);
		HashSet<Integer> searchedSet = new HashSet<Integer>();
		PriorityQueue<Node> searchGraph = new PriorityQueue<Node>(1, comparator);
		searchGraph.add(puzzle.getInitNode());

		Node goal;
		while(true) {
			//check if searchGraph is empty
			if (searchGraph.peek() == null) {
				goal = null;
				break;
			}

			goal = (Node) searchGraph.poll();

			//check if goal is in the searchedSet
			if (searchedSet.contains(goal.getState().hashCode()))
				continue;

			//check if temp is the goal state
			if (goal.getState().isGoal())
				break;

			// add node to searched set
			searchedSet.add(goal.getState().hashCode());

			//add the neighbors to the pq
			Node[] neighbors = goal.expand();
			for (Node i : neighbors) {
				searchGraph.add(i);
			}
		}

		if (goal == null)
			path = null;
		else {
			//create path from goalNode
			int n = goal.getDepth() + 1;
			path = new State[n];
			for (int i = n-1; goal != null; i--) {
				path[i] = goal.getState();
				goal = goal.getParent();
			}
		}

    }

    // comparator for node comparisons (depth + heuristic)
    private class NodeComparator implements Comparator<Node> {
    	private Heuristic heuristic;
    	public NodeComparator(Heuristic heuristic) {
    		this.heuristic = heuristic;
    	}

    	public int compare(Node a, Node b) {
    		int fa = a.getDepth() + heuristic.getValue(a.getState());
    		int fb = b.getDepth() + heuristic.getValue(b.getState());
    		if (fa < fb)
    			return -1;
    		if (fa > fb)
    			return 1;
    		return 0;
    	}
    }

}
