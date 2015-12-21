import java.util.*;

/** This class provides a representation of a CNF, including methods
 *  for constructing the CNF, for converting it into an array of arrays
 *  of <tt>Literal</tt>s, and for printing.  The class also includes a
 *  number of static methods which may be useful in processing CNF
 *  formulas.
 *  <p>
 *  In most cases, a complete CNF can be created using the constructor
 *  (which constructs an empty CNF), and the methods
 *  <tt>addClause()</tt> (which adds a clause to the CNF), and
 *  <tt>addLiteral()</tt> (which adds a literal to one of the clauses,
 *  by default, the last clause).  Other methods are provided for
 *  accessing or setting individual literals of individual clauses,
 *  and for removing literals or entire clauses.
 *  <p>
 *  Each clause has an index as in an array, the first having index 0,
 *  the next with index 1, etc.  Similarly, within each clause, every
 *  literal has an index according to the order in which it was added.
 *  Clauses and literals can be accessed using these indices.  In
 *  addition, the method <tt>toLiteralArray()</tt> returns an array of
 *  arrays of <tt>Literal</tt>s containing all of the elements of the
 *  CNF in the correct order according to this indexing.
 *  <p>
 *  A literal is generally specified by a sign and a symbol.  The sign
 *  is represented by a boolean which is <tt>false</tt> if the literal
 *  is negated, and <tt>true</tt> otherwise.  Each symbol is given by
 *  an arbitrary <tt>String</tt>, and two such symbols are considered
 *  the same if and only if the corresponding <tt>String</tt>s are
 *  equal.  However, the components of the array of arrays returned by
 *  <tt>toLiteralArray()</tt> represent each symbol by its index,
 *  i.e., by an integer between 0 and <tt>getNumSymbols()</tt> - 1.
 *  The <tt>String</tt> corresponding to such an index can be accessed
 *  using the <tt>getSymbolName()</tt> method.
 * */

public class Cnf {

    /** This is the constructor for the class.  It simply creates an
     *  empty CNF.
     */
    public Cnf() {
	clause_list = new Vector<Vector<Literal>>();
	symbol_names = new Vector<String>();
	symbol_table = new HashMap<String, Integer>();
    }

    /** Adds a new clause to this CNF.
     */
    public void addClause() {
	clause_list.add(new Vector<Literal>());
    }

    /** Adds a new literal to the last clause that was added to the CNF.
     *	@param symbol the symbol of the new literal, as a <tt>String</tt>
     *	@param sign   the sign of the new literal
     *	@throws NoSuchElementException if CNF is empty
     */
    public void addLiteral(String symbol,
			   boolean sign) {

	clause_list.lastElement().add(new Literal(lookUpSymbol(symbol),
						  sign));
    }

    /** Adds a new literal to a specified clause of this CNF.
     *	@param clause index of clause to which literal is to be added
     *	@param symbol the symbol of the new literal, as a <tt>String</tt>
     *	@param sign   the sign of the new literal
     *	@throws ArrayIndexOutOfBoundsException if clause index is out of range
     */
    public void addLiteral(int clause,
			   String symbol,
			   boolean sign) {

	clause_list.get(clause).add(new Literal(lookUpSymbol(symbol),
						sign));
    }

    /** Returns the number of unique symbols appearing in literals
     *  that have been added to this CNF.
     */
    public int getNumSymbols() {
	return symbol_names.size();
    }

    /** Returns the number of clauses in this CNF.
     */
    public int getNumClauses() {
	return clause_list.size();
    }

    /** Returns the number of literals in the specified clause of this CNF.
     *	@param clause index of specified clause
     *	@throws ArrayIndexOutOfBoundsException if clause index is out of range
     */
    public int getClauseLength(int clause) {
	return clause_list.get(clause).size();
    }

    /** Returns the symbol for the literal in the specified position
     *  of this CNF.
     *	@param clause index of specified clause
     *	@param index index of specified literal within that clause
     *	@throws ArrayIndexOutOfBoundsException if either index is out of range
     */
    public String getSymbol(int clause, int index) {
	return symbol_names.get(clause_list.get(clause).get(index).symbol);
    }

    /** Returns the sign for the literal in the specified position
     *  of this CNF.
     *	@param clause index of specified clause
     *	@param index index of specified literal within that clause
     *	@throws ArrayIndexOutOfBoundsException if either index is out of range
     */
    public boolean getSign(int clause, int index) {
	return clause_list.get(clause).get(index).sign;
    }

    /** Replaces the literal in the specified position of this CNF.
     *	@param clause index of specified clause
     *	@param index index of specified literal within that clause
     *	@param symbol the symbol of the new literal, as a <tt>String</tt>
     *	@param sign   the sign of the new literal
     *	@throws ArrayIndexOutOfBoundsException if either index is out of range
     */
    public void setLiteral(int clause, int index,
			   String symbol, boolean sign) {
	clause_list.get(clause).set(index, new Literal(lookUpSymbol(symbol),
						       sign));
    }

    /** Removes the entire specified clause from this CNF.  Shifts any
     *	subsequent clauses to the left (subtracts one from their
     *	indices).
     *	@param clause index of specified clause
     *	@throws ArrayIndexOutOfBoundsException if clause index is out of range
     */
    public void removeClause(int clause) {
	clause_list.remove(clause);
    }

