import java.util.*;
public class Tester {
	
	public static void main(String[] args) {
		String file = "./data/robot_no_momemtum.data";
		DataSet ds = null;
		try {
			ds = new DataSet(file);
		}
		catch (Exception e) {

		}

		System.out.println(ds.trainState.length == ds.trainOutput.length);

		// System.out.println(ds.trainState.length == ds.trainOutput.length);
		// for (int i = 0; i < ds.trainState.length; i++) {
		// 	System.out.println(ds.trainState[i].length == ds.trainOutput[i].length);
		// }

		// for (int[] i : ds.trainState) {
		// 	for (int j : i)
		// 		System.out.print(ds.stateName[j] + " ");
		// 	System.out.println();
		// 	System.out.println();
		// }

	}

}