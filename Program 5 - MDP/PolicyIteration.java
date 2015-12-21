/**********************************************************************************/
// Edited By: Brandon Zhou
// netID: brandonz
// Course: COS 402
/**********************************************************************************/

/**
 * This is the template of a class that should run policy iteration on
 * a given MDP to compute the optimal policy which is returned in the
 * public <tt>policy</tt> field.  You need to fill in the constructor.
 * You may wish to add other fields with other useful information that
 * you want this class to return (for instance, number of iterations
 * before convergence).  You also may add other constructors or
 * methods, provided that these are in addition to the one given below
 * (which is the one that will be automatically tested).  In
 * particular, your code must work properly when run with the
 * <tt>main</tt> provided in <tt>RunCatMouse.java</tt>.
 */
public class PolicyIteration {

    /** the computed optimal policy for the given MDP **/
    public int policy[];
    public int iterations;
    public int evalIterations;

    /**
     * The constructor for this class.  Computes the optimal policy
     * for the given <tt>mdp</tt> with given <tt>discount</tt> factor,
     * and stores the answer in <tt>policy</tt>.
     */
    public PolicyIteration(Mdp mdp, double discount) {

	// your code here
        iterations = 0;
        evalIterations = 0;
        policy = new int[mdp.numStates];

        // declare a flag to determine if policy improvement yields changes
        boolean flag;
        do {
            iterations++;

            // policy evaluation
            PolicyEvaluation polEval = new PolicyEvaluation(mdp, discount, policy);
            evalIterations += polEval.iterations;
            
            // policy improvement
            flag = false;

            // iterate over all states
            for (int s = 0; s < mdp.numStates; s++) {
                double max = Double.NEGATIVE_INFINITY;
                int bestPol = -1;

                // iterate over all actions available to find best policy
                for (int a = 0; a < mdp.nextState[s].length; a++) {
                    double eUtil = 0.0;

                    // calculate the expected utility
                    for (int i = 0; i < mdp.nextState[s][a].length; i++)
                        eUtil += mdp.transProb[s][a][i]*polEval.utility[mdp.nextState[s][a][i]];

                    // check if this action maximizes utility
                    if (eUtil > max) {
                        bestPol = a;
                        max = eUtil;
                    }
                }

                int currPol = policy[s];
                double currUtil = 0.0;
                // iterate over all actions under the current policy
                for (int i = 0; i < mdp.nextState[s][currPol].length; i++) {
                    double transProb = mdp.transProb[s][currPol][i];
                    double util = polEval.utility[mdp.nextState[s][currPol][i]];
                    currUtil += transProb*util;
                }

                // save best action
                if (max > currUtil) {
                    policy[s] = bestPol;
                    flag = true;
                }
            }

        } while (flag);
    }
}
