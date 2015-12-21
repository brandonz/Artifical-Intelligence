import java.io.*;

public class Tester {
	public static void main(String[] args) throws FileNotFoundException, IOException {
		String net = "./data/alarm.bn";
		String query = "./data/alarm1.qry";
		BayesNet bn = new BayesNet(net);
		Query q = new Query(bn, query);

		for (int i = 0; i < bn.parents.length; i++) {
			System.out.println("Parents of " + bn.varName[i] + ": ");
			for (int j = 0; j < bn.parents[i].length; j++) {
				System.out.print(bn.varName[bn.parents[i][j]] + " ");
			}
			System.out.println();
			System.out.println();
		}

		// Mcmc mc = new Mcmc(bn, q);
		// double[] result = mc.runMoreIterations(100000);
		// System.out.println(result[0] + " " + result[1]);

		// // num variables
		// System.out.println(bn.numVariables);
		// System.out.println();

		// // varnames
		// for (String var : bn.varName) {
		// 	System.out.print(var + " ");
		// }
		// System.out.println();
		// System.out.println();

		// // numvals
		// for (int vals : bn.numValues) {
		// 	System.out.print(vals + " ");
		// }
		// System.out.println();
		// System.out.println();

		// // value names
		// for (String[] var : bn.valueNames) {
		// 	for (String val : var) {
		// 		System.out.print(val + " ");
		// 	}
		// 	System.out.println();
		// }
		// System.out.println();

		// // num parents
		// for (int num : bn.numParents) {
		// 	System.out.print(num + " ");
		// }
		// System.out.println();
		// System.out.println();

		// // parents 
		// for (int[] var : bn.parents) {
		// 	for (int parent : var) {
		// 		System.out.print(parent + " ");
		// 	}
		// 	System.out.println();
		// }
		// System.out.println();
	}
}