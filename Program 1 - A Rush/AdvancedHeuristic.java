/**********************************************************************************/
// Edited By: Brandon Zhou
// netID: brandonz
// Course: COS 402
/**********************************************************************************/

/**
 * This is a template for the class corresponding to your original
 * advanced heuristic.  This class is an implementation of the
 * <tt>Heuristic</tt> interface.  After thinking of an original
 * heuristic, you should implement it here, filling in the constructor
 * and the <tt>getValue</tt> method.
 */
import java.util.HashSet;

public class AdvancedHeuristic implements Heuristic {

    private final int GOAL_CAR = 0;
    private Puzzle puzzle;
    private int fixedGoal;
    private int sizeGoal;
    private int gridSize;

    /**
     * This is the required constructor, which must be of the given form.
     */
    public AdvancedHeuristic(Puzzle puzzle) {

	// your code here
        this.puzzle = puzzle;
        fixedGoal = puzzle.getFixedPosition(GOAL_CAR);
        sizeGoal = puzzle.getCarSize(GOAL_CAR);
        gridSize = puzzle.getGridSize();
    }
	
    /**
     * This method returns the value of the heuristic function at the
     * given state.
     */

    // in addition to the number of blocking cars, this heuristic checks
    // the cars that block a blocking car as in order to move the 
    // blocking cars the car(s) that block the first blocking cars 
    // must be moved
    public int getValue(State state) {

	// your code here
        if (state.isGoal())
            return 0;

        int[][] grid = state.getGrid();
        HashSet<Integer> checked = new HashSet<Integer>();

        int heuristic = 1;
        int varGoal = state.getVariablePosition(GOAL_CAR);
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

            // if vertically-aligned cars are right of the goal car...
            if (orient && (varGoal + sizeGoal <= fixedPosition)) {
                // check if the car blocks the goal
                if ((varPosition <= fixedGoal) && 
                    (fixedGoal < varPosition + length)) {
                    heuristic++;

                    // if there is enough space to move blocking-car, 
                    // find min number of cars needed to move
                    // in order to move blocking-car out of the way

                    int blockingCars = Integer.MAX_VALUE;

                    // check above the car
                    if (0 <= (fixedGoal - length)) {
                        int count = 0;
                        for (int j = varPosition - 1; j >= fixedGoal - length; j--) {
                            int car = grid[fixedPosition][j];
                            if (car != -1) {
                                // this avoids double counting cars
                                // if a car is vertically aligned don't count it twice
                                // if a car is horizontall aligned don't double count it
                                if (puzzle.getCarOrient(car)) {
                                    j -= puzzle.getCarSize(car);
                                    count++;
                                }
                                else if (checked.contains(car)){
                                    continue;
                                }
                                else {
                                    checked.add(car);
                                    count++;
                                }
                            }
                        }
                        blockingCars = Math.min(blockingCars, count);
                    }

                    // check below the car
                    if ((fixedGoal + length) < gridSize) {
                        int count = 0;
                        for (int j = varPosition + length; j <= fixedGoal + length; j++) {
                            int car = grid[fixedPosition][j];
                            if (car != -1) {
                                if (puzzle.getCarOrient(car)) {
                                    j += puzzle.getCarSize(car);
                                    count++;
                                }
                                else if (checked.contains(car)){
                                    continue;
                                }
                                else {
                                    checked.add(car);
                                    count++;
                                }
                            }
                        }
                        blockingCars = Math.min(blockingCars, count);
                    }

                    heuristic += blockingCars;
                }
            }

        }
        return heuristic;
    }

}
