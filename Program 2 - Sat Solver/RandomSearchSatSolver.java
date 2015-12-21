import java.util.*;

/** This is a trivial implementation of <tt>SatSolver</tt> that simply
 * generates random models and returns the best one when time runs
 * out.  You can use this class as a sample when writing your own.
 */
public class RandomSearchSatSolver implements SatSolver {

    private Random random;   // a random number generator
    private String author = "Rob Schapire";
    private String description = "This sat-solver generates random models "
	+ "and returns the best one when time runs out.";

    /** A constructor for this class.
     */
    public RandomSearchSatSolver() {
	random = new Random();
    }

    /** This routine attempts to solve the satisfaction problem by
     * trying random models until time runs out and returning the best
     * one.
     */
    public boolean[] solve(Literal[][] cnf, int num_symbols, Timer timer) {

	boolean[] model = new boolean[num_symbols];
	boolean[] best_model = new boolean[num_symbols];
	int best_num_not_satd = cnf.length + 1;
	int num_not_satd;

	while(timer.getTimeRemaining() >= 0) {

	    for (int i = 0; i < num_symbols; i++)
		model[i] = random.nextBoolean();

	    num_not_satd = Cnf.numClauseUnsat(cnf, model);

	    if (num_not_satd < best_num_not_satd) {
		if (num_not_satd == 0)
		    return model;
		best_num_not_satd = num_not_satd;
		for (int i = 0; i < num_symbols; i++)
		    best_model[i] = model[i];
	    }
	}

	return best_model;
    }

    /** The author of this sat-solver. */
    public String author() {
	return author;
    }

    /** A brief description of this sat-solver. */
    public String description() {
	return description;
    }

}
		

