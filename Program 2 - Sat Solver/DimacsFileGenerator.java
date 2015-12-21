import java.io.*;

/** A <tt>Generator</tt> that simply reads a file representing a CNF
 *  in the style used in the <a href="ftp://dimacs.rutgers.edu/pub/challenge/satisfiability/benchmarks/cnf/">DIMACS implementation challenge</a>.
 *  (Note that these files must be uncompressed before using here.)
 *  Each time <tt>getNext</tt> is called, this same CNF is returned.
 *  This class also includes a <tt>main</tt> method for testing.
 */
public class DimacsFileGenerator implements Generator {

    Cnf cnf;
    String file_name;

    /** Constructor for this generator.
     * @param file_name name of file containing DIMACS CNF description
     */
    public DimacsFileGenerator(String file_name)
	throws FileNotFoundException, IOException {
	BufferedReader in;
	String line;

	this.file_name = file_name;

	try {
	    in = new BufferedReader(new FileReader(file_name));
	}
	catch (FileNotFoundException e) {
	    System.err.print("File "+file_name+" not found.\n");
	    throw e;
	}

	boolean new_clause = false;

	while(true) {
	    try {
		line = in.readLine();
	    } catch (IOException e) {
		System.err.println("Error reading file "+file_name);
		throw e;
	    }

	    if (line == null)
		break;

	    line = line.trim( );

	    String[] words = line.split("\\s+");
      
	    if (words[0].equals("c"))
		continue;

	    if (words[0].equals("p")) {
		if (!words[1].equals("cnf"))
		    System.err.println("expect cnf after p in file " + file_name);

		cnf = new Cnf();
		new_clause = true;
	    } else if (!line.equals("")) {
		for (int j = 0; j < words.length; j++) {
		    int var = Integer.parseInt(words[j]);
		    if (var == 0) {
			new_clause = true;
		    } else{
			if (new_clause) {
			    new_clause = false;
			    cnf.addClause();
			}
			cnf.addLiteral("X" + (var < 0 ? -var : var),
				       (var > 0));
		    }
		}
	    }
	}
    }

    /** Returns (a clone of) the CNF read in from the named DIMACS file. */
    public Cnf getNext() {
	Cnf new_cnf = new Cnf();
	for (int i = 0; i < cnf.getNumClauses(); i++) {
	    new_cnf.addClause();
	    for (int j = 0; j < cnf.getClauseLength(i); j++) {
		new_cnf.addLiteral(cnf.getSymbol(i, j),
				   cnf.getSign(i, j));
	    }
	}
	return new_cnf;
    }

    /** The author of this generator. */
    public String author() {
	return "Rob Schapire";
    }

    /** A brief description of this generator. */
    public String description() {
	return "Reads the file " + file_name + " representing a CNF " +
	    "in the style used in the DIMACS implementation challenge, " +
	    "and returns this same CNF each time called.";
    }

    /** This is a simple <tt>main</tt> which runs <tt>MySatSolver</tt>
     * on the CNF stored in the file named in the first argument
     * within the time limit specified by the second argument.
     */
    public static void main(String[] argv)
	throws FileNotFoundException, IOException {
	String file_name = "";
	int time_limit = 0;
	try {
	    file_name = argv[0];
	    time_limit = Integer.parseInt(argv[1]);
	} catch (Exception e) {
	    System.err.println("Arguments: <file_name> <time_limit>");
	    return;
	}

	Generator gen = new DimacsFileGenerator(file_name);
	SatSolver sat = new MySatSolver();

	Cnf.runSat(gen, sat, 1, time_limit);
    }
}

