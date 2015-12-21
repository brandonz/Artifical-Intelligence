/**********************************************************************************/
// Edited By: Brandon Zhou
// netID: brandonz
// Course: COS 402
/**********************************************************************************/

/** This is a template of the <tt>MySatSolver</tt> class that must be
 * turned in.  See <tt>RandomSearchSatSolver</tt> for sample code, and
 * see <tt>SatSolver</tt> for further explanation.
 */
import java.util.Random;
import java.util.ArrayList;

public class MySatSolver implements SatSolver {

    private static final double RANDOM_WALK_P = 0.5;
    private Random rand;

    /** A constructor for this class.  You must include a constructor
     * such as this one taking no arguments.  (You may have other
     * constructors that you use for your experiments, but this is the
     * constructor that will be used as part of the class
     * implementation challenge.)
     */
    public MySatSolver() {
	// fill in initialization code here
        rand = new Random();
    }

    /** This is the method for solving satifiability problems.  Each
     * <tt>Literal</tt> of the given <tt>cnf</tt> must include symbols
     * indexed by integers between 0 and <tt>num_symbols</tt>-1.  The
     * method should return a solution in the form of a boolean array
     * of length <tt>num_symbols</tt> representing an assignment to
     * all the symbols of the given CNF that satisfies as many clauses
     * as possible by when time runs out on the given <tt>timer</tt>
     * object (or very soon thereafter).
     * @param cnf the given cnf, represented as an array of arrays of <tt>Literal</tt>s
     * @param num_symbols the number of distinct symbols in the cnf
     * @param timer the given timer object */
    public boolean[] solve(Literal[][] cnf, int num_symbols, Timer timer) {
	// fill in sat-solver code here

        // initialize a random model
        boolean[] model = new boolean[num_symbols];
        for (int i = 0; i < model.length; i++)
            model[i] = rand.nextBoolean();

        // iterate over cnf
        for (int i = 0; i < cnf.length; i ++) {
            Literal[] clause = cnf[i];

            // if the clause has one literal, it must be set to true
            if (clause.length == 1)
                model[clause[0].symbol] = clause[0].sign;
        }

        // in case of unsatisfiable local mins, force random flip
        boolean randomFlip = false;

        while (timer.getTimeRemaining() > 0) {

            // list of unsatisfiable clauses 
            ArrayList<Integer> clauses = unsatisfiableClauses(cnf, model);

            // if model satisfies clauses then return model
            if (clauses.size() == 0)
                return model;

            // get random clause that is unsatisfiable
            int randomClause = clauses.get(rand.nextInt(clauses.size()));

            // with probability p flip a random symbol in randomClause
            if (randomFlip || rand.nextDouble() < RANDOM_WALK_P) {
                randomFlip = false;
                int randomLiteral = rand.nextInt(cnf[randomClause].length);
                int randomSymbol = cnf[randomClause][randomLiteral].symbol;
                model[randomSymbol] = !model[randomSymbol];
            }
            // flip the symbol in randomClause that maximizes num of satisfied clauses
            else {
                // store best symbol with associated num
                Pair best = new Pair(0, 0);
                int max = best.made - best.broke;
                int symbol = -1;

                // for each symbol
                for (int i = 0; i < cnf[randomClause].length; i++) {
                    int tempSymbol = cnf[randomClause][i].symbol;

                    // // flip symbol, find num of satisfied clauses, restore symbol
                    // int oldSatisfied = numSatisfiedClauses(cnf, model);
                    // model[tempSymbol] = !model[tempSymbol];
                    // int satisfied = numSatisfiedClauses(cnf, model);
                    // model[tempSymbol] = !model[tempSymbol];

                    Pair changes = flipChanges(cnf, model, tempSymbol);

                    // if the flip was strictly better, accept it
                    if (changes.broke == 0 && changes.made > 0) {
                        model[tempSymbol] = !model[tempSymbol];
                        continue;
                    }

                    // if a new best is found
                    int change = changes.made - changes.broke;
                    if (change > max) {
                        max = change;
                        best = changes;
                        symbol = tempSymbol;
                    }
                    // in case of tie choose the symbol that broke less
                    if (change == max && changes.broke < best.broke) {
                        max = change;
                        best = changes;
                        symbol = tempSymbol;
                    }
                }

                // flip the best symbol
                if (symbol != -1)
                    model[symbol] = !model[symbol];
                // else a local min is found, flip a random symbol next iteration
                else
                    randomFlip = true;
            }
        }
        return model;
    }

    /** This method should simply return the "author" of this program
     * as you would like it to appear on a class website.  You can use
     * your real name or a pseudonym of your choice.
     */
    public String author() {
	// fill in author code here
        return "Half a Byte is Called a Nibble";
    }

    /** This method should return a very brief (1-3 sentence)
     * description of the algorithm and implementational improvements
     * that are being used, appropriate for posting on the class
     * website.
     */
    public String description() {
	// fill in description code here
        String description = "This SAT Solver is an extension of WalkSat.";
        return description;
    }



    //---------HELPER METHODS---------

    // helper class to coalesce changes made by a flip
    private class Pair {
        public int made;
        public int broke;
        public Pair(int made, int broke) {
            this.made = made;
            this.broke = broke;
        }
    }

    // returns a pair that holds the number of clauses that were broken/made
    // as a result of the symbol flip
    private Pair flipChanges(Literal[][] cnf, boolean[] model, int symbol) {
        int made = 0;
        int broke = 0;
        for (int i = 0; i < cnf.length; i++) {
            Literal[] clause = cnf[i];
            boolean sat = satisfiableClause(clause, model);

            model[symbol] = !model[symbol];
            // if the flip broke the clause
            if (sat && !satisfiableClause(clause, model))
                broke++;
            // if the flip made the clause
            if (!sat && satisfiableClause(clause, model))
                made++;
            model[symbol] = !model[symbol];
        }

        return new Pair(made, broke);
    }

    // return an ArrayList<Integer> that contains the indexes of unsatisfiable clauses
    private ArrayList<Integer> unsatisfiableClauses(Literal[][] cnf, boolean[] model) {
        ArrayList<Integer> unsatisfiableClauses = new ArrayList<Integer>();
        for (int i = 0; i < cnf.length; i++) {
             if (!satisfiableClause(cnf[i], model))
                unsatisfiableClauses.add(i);
        }
        return unsatisfiableClauses;
    }

    // returns the number of satisfied clauses
    private int numSatisfiedClauses(Literal[][] cnf, boolean[] model) {
        int result = 0;
        for (int i = 0; i < cnf.length; i++) {
            if (satisfiableClause(cnf[i], model))
                result++;
        }
        return result;
    }

    // returns true if the clause is satisfiable with the model
    private boolean satisfiableClause(Literal[] clause, boolean[] model) {
        for (int i = 0; i < clause.length; i++) {
            if (clause[i].sign == model[clause[i].symbol])
                return true;
        }
        return false;
    }

}
		

