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
        int[] path = new int[output.length];
        double[] previous = new double[numStates];
        double[] current = new double[numStates];
        // viterbi calculations
        double max = Double.NEGATIVE_INFINITY;      // running max prob for each state
        double stepMax = Double.NEGATIVE_INFINITY;  // running max prob for each step
        int val = -1;                               // state associated with running max prob

        // set the initial vector for step 0
        for (int i = 0; i < numStates; i++) {
            double prob = hmm.getLogStartProb(i) + hmm.getLogOutputProb(i, output[0]);
            previous[i] = prob;
            if (stepMax < prob) {
                stepMax = prob;
                val = i;
            }
        }
        path[0] = val;
        System.out.println(stepMax);

        // calculate the probabilities of the states at each step and save he most probable state
        for (int i = 1; i < output.length; i++) {
            stepMax = Double.NEGATIVE_INFINITY;

            // get max of probabilities (min log)
            for (int state = 0; state < numStates; state++) {
                max = Double.NEGATIVE_INFINITY;
                // for each state in the previous step
                for (int prevState = 0; prevState < numStates; prevState++) {

                    // calculate probability and save the max
                    double prob = previous[prevState] + hmm.getLogTransProb(prevState, state) + hmm.getLogOutputProb(state, output[i]);
                    if (prob > max)
                        max = prob;
                }
                // save the probability of each state for calculations of the next step
                current[state] = max;
                if (max > stepMax) {
                    val = state;
                    stepMax = max;
                }
            }

            // move the backward pointer forward
            // swatch previous/current because the old previous can be overwritten
            double[] temp = previous;
            previous = current;
            current = temp;

            // save the most likely first state
            path[i] = val;
        }

        return path;
    }

}
