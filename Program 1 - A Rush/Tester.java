import java.io.*;

public class Tester {
	public static void main(String[] args) throws FileNotFoundException, IOException {
		Puzzle[] puzzle = Puzzle.readPuzzlesFromFile("jam1.txt");
		AStar astar = new AStar(puzzle[0], new ZeroHeuristic(puzzle[0]));
		System.out.println("FINISHED!");
	}
}