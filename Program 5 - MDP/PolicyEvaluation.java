/**********************************************************************************/
// Edited By: Brandon Zhou
// netID: brandonz
// Course: COS 402
/**********************************************************************************/

/**
 * This is the template of a class that evaluates a given policy,
 * i.e., computes the utility of each state when actions are chosen
 * according to it.  The utility is returned in the public
 * <tt>utility</tt> field.  You need to fill in the constructor.  You
 * may wish to add other fields with other useful information that you
 * want this class to return (for instance, number of iterations
 * before convergence).  You also may add other constructors or
 * methods, provided that these are in addition to the one given below
 * (which is the one that will be automatically tested).  In
 * particular, your code must work properly when run with the
 * <tt>main</tt> provided in <tt>RunCatMouse.java</tt>.
 */
public class PolicyEvaluation {

    private static final double TOLERANCE = 1.0E-8;

    /** the computed utility of each state under the given policy */
    public double utility[];
    public int iterations;

    /**
     * The constructor for this class.  Computes the utility of policy
     * <tt>pi</tt> for the given <tt>mdp</tt> with given
     * <tt>discount</tt> factor, and stores the answer in
     * <tt>utility</tt>.
     */
    public PolicyEvaluation(Mdp mdp, double discount, int pi[]) {

	// your code here
        iterations = 0;
        utility = new double[mdp.numStates];
        double[] prevUtil = new double[mdp.numStates];
        double delta = 0.0;

        do {
            iterations++;
            delta = 0.0;

            double[] temp = utility;
            utility = prevUtil;
            prevUtil = temp;

            // iterate over all states
            for (int s = 0; s < mdp.numStates; s++) {
                int policy = pi[s];
                double total = 0.0;

                // iterate over all transition states
                for (int i = 0; i < mdp.nextState[s][policy].length; i++)
                    total += mdp.transProb[s][policy][i]*prevUtil[mdp.nextState[s][policy][i]];

                // update utility
                utility[s] = mdp.reward[s] + (discount*total);
                if (Math.abs(utility[s] - prevUtil[s]) > delta)
                    delta = Math.abs(utility[s] - prevUtil[s]);
            }

        } while(delta > TOLERANCE*(1-discount)/discount);

    }

}
