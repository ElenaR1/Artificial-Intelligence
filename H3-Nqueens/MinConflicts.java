import java.util.Scanner;

public class MinConflicts {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.print("N: ");
		Scanner scanner = new Scanner(System.in);
		int N = scanner.nextInt();
		if (N < 4) {
			System.out.println("Enter a valid number");
		} else {
			Board board = new Board(N);
			// board.printBoard();
			// System.out.println();
			// System.out.println();
			// System.out.println();
			board.solve();
			board.printBoard();
		}
	}

}
