/**********************************************************************************/
// Edited By: Brandon Zhou
// netID: brandonz
// Course: COS 402
/**********************************************************************************/

/** This is a template for an HMM class.  Fill in code for the
 * constructor and all of the methods.  Do not change the signature of
 * any of these, and do not add any other public fields, methods or
 * constructors (but of course it is okay to add private stuff).  All
 * public access to this class must be via the constructor and methods
 * specified here.
 */
public class Hmm {

    private int numStates;
    private int numOutputs;
    private double[][] transition;
    private double[][] output;
    private int dummy;

    /** Constructs an HMM from the given data.  The HMM will have
     * <tt>numStates</tt> possible states and <tt>numOutputs</tt>
     * possible outputs.  The HMM is then built from the given set of
     * state and output sequences.  In particular,
     * <tt>state[i][j]</tt> is the <tt>j</tt>-th element of the
     * <tt>i</tt>-th state sequence, and similarly for
     * <tt>output[i][j]</tt>.
     */
    public Hmm(int numStates, int numOutputs,
	       int state[][], int output[][]) {

	// your code here
        this.numStates = numStates;
        this.numOutputs = numOutputs;
        dummy = numStates;
        transition = new double[numStates + 1][numStates];
        this.output = new double[numStates][numOutputs];
    
        // increment every item in the transition/output matrices to smooth
        for (int i = 0; i < numStates; i ++) {
            for (int j = 0; j < numStates; j++)
                transition[i][j]++;
            for (int j = 0; j < numOutputs; j++)
                this.output[i][j]++;
        }
        for (int j = 0; j < numStates; j++)
            transition[dummy][j]++;

        // formulate the transition matrix from the training data
        for (int i = 0; i < state.length; i++) {
            int n = state[i].length;

            // get previous state to avoid unnecessary array access
            int prev = state[i][0];
            int next;

            // increment the dummy start
            transition[dummy][prev]++;

            // increment transition matrix from sample
            for (int j = 1; j < n; j++) {
                next = state[i][j];
                transition[prev][next]++;
                prev = next;
            }
        }

        // formulate output matrix from the training data
        for (int i = 0; i < output.length; i++)
            for (int j = 0; j < output[i].length; j++)
                this.output[state[i][j]][output[i][j]]++;

        // normalize
        for (int i = 0; i < numStates; i++) {
            transition[i] = normalize(transition[i]);
            this.output[i] = normalize(this.output[i]);
        }
        transition[dummy] = normalize(transition[dummy]);
    }

    /** Returns the number of states in this HMM. */
    public int getNumStates() {
	// your code here
        return numStates;
    }

    /** Returns the number of output symbols for this HMM. */
    public int getNumOutputs() {
	// your code here
        return numOutputs;
    }

    /** Returns the log probability assigned by this HMM to a
     * transition from the dummy start state to the given
     * <tt>state</tt>.
     */
    public double getLogStartProb(int state) {
	// your code here
        return Math.log(transition[dummy][state]);
    }

    /** Returns the log probability assigned by this HMM to a
     * transition from <tt>fromState</tt> to <tt>toState</tt>.
     */
    public double getLogTransProb(int fromState, int toState) {
	// your code here
        return Math.log(transition[fromState][toState]);
    }

    /** Returns the log probability of <tt>state</tt> emitting
     * <tt>output</tt>.
     */
    public double getLogOutputProb(int state, int output) {
	// your code here
        return Math.log(this.output[state][output]);
    }

    // normalizes an input double[]
    private double[] normalize(double[] vector) {
        double[] output = vector.clone();
        double tot = 0.0;

        for (double i : output)
            tot += i;
        for (int i = 0; i < output.length; i++)
            output[i] /= tot;

        return output;
    }

}
