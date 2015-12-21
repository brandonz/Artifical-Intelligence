/**********************************************************************************/
// Edited By: Brandon Zhou
// netID: brandonz
// Course: COS 402
/**********************************************************************************/

/**
 * This is a template for the class corresponding to the blocking
 * heuristic.  This heuristic returns zero for goal states, and
 * otherwise returns one plus the number of cars blocking the path of
 * the goal car to the exit.  This class is an implementation of the
 * <tt>Heuristic</tt> interface, and must be implemented by filling in
 * the constructor and the <tt>getValue</tt> method.
 */
public class BlockingHeuristic implements Heuristic {

    private final int GOAL_CAR = 0;
    private Puzzle puzzle;
    private int fixedGoal;
    private int sizeGoal;

    /**
     * This is the required constructor, which must be of the given form.
     */
    public BlockingHeuristic(Puzzle puzzle) {

	// your code here
        this.puzzle = puzzle;
        fixedGoal = puzzle.getFixedPosition(GOAL_CAR);
        sizeGoal = puzzle.getCarSize(GOAL_CAR);
    }
	

    /**
     * This method returns the value of the heuristic function at the
     * given state.
     */

    // count the number of cars blocking the goal car
    public int getValue(State state) {

	// your code here
        if (state.isGoal())
            return 0;

        int heuristic = 1;
        int varGoal = state.getVariablePosition(GOAL_CAR);

        // for each car check if it blocks the goal car
        for (int i = 1; i < puzzle.getNumCars(); i++) {
            int varPosition = state.getVariablePosition(i);
            int fixedPosition = puzzle.getFixedPosition(i);
            int length = puzzle.getCarSize(i);
            boolean orient = puzzle.getCarOrient(i);

            // this case should not occur, but check anyways
            if (!orient && (fixedPosition == fixedGoal) && 
                (varGoal + sizeGoal <= varPosition)) {
                heuristic++;
                continue;
            }

            // if vertically-aligned cars are right of the goal car
            if (orient && (varGoal + sizeGoal <= fixedPosition)) {
                // check if the car blocks the goal
                if ((varPosition <= fixedGoal) && 
                    (fixedGoal < varPosition + length))
                    heuristic++;
            }
        }
        return heuristic;
    }

}
