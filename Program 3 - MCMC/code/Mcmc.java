/**********************************************************************************/
// Edited By: Brandon Zhou
// netID: brandonz
// Course: COS 402
/**********************************************************************************/

/**
 *  This class is a template for implementing the MCMC algorithm.  You
 *  need to fill in the constructor and the
 *  <tt>runMoreIterations()</tt> method.
 */
import java.util.Random;
import java.util.HashSet;
import java.util.ArrayList;

public class Mcmc {

    private BayesNet bn;                            // bayes net
    private Query q;                                // query
    private Random rand;                            // random generator
    private int[] vals;                             // current state
    private double[] probability;                   // frequency of query variable
    private ArrayList<Integer> variables;           // list of non evidence var
    private ArrayList<HashSet<Integer>> children;   // set of children of ith var

    /**
     *  This is the constructor for the class that you need to fill
     *  in.  Any initialization of the MCMC algorithm should go here.
     *  The parameters to the constructor specify the Bayes net and
     *  query on which MCMC is to be run.
     */
    public Mcmc(BayesNet bn, Query q) {

	// fill in initialization code here
        this.bn = bn;
        this.q = q;
        rand = new Random();
        vals = new int[bn.numVariables];
        probability = new double[bn.numValues[q.queryVar]];
        variables = new ArrayList<Integer>();

        // set model
        for (int i = 0; i < q.evidence.length; i++) {
            // if evidence variable
            if (q.evidence[i] != -1)
                vals[i] = q.evidence[i];
            // otherwise select random variable and add to list
            else {
                vals[i] = rand.nextInt(bn.numValues[i]);
                variables.add(i);
            }
        }

        // get the children of each variable and add to list of sets
        children = new ArrayList<HashSet<Integer>>(bn.numVariables);
        for (int i = 0; i < bn.numVariables; i++)
            children.add(i, new HashSet<Integer>());
        for (int i = 0; i < bn.parents.length; i++) {
            for (int j = 0; j < bn.parents[i].length; j++) {
                HashSet<Integer> set = children.get(bn.parents[i][j]);
                set.add(i);
            }
        }

        // prevents division by zero if no iteration is run
        probability[vals[q.queryVar]]++;
    }

    /**
     *  This method, which must be filled in, runs <tt>n</tt>
     *  <i>additional</i> iterations of the MCMC algorithm on the
     *  Bayes net and query that were specified when this object was
     *  constructed.  It is important to remember that the method must
     *  <i>continue</i> a previous execution of MCMC.  It should
     *  <i>not</i> restart from scratch each time it is called.  The
     *  method returns an array with the estimated probability of each
     *  value of the query variable as estimated by MCMC following the
     *  <tt>n</tt> additional iterations.
     */
    public double[] runMoreIterations(int n) {

	// fill in MCMC code here
        for (int i = 0; i < n; i++)
            iteration();
        return normalize(probability);
    }

    private void iteration() {
        // get random non evidence variable
        int randIndex = rand.nextInt(variables.size());
        int var = variables.get(randIndex);

        // calculate P(xi | MB(X))
        double[] distribution = new double[bn.numValues[var]];
        HashSet<Integer> childSet = children.get(var);
        for (int i = 0; i < distribution.length; i++) {
            vals[var] = i;
            distribution[i] = bn.getCondProb(var, vals);
            for (int child : childSet)
                distribution[i] *= bn.getCondProb(child, vals);
        }
        distribution = normalize(distribution);

        // sample from P(X | MB(X))
        double r = rand.nextDouble();
        double total = 0.0;
        for (int i = 0; i < distribution.length; i++) {
            total += distribution[i];
            if (r < total) {
                vals[var] = i;
                break;
            }
        }

        // increment frequency state of query variable
        probability[vals[q.queryVar]]++;
    }

    // normalizes the input double[]
    // returns a double[] whose entries sum to 1.0
    private double[] normalize(double[] probability) {
        double[] distribution = probability.clone();
        double total = 0.0;

        for (double i : distribution)
            total += i;
        for (int i = 0; i < distribution.length; i++)
            distribution[i] /= total;

        return distribution;
    }

}
