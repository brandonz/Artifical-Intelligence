import java.util.*;
import java.io.*;

public class Test {
	public static void main(String[] args) throws FileNotFoundException, IOException{
		Mdp mdp = new Mdp("./data/sample.mdp");
		// System.out.println(mdp.nextState[0][0][0]);
		// System.out.println(mdp.nextState[0][0][1]);
		// System.out.println(mdp.transProb[0][0][0]);
		// System.out.println(mdp.transProb[0][0][1]);

		PolicyIteration val = new PolicyIteration(mdp, 0.95);
		for (int i = 0; i < 5; i++) {
			System.out.println(val.policy[i]);
		}
	}
}