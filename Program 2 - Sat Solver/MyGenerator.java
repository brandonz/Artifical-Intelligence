/**********************************************************************************/
// Edited By: Brandon Zhou
// netID: brandonz
// Course: COS 402
/**********************************************************************************/

/** This is a template of the <tt>MyGenerator</tt> class that must be
 * turned in.  See <tt>RandomCnfGenerator</tt> or
 * <tt>RandomSatisfiedCnfGenerator</tt> for sample code, and see
 * <tt>Generator</tt> for further explanation.
 */
import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;

public class MyGenerator implements Generator {

    private static final int MAX_SEATS = 50;
    private static final int MIN_SEATS = 4;
    private static final boolean MIN_MODELS = false;
    private Random rand;

    /** A constructor for this class.  You must have a constructor
     * taking no arguments.  (You may have other constructors that you
     * use for your experiments, but this is the constructor that will
     * be used as part of the class implementation challenge.)
     */
    public MyGenerator() {
	// fill in initialization code here
        rand = new Random();
    }

    /** This is the method that generates CNF formulas, represented by
     * <tt>Cnf</tt> objects, each time it is called.
     */
    public Cnf getNext() {
	// fill in generation code here

        // a table seats the same number of people on each side
        int numSeats = rand.nextInt(MAX_SEATS - MIN_SEATS + 1);
        numSeats += MIN_SEATS;
        if (numSeats % 2 != 0)
            numSeats++;

        // number of guests on each side
        int mid = numSeats/2;
        System.out.println(numSeats);

        Cnf cnf = new Cnf();
    
        // each seat must have exactly one person
        // the first number of a symbol is the chair num
        // the second number is the guest num
        for (int i = 0; i < numSeats; i++) {
            // each node seats one person
            // (01 v 11 v 12 v 13...)
            cnf.addClause();
            for (int j = 0; j < numSeats; j++) {
                String literal = new StringBuilder().append(i).append(j).toString();
                cnf.addLiteral(literal, true);
            }

            // each node seats only one person
            // (!11 v !12) ^ (!11 v !13) ^ ...
            for (int j = 0; j < numSeats-1; j++) {
                for (int k = j + 1; k < numSeats; k++) {
                    cnf.addClause();
                    String literal1 = new StringBuilder().append(i).append(j).toString();
                    String literal2 = new StringBuilder().append(i).append(k).toString();
                    cnf.addLiteral(literal1, false);
                    cnf.addLiteral(literal2, false);
                }
            }
        }

        // instead of generating who must sit together and who must not sit together randomly,
        // we create a satisfiable solution and use the solution to create the pairings, using an int[]
        int[] table = new int[numSeats];
        Arrays.fill(table, -1);
        // fill the table with a possible seating/model
        for (int i = 0; i < numSeats; i++) {
            int index = rand.nextInt(numSeats);
            while (table[index] != -1)
                index = ++index % numSeats;
            table[index] = i;
        }

        ArrayList<Pair> complementaryPairs = new ArrayList<Pair>();
        ArrayList<Pair> opposingPairs = new ArrayList<Pair>();

        int numComplementaryPairs, numOpposingPairs;
        // generate the maximum number of pairs possible to reduce satisfiable models
        if (MIN_MODELS) {
            numComplementaryPairs = mid - 2;
            numOpposingPairs = (((mid - 2)*(mid - 1))/2) + 1;
        }
        // generate a random number of complementary/pairs
        // the range is from 0 - max possible num of pairs
        else {
            numComplementaryPairs = rand.nextInt(mid - 1);
            numOpposingPairs = rand.nextInt((((mid - 2)*(mid - 1))/2) + 2);
        }

        // add complementary pairs
        int counter = 0;
        for (int i = 0; i < numComplementaryPairs; i++) {
            if (counter == mid - 1)
                counter++;
            if (counter + 1 >= numSeats)
                counter = 1;
            if (counter == mid && i > mid)
                counter++;
            Pair pair = new Pair(table[counter], table[counter + 1]);
            complementaryPairs.add(pair);
            counter += 2;
        }

        // add opposing pairs
        counter = 0;
        int offset = 2;
        // account for the end of table
        if (numOpposingPairs == (((mid - 2)*(mid - 1))/2) + 1) {
            numOpposingPairs--;
            opposingPairs.add(new Pair(table[mid-1], table[numSeats-1]));
        }
        for (int i = 0; i < numOpposingPairs; i++) {
            if (counter + offset == numSeats) {
                counter = 0;
                offset++;
            }

            Pair pair;
            if (counter == mid - 1)
                pair = new Pair(table[counter], table[counter + offset - 1]);
            else
                pair = new Pair(table[counter], table[counter + offset]);
            opposingPairs.add(pair);
            counter++;
        }

        // and adding opposing/complementary pairs to cnf accordingly
        addCnfPairs(cnf, complementaryPairs, numSeats, mid, true);
        addCnfPairs(cnf, opposingPairs, numSeats, mid, false);
        
        return cnf;
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
     * description (appropriate for posting on the class website) of
     * how the CNF formulas are generated by this generator.
     */
    public String description() {
	// fill in description code here
        return "This generates a dinner party planning problem.";
    }

    /** This is a simple <tt>main</tt>.  It generates a CNF using
     * <tt>MyGenerator</tt>.  Once generated, <tt>MySatSolver</tt> is
     * called to try to solve the CNF within the time limit specified
     * by the first argument.  This process is repeated the number of
     * times specified by the second argument.
     */
    public static void main(String[] argv) {
	int time_limit = 0;
	int num_reps = 0;
	try {
	    time_limit = Integer.parseInt(argv[0]);
	    num_reps = Integer.parseInt(argv[1]);
	}
	catch (Exception e) {
	    System.err.println("Arguments: <time_limit> <num_reps>");
	    return;
	}

	Generator gen = new MyGenerator();
	SatSolver sat = new MySatSolver();

	Cnf.runSat(gen, sat, num_reps, time_limit);
    }



    //---------HELPER METHODS---------

    // helper class to hold a pairing for two guests
    // can either be that these two must sit together or must NOT sit together
    private class Pair {
        public int x;
        public int y;
        public Pair(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    // adds the pairs to cnf, if complentary is true, then the pairs must sit together
    // if false the pairs must NOT sit together
    private void addCnfPairs(Cnf cnf, ArrayList<Pair> pairs, int numSeats, int mid, boolean complementary) {
        // for each node pairs on the first side of the table
        for (int i = 0; i < numSeats - 1; i++) {
            // once one side of the table is complete, skip to the other side
            // if the table is size 8, seats 3 and 4 are NOT next to each other
            // seat 3 is on one side and seat 4 is on the other
            if (i == mid - 1)
                continue;

            // if person 4 and person 18 must sit together, then for seats 0, 1
            // 04 <=> 118, in cnf: (!04 v 118) ^ (!118 v 04)
            // this means if person 1 sits in seat 1, person 2 must be in seat 2 and vice-versa

            // if person 4 and person 18 must NOT sit together, then for seats 0, 1
            // (!04 v !118) ^ (!018 v !14)

            // in both cases the first literal of each clause is negated, the second is negative if 
            // the pair are opposing pairs
            for (Pair pair : pairs) {
                cnf.addClause();
                String literal1 = new StringBuilder().append(i).append(pair.x).toString();
                String literal2 = new StringBuilder().append(i+1).append(pair.y).toString();
                cnf.addLiteral(literal1, false);
                cnf.addLiteral(literal2, complementary);

                cnf.addClause();
                String literal3 = new StringBuilder().append(i).append(pair.y).toString();
                String literal4 = new StringBuilder().append(i+1).append(pair.x).toString();
                cnf.addLiteral(literal3, false);
                cnf.addLiteral(literal4, complementary);
            }
        }
    }
}
