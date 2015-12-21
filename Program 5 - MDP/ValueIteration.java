/**********************************************************************************/
// Edited By: Brandon Zhou
// netID: brandonz
// Course: COS 402
/**********************************************************************************/

/**
 * This is the template of a class that should run value iteration on
 * a given MDP to compute the optimal policy which is returned in the
 * public <tt>policy</tt> field.  The computed optimal utility is also
 * returned in the public <tt>utility</tt> field.  You need to fill in
 * the constructor.  You may wish to add other fields with other
 * useful information that you want this class to return (for
 * instance, number of iterations before convergence).  You also may
 * add other constructors or methods, provided that these are in
 * addition to the one given below (which is the one that will be
 * automatically tested).  In particular, your code must work properly
 * when run with the <tt>main</tt> provided in <tt>RunCatMouse.java</tt>.
 */
public class ValueIteration {

    private static final double TOLERANCE = 1.0E-8;

    /** the computed optimal policy for the given MDP **/
    public int policy[];
    public int iterations;

    /** the computed optimal utility for the given MDP **/
    public double utility[];

    /**
     * The constructor for this class.  Computes the optimal policy
     * for the given <tt>mdp</tt> with given <tt>discount</tt> factor,
     * and stores the answer in <tt>policy</tt>.  Also stores the
     * optimal utility in <tt>utility</tt>.
     */
    public ValueIteration(Mdp mdp, double discount) {

	// your code here
        iterations = 0;
        policy = new int[mdp.numStates];
        utility = new double[mdp.numStates];

        // temporary array for utilities
        double[] prevUtil = new double[mdp.numStates];

        // perform Bellman updates until the utilities converge
        double delta = 0.0;
        do {
            // set delta
            delta = 0.0;
            iterations++;

            // swap utility arrays
            double[] tempUtil = utility;
            utility = prevUtil;
            prevUtil = tempUtil;

            // iterate through every state
            for (int s = 0; s < mdp.numStates; s++) {
                // find action that maximizes utility
                double max = Double.NEGATIVE_INFINITY;

                // for each action available to the state
                for (int a = 0; a < mdp.nextState[s].length; a++) {
                    // calculate utility under this action
                    double temp = 0;
                    for (int i = 0; i < mdp.nextState[s][a].length; i++) {
                        double prob = mdp.transProb[s][a][i];
                        double sPrimeUtil = prevUtil[mdp.nextState[s][a][i]];
                        temp += prob*sPrimeUtil;
                    }

                    // update if max
                    if (temp > max)
                        max = temp;
                }

                utility[s] = mdp.reward[s] + (discount*max);
                if (Math.abs(utility[s] - prevUtil[s]) > delta)
                    delta = Math.abs(utility[s] - prevUtil[s]);
            }
        } while(delta > TOLERANCE*(1-discount)/discount);

        // calculate policy
        int bestPol;
        double max;
        double eUtil;
        double sUtil;

        // iterate over all states
        for (int s = 0; s < mdp.numStates; s++) {
            max = Double.NEGATIVE_INFINITY;
            bestPol = -1;

            // iterate over all actions available to the state
            for (int a = 0; a < mdp.nextState[s].length; a++) {
                eUtil = 0.0;

                // calculate the expected utility
                for (int i = 0; i < mdp.nextState[s][a].length; i++)
                    eUtil += mdp.transProb[s][a][i]*utility[mdp.nextState[s][a][i]];

                // check if this action maximizes utility
                if (eUtil > max) {
                    bestPol = a;
                    max = eUtil;
                }
            }

            // save best action
            policy[s] = bestPol;
        }
    }

}
