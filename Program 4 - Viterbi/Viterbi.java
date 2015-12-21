/**********************************************************************************/
// Edited By: Brandon Zhou
// netID: brandonz
// Course: COS 402
/**********************************************************************************/

/** This is a template for a Viterbi class, which can be used to
 * compute most likely sequences.  Fill in code for the constructor
 * and <tt>mostLikelySequence</tt> method.
 */
public class Viterbi {

    private Hmm hmm;

    /** This is the constructor for this class, which takes as input a
     * given HMM with respect to which most likely sequences will be
     * computed.
     */
    public Viterbi(Hmm hmm) {

    // your code here
        this.hmm = hmm;
    }

    /** Returns the most likely state sequence for the given
     * <tt>output</tt> sequence, i.e., the state sequence of highest
     * conditional probability given the output sequence, according to
     * the HMM that was provided to the constructor.  The returned
     * state sequence should have the same number of elements as the
     * given output sequence.
     */
    public int[] mostLikelySequence(int output[]) {

    // your code here
        int numStates = hmm.getNumStates();
        int[] path = new int[output.length];                // most likely path
        double[] previous = new double[numStates];          // previous step prob
        double[] current = new double[numStates];           // current step prob
        int[][] back = new int[output.length][numStates];   // backward chain

        // viterbi calculations
        double max = Double.NEGATIVE_INFINITY;  // running max prob for each state
        int backVal = -1;                       // running prevState to current state

        // set the initial vector for step 0
        for (int i = 0; i < numStates; i++)
            previous[i] = hmm.getLogStartProb(i) + hmm.getLogOutputProb(i, output[0]);

        // calculate the probabilities of the states at each step
        // save he most probable state
        for (int i = 1; i < output.length; i++) {

            // get max of probabilities
            for (int state = 0; state < numStates; state++) {
                max = Double.NEGATIVE_INFINITY;
                backVal = -1;

                // for each state in the previous step
                for (int prevState = 0; prevState < numStates; prevState++) {
                    // calculate probability and save the max
                    double prob = previous[prevState] + hmm.getLogTransProb(prevState, state)
                     + hmm.getLogOutputProb(state, output[i]);
                    if (prob > max) {
                        max = prob;
                        backVal = prevState;
                    }
                }
                // save the probability and state to get there
                current[state] = max;
                back[i][state] = backVal;
            }

            // move the backward pointer forward
            // swatch previous/current because the old previous can be overwritten
            double[] temp = previous;
            previous = current;
            current = temp;
        }

        // find the end of the most probable path
        max = Double.NEGATIVE_INFINITY;
        backVal = -1;
        for (int i = 0; i < numStates; i++) {
            if (previous[i] > max) {
                max = previous[i];
                backVal = i;
            }
        }
        path[output.length - 1] = backVal;

        // follow it back
        for (int i = path.length - 2; i >= 0; i--)
            path[i] = back[i+1][path[i+1]];

        return path;
    }

}
