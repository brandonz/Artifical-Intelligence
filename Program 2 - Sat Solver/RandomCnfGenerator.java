import java.util.*;

/** This <tt>Generator</tt> generates random CNF formulas of a fixed
 * size over a fixed number of symbols and with a fixed number of
 * literals per clause.  This class also includes a <tt>main</tt>
 * method for testing.
 */
public class RandomCnfGenerator implements Generator {

    private int num_clauses;  // number of clauses
    private int num_symbols;  // number of symbols
    private int num_literals; // number of literals per clause

    private Random random;  // random number generator

    /** Constructor for this object.
     *  @param num_clauses  number of clauses in constructed CNF
     *  @param num_symbols  number of symbols (variables) in CNF
     *  @param num_literals number of literals per clause
     */
    public RandomCnfGenerator(int num_clauses, int num_symbols, int num_literals) {
	random = new Random();
	this.num_clauses = num_clauses;
	this.num_symbols = num_symbols;
	this.num_literals = num_literals;
    }

    /** Generates another CNF of the specified form. */
    public Cnf getNext() {
	Cnf cnf = new Cnf();

	for (int i = 0; i < num_clauses; i++) {
	    cnf.addClause();
	    for (int j = 0; j < num_literals; j++) {
		cnf.addLiteral("X" + random.nextInt(num_symbols),
			       random.nextBoolean());
	    }
	}
	return cnf;
    }

    /** The author of this generator. */
    public String author() {
	return "Rob Schapire";
    }

    /** A brief description of this generator. */
    public String description() {
	return "Generates random CNF formulas over " + num_symbols +
	    " symbols, with " + num_clauses + " clauses and " +
	    num_literals + " literals per clause.";
    }

    /** This is a simple <tt>main</tt>.  It generates a random CNF
     * formula with number of clauses, number of symbols and number of
     * literals per clause specified by the first three arguments.
     * Once generated, <tt>MySatSolver</tt> is called to try to solve
     * the CNF within the time limit specified by the fourth argument.
     * This process is repeated the number of times specified by the
     * fifth argument.
     */
    public static void main(String[] argv) {
	int num_clauses = 0;
	int num_symbols = 0;
	int num_literals = 0;
	int time_limit = 0;
	int num_reps = 0;
	try {
	    num_clauses = Integer.parseInt(argv[0]);
	    num_symbols = Integer.parseInt(argv[1]);
	    num_literals = Integer.parseInt(argv[2]);
	    time_limit = Integer.parseInt(argv[3]);
	    num_reps = Integer.parseInt(argv[4]);
	}
	catch (Exception e) {
	    System.err.println("Arguments: <num_clauses> <num_symbols> <num_literals_per_clause> <time_limit> <num_reps>");
	    return;
	}

	Generator gen = new RandomCnfGenerator(num_clauses,
					       num_symbols,
					       num_literals);
	SatSolver sat = new MySatSolver();

	Cnf.runSat(gen, sat, num_reps, time_limit);
    }
}
