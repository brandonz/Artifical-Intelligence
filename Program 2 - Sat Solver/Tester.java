import java.util.ArrayList;
import java.util.Random;
public class Tester {
	public static void main(String[] args) {
		// int[][] test = {{1, 2, 3}, {4, 5, 6}};
		// System.out.println(test.length);
		// System.out.println(test[0].length);
		// int[] test2 = test[1];
		// System.out.println();

		// for (int i = 0; i < test2.length; i++) {
		// 	System.out.println(test2[i]);
		// }
		// System.out.println();

		// ArrayList<Integer> list = new ArrayList<Integer>();
		// System.out.println(list.size());
		// System.out.println();

		// Random rand = new Random();
		// System.out.println(rand.nextInt(1));
		// System.out.println();


		// RandomSatisfiedCnfGenerator gen = new RandomSatisfiedCnfGenerator(1000, 700, 300);
		MyGenerator gen = new MyGenerator();
		Cnf cnf = gen.getNext();
		int numSymbols = cnf.getNumSymbols();
		MySatSolver solver = new MySatSolver();
		Literal[][] literals = cnf.toLiteralArray();
		boolean[] model = solver.solve(literals, numSymbols, new Timer(60000));
		// for (int i = 0; i < model.length; i++) {
		// 	System.out.println(model[i]);
		// }
		System.out.println();
		// cnf.print();
		// System.out.println();

		// int[][] table = new int[2][5];
		// System.out.println(table[0][0]);
		// table[0][0] = -1;
		// System.out.println(table[0][0]);
	}
}