/** This is the interface defining a <tt>SatSolver</tt> object.  Every
 * <tt>SatSolver</tt> must include a method <tt>solve</tt> for solving
 * CNF satisfiability problems.  In addition, it must include a method
 * returning the "author" of the program, and another method returning
 * a brief description of the algorithm used.
 */
public interface SatSolver {

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
    public boolean[] solve(Literal[][] cnf, int num_symbols, Timer timer);


    /** This method should simply return the "author" of this program
     * as you would like it to appear on a class website.  You can use
     * your real name or a pseudonym of your choice.
     */
    public String author();


    /** This method should return a very brief (1-3 sentence)
     * description of the algorithm and implementational improvements
     * that are being used, appropriate for posting on the class
     * website.
     */
    public String description();

}