    /** Removes the specified literal from the specified clause from
     *	this CNF.  Shifts any subsequent literals of the clause to the
     *	left (subtracts one from their indices).
     *	@param clause index of specified clause
     *	@throws ArrayIndexOutOfBoundsException if either index is out of range
     */
    public void removeLiteral(int clause, int index) {
	clause_list.get(clause).remove(index);
    }

    /** Returns the name (as a <tt>String</tt>) of the symbol with the
     *  given index.
     *  @param index index of specified symbol
     *	@throws IndexOutOfBoundsException if index is out of range
     */
    public String getSymbolName(int index) {
	return symbol_names.get(index);
    }	

    /** Returns this CNF represented as an array of arrays of
     <tt>Literal</tt>s.
    */
    public Literal[][] toLiteralArray() {
	int num_clauses = clause_list.size();
	Literal[][] lit = new Literal[num_clauses][];
	for (int c = 0; c < num_clauses; c++) {
	    Vector<Literal> cl = clause_list.get(c);
	    int len = cl.size();
	    lit[c] = new Literal[len];
	    for (int i = 0; i < len; i++) {
		Literal l = cl.get(i);
		lit[c][i] = new Literal(l.symbol, l.sign);
	    }
	}
	return lit;
    }

    /** Prints this CNF to standard output. */
    public void print() {
	int num_clauses = clause_list.size();
	for (int c = 0; c < num_clauses; c++) {
	    System.out.println("Clause " + c + ":");
	    Vector<Literal> cl = clause_list.get(c);
	    int len = cl.size();
	    for (int i = 0; i < len; i++) {
		Literal l = cl.get(i);
		System.out.println("   " + (l.sign ? "+" : "-")
				   + " " + symbol_names.get(l.symbol));
	    }
	    System.out.println();
	}
    }

    /** Prints a given model to standard output.  The given model is a
     *  boolean array representing a truth assignment to each of the
     *  symbols, and thus must have length equal to
     *  <tt>getNumSymbols()</tt>.
     */
    public void printModel(boolean[] model) {
	for (int i = 0; i < model.length; i++) {
	    System.out.println(" " + (model[i] ? "+" : "-")
			       + " " + symbol_names.get(i));
	}
    }

    /** Repeatedly calls a given sat-solver on CNF's generated by the
     *  given generator.  The process is repeated <tt>num_reps</tt>
     *  times.  Each call to the sat-solver is limited to
     * <tt>time_limit</tt> seconds.  */
    public static void runSat(Generator generator,
			      SatSolver sat_solver,
			      int num_reps,
			      int time_limit) {

	System.out.println("Generator:");
	System.out.println(generator.description());
	System.out.println("by: " + generator.author());
	System.out.println();
	System.out.println("SatSolver:");
	System.out.println(sat_solver.description());
	System.out.println("by: " + sat_solver.author());

	for (int r = 0; r < num_reps; r++) {
	    Cnf cnf = generator.getNext();
	    System.out.println("--------------------------");
	    System.out.println("CNF:");
	    cnf.print();
	    Literal[][] cnf_as_array = cnf.toLiteralArray();
	    Timer timer = new Timer(time_limit * 1000);
	    boolean[] model = sat_solver.solve(cnf_as_array,
					       cnf.getNumSymbols(),
					       timer);
	    System.out.println("elapsed time= " + timer.getTimeElapsed());
	    int num_unsat = numClauseUnsat(cnf_as_array, model);
	    System.out.println("# unsatisfied clauses= " + num_unsat);
	    System.out.println("model: ");
	    cnf.printModel(model);
	}
    }

    /** Returns <tt>true</tt> if the given model satisfies the given
     *  clause.  The given <tt>model</tt> is represented as a boolean
     *  array representing a truth assignment to a set of symbols.
     *  The given <tt>clause</tt> is an array of <tt>Literal</tt>s
     *  over the same set of symbols.
     */
    public static boolean isClauseSat(Literal[] clause, boolean[] model) {
	for (int j = 0; j < clause.length; j++)
	    if (model[clause[j].symbol] == clause[j].sign)
		return true;
	return false;
    }

    /** Returns the number of clauses of the given CNF not satisfied
     *  by the given model.  The given <tt>model</tt> is represented
     *  as a boolean array representing a truth assignment to a set of
     *  symbols.  The given <tt>cnf</tt> is an array of arrays of
     *  <tt>Literal</tt>s over the same set of symbols.
     */
    public static int numClauseUnsat(Literal[][] cnf, boolean[] model) {
	int num_not_satd = 0;

	for (int i = 0; i < cnf.length; i++)
	    if (!isClauseSat(cnf[i], model))
		num_not_satd++;

	return num_not_satd;
    }

    // ------------------- private stuff ----------------------- //

    private int lookUpSymbol(String symbol) {
	if (!symbol_table.containsKey(symbol)) {
	    int sym_idx = symbol_names.size();
	    symbol_names.add(symbol);
	    symbol_table.put(symbol, new Integer(sym_idx));
	}
	return symbol_table.get(symbol).intValue();
    }

    private Vector<Vector<Literal>> clause_list;
    private List<String> symbol_names;
    private Map<String, Integer> symbol_table;

}
