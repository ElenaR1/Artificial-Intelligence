import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

public class Board {
	private Random ran = new Random();
	private int[] rows;
	private ArrayList<Integer> chosenQueens = new ArrayList<Integer>();
	private Vector<Integer> v = new Vector();

	public Board(int n) {
		rows = new int[n];
		randomBoard();
	}

	public void random() {
		int n = rows.length;

		for (int i = 0; i < n; i++) {
			rows[i] = ran.nextInt(n);
		}

		/*
		 * int i=0; while(i<n) { rows[i] = ran.nextInt(n); if (!v.contains(rows[i])) {
		 * v.add(i, rows[i]); i++; } }
		 */
	}

	public void randomBoard() {
		int n = rows.length;
		for (int i = 0; i < n; i++) {
			rows[i] = i;
		}
		for (int i = 0; i < n; i++) {
			int j = ran.nextInt(n);
			int rowToSwap = rows[i];
			rows[i] = rows[j];
			rows[j] = rowToSwap;
		}
	}

	public int conflicts(int row, int col) {
		int count = 0;
		for (int c = 0; c < rows.length; c++) {
			if (c == col)
				continue;// if it is the element itself
			int r = rows[c];
			if (r == row || Math.abs(r - row) == Math.abs(c - col))
				count++;// firstly we check if it is in the same row
		}
		return count;
	}

	public int getMaxConflicts(int maxConflicts) {

		for (int c = 0; c < rows.length; c++) {
			int conflicts = conflicts(rows[c], c);
			if (conflicts == maxConflicts) {
				chosenQueens.add(c);
			} else if (conflicts > maxConflicts) {
				maxConflicts = conflicts;
				chosenQueens.clear();
				chosenQueens.add(c);
			}
		}
		return maxConflicts;
	}

	public int getMinConflicts(int minConflicts, int worstQueenColumn) {

		for (int r = 0; r < rows.length; r++) {
			int conflicts = conflicts(r, worstQueenColumn);
			if (conflicts == minConflicts) {
				chosenQueens.add(r);
			} else if (conflicts < minConflicts) {
				minConflicts = conflicts;
				chosenQueens.clear();
				chosenQueens.add(r);
			}
		}
		return minConflicts;
	}

	public void solve() {
		int moves = 0;

		while (true) {
			int maxConflicts = 0;
			chosenQueens.clear();
			maxConflicts = getMaxConflicts(maxConflicts);

			if (maxConflicts == 0) {
				break;
			}
			// one random queen from the ones with the most conflicts
			int worstQueenColumn = chosenQueens.get(ran.nextInt(chosenQueens.size()));// gets a number between 0 and
																						// chosenQueens.size()(exclusive)
			int minConflicts = rows.length;
			chosenQueens.clear();
			minConflicts = getMinConflicts(minConflicts, worstQueenColumn);

			if (!chosenQueens.isEmpty()) {
				rows[worstQueenColumn] = chosenQueens.get(ran.nextInt(chosenQueens.size()));
			}

			moves++;
			if (moves == rows.length * 2) {
				randomBoard();
				moves = 0;
			}
		}
	}

	public void printBoard() {
		for (int i = 0; i < rows.length; i++) {
			for (int j = 0; j < rows.length; j++) {
				if (rows[j] == i) {
					System.out.print('*');
				} else
					System.out.print('.');
			}
			System.out.println();
		}
	}
}