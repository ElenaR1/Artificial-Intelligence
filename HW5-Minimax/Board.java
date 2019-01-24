import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Board {
	private List<Point> availablePoints;
	private int[][] board = new int[3][3];
	private List<PointsAndScores> children = new ArrayList<>();
	private int limitDepth = -1;

	
	  /*public Board(int b[][]) { for (int i = 0; i < 3; i++) { for (int j = 0; j <
	  3; j++) { board[i][j] = b[i][j]; } } for (int i = 0; i < board.length; i++) {
	  for (int j = 0; j < board.length; j++) { System.out.print(board[i][j] + " ");
	  } System.out.println(); } }
	 */

	public int scoreOfBoard() {
		int score = 0;
		for (int i = 0; i < 3; ++i) {
			int blank = 0;
			int X = 0;
			int O = 0;
			for (int j = 0; j < 3; ++j) {
				if (board[i][j] == 0) {
					blank++;
				} else if (board[i][j] == 1) {
					X++;
				} else {
					O++;
				}

			}
			score += evaluateScore(X, O);
		}
		//  columns
		for (int j = 0; j < 3; ++j) {
			int blank = 0;
			int X = 0;
			int O = 0;
			for (int i = 0; i < 3; ++i) {
				if (board[i][j] == 0) {
					blank++;
				} else if (board[i][j] == 1) {
					X++;
				} else {
					O++;
				}
			}
			score += evaluateScore(X, O);
		}

		int blank = 0;
		int X = 0;
		int O = 0;

		//  diagonal (first)
		for (int i = 0, j = 0; i < 3; ++i, ++j) {
			if (board[i][j] == 1) {
				X++;
			} else if (board[i][j] == 2) {
				O++;
			} else {
				blank++;
			}
		}

		score += evaluateScore(X, O);

		blank = 0;
		X = 0;
		O = 0;

		//  Diagonal (Second)
		for (int i = 2, j = 0; i > -1; --i, ++j) {
			if (board[i][j] == 1) {
				X++;
			} else if (board[i][j] == 2) {
				O++;
			} else {
				blank++;
			}
		}

		score += evaluateScore(X, O);

		return score;
	}

	private int evaluateScore(int X, int O) {
		int change;
		if (X == 3) {
			change = 100;
		} else if (X == 2 && O == 0) {
			change = 10;
		} else if (X == 1 && O == 0) {
			change = 1;
		} else if (O == 3) {
			change = -100;
		} else if (O == 2 && X == 0) {
			change = -10;
		} else if (O == 1 && X == 0) {
			change = -1;
		} else {
			change = 0;
		}
		return change;
	}

	public int minimax(int alpha, int beta, int depth, int turn) {
		if (beta <= alpha) {
			System.out.println("Pruning at depth = " + depth);

			if (turn == 1)
				return Integer.MAX_VALUE;// parent node is a min
			else
				return Integer.MIN_VALUE;
		}
		if (depth == limitDepth || isGameOver()) {
			return scoreOfBoard();
		}
		List<Point> pointsAvailable = getAvailableStates();

		if (pointsAvailable.isEmpty()) {
			System.out.println("empty");
			return 0;
		}
		if (depth == 0) {
			children.clear();
		}
		int maxValue = Integer.MIN_VALUE, minValue = Integer.MAX_VALUE;
		for (int i = 0; i < pointsAvailable.size(); ++i) {
			Point point = pointsAvailable.get(i);

			int currentScore = 0;
			// max
			if (turn == 1) {
				placeAMove(point, 1);

				currentScore = minimax(alpha, beta, depth + 1, 2);
				maxValue = Math.max(maxValue, currentScore);
				alpha = Math.max(currentScore, alpha);
				if (depth == 0)
					children.add(new PointsAndScores(currentScore, point));
			}
			// min
			else if (turn == 2) {
				placeAMove(point, 2);
				currentScore = minimax(alpha, beta, depth + 1, 1);
				minValue = Math.min(minValue, currentScore);
				beta = Math.min(currentScore, beta);
			}

			board[point.x][point.y] = 0;
			// if there was orunning we shouldn't check the other sibling states
			if (currentScore == Integer.MAX_VALUE || currentScore == Integer.MIN_VALUE)
				break;
		}
		if (turn == 1) {
			return maxValue;
		} else
			return minValue;

	}

	public List<Point> getAvailableStates() {
		availablePoints = new ArrayList<>();
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {
				if (board[i][j] == 0) {
					availablePoints.add(new Point(i, j));
				}
			}
		}
		return availablePoints;
	}

	public void placeAMove(Point point, int player) {
		int x = point.x;
		int y = point.y;
		board[x][y] = player; // player =1->X 2->O
	}

	public boolean isGameOver() {
		return (hasXWon() || hasOWon() || getAvailableStates().isEmpty());
	}

	public boolean hasXWon() {
		if ((board[0][0] == board[1][1] && board[0][0] == board[2][2] && board[0][0] == 1)
				|| (board[0][2] == board[1][1] && board[0][2] == board[2][0] && board[0][2] == 1)) {
			// System.out.println("X Diagonal Win");
			return true;
		}
		for (int i = 0; i < 3; ++i) {
			if (((board[i][0] == board[i][1] && board[i][0] == board[i][2] && board[i][0] == 1)
					|| (board[0][i] == board[1][i] && board[0][i] == board[2][i] && board[0][i] == 1))) {
				// System.out.println("X Row or Column win");
				return true;
			}
		}
		return false;
	}

	public boolean hasOWon() {
		if ((board[0][0] == board[1][1] && board[0][0] == board[2][2] && board[0][0] == 2)
				|| (board[0][2] == board[1][1] && board[0][2] == board[2][0] && board[0][2] == 2)) {
			// System.out.println("O Diagonal Win");
			return true;
		}
		for (int i = 0; i < 3; ++i) {
			if ((board[i][0] == board[i][1] && board[i][0] == board[i][2] && board[i][0] == 2)
					|| (board[0][i] == board[1][i] && board[0][i] == board[2][i] && board[0][i] == 2)) {
				// System.out.println("O Row or Column win");
				return true;
			}
		}

		return false;
	}

	public Point returnBestMove() {
		int MAX = -100000;
		int best = -1;

		for (int i = 0; i < children.size(); ++i) {
			if (MAX < children.get(i).score) {
				MAX = children.get(i).score;
				best = i;
			}
		}

		return children.get(best).point;
	}

	public void displayBoard() {
		System.out.println();

		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {
				System.out.print(board[i][j] + " ");
			}
			System.out.println();

		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*
		  Point p = new Point(3, 4); System.out.println(p); int[][] board = new
		  int[3][3]; board[0][0] = 1; board[0][1] = 1; board[0][2] = 1;
		  
		  board[1][0] = 2; board[1][1] = 1; board[1][2] = 2;
		  
		  board[2][0] = 2; board[2][1] = 0; board[2][2] = 2; Board b = new
		  Board(board);
		  
		  System.out.println(b.scoreOfBoard()); int[][] board2 = new int[3][3];
		  board2[0][0] = 1; board2[0][1] = 0; board2[0][2] = 1;
		  
		  board2[1][0] = 0; board2[1][1] = 1; board2[1][2] = 2;
		  
		  board2[2][0] = 2; board2[2][1] = 2; board2[2][2] = 2;
		  
		  Board b2 = new Board(board2); System.out.println(b2.scoreOfBoard());// shte
		 // vurne -90 < - 100 t.e min shte izbere -100(a za da poluchim -100) // trqbva
		//  da ograem poveche // t.e dori da zagubim(koeto ne se sluchva) da sme
		 // napravili vuzmojno nai-mnogo // hodove 
		  int[][] board3 = new int[3][3];
		  board3[0][0] = 0; board3[0][1] = 1; board3[0][2] = 1;
		  
		  board3[1][0] = 0; board3[1][1] = 1; board3[1][2] = 2;
		  
		  board3[2][0] = 2; board3[2][1] = 2; board3[2][2] = 2;
		  
		  Board b3 = new Board(board3); System.out.println(b3.scoreOfBoard());// -91t.k ima kolona v koqto ima samo edna 2ka
		 */
		Board b = new Board();
		Random rand = new Random();

		b.displayBoard();
		System.out.println("Who is going to be first?For computer choose 1. For user choose 2: ");
		Scanner s = new Scanner(System.in);
		int choice = s.nextInt();
		if (choice == 1) {
			Point p = new Point(rand.nextInt(3), rand.nextInt(3));
			b.placeAMove(p, 1);
			b.displayBoard();
		}
		while (!b.isGameOver()) {
			System.out.println("Your move: ");
			Point userMove = new Point(s.nextInt(), s.nextInt());

			b.placeAMove(userMove, 2);
			b.displayBoard();
			if (b.isGameOver())
				break;
			b.minimax(Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 1);
			for (PointsAndScores pas : b.children)
				System.out.println("Point: " + pas.point + " Score: " + pas.score);

			b.placeAMove(b.returnBestMove(), 1);
			b.displayBoard();
		}
		if (b.hasXWon()) {
			System.out.println("Unfortunately, you lost!");
		} else if (b.hasOWon()) {
			System.out.println("You win!");
		} else {
			System.out.println("a draw!");
		}
	}

}
