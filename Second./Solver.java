import java.util.*;


public class Solver {
    private MinPQ<Node> pq = new MinPQ<Node>();
    private int minMoves = -1;
    private Node bestNode;
    private boolean solved;
 
    private class Node implements Comparable<Node> {
        private Board board;
        private int moves, dist;
        private Node prev;
 
        public Node(Board board, int moves, Node prev) {
            this.board = board;
            this.moves = moves;
            this.prev = prev;
            dist = board.manhattan();
        }
        
        public Board getBoard() {
        	return this.board;
        }
        
        @Override
        public int compareTo(Node that) {
            return this.moves + this.dist - that.moves - that.dist;
        }
    }
 
    public Solver(Board initial) {
    // find a solution to the initial board (using the A* algorithm)
        if (initial == null) {
            throw new NullPointerException();
        }
        pq.insert(new Node(initial, 0, null)); 
        pq.insert(new Node(initial.twin(), 0, null));
        while (!pq.isEmpty()) {
            Node current = pq.delMin();
            if (current.board.isGoal()) {
            	//System.out.println("MIN: ");
            	//System.out.println(current.getBoard());
                Node root = root(current);
                System.out.println("ROOT:");
                System.out.println(root.getBoard());
                if (!root.board.equals(initial)) {
                    break;
                }
                solved = true;
                if (minMoves == -1 || current.moves < minMoves) {
                	System.out.println("aaaaaaaa");
                	System.out.println(minMoves);
                	System.out.println(current.getBoard());
                    minMoves = current.moves;
                    bestNode = current;
                }
            } 
            if (minMoves == -1 || current.moves + current.dist < minMoves) {
            	System.out.println("bbbbbbbbbbbbbbbbbbb");
            	System.out.println(minMoves);
            	System.out.print("current board: ");
            	System.out.println(current.getBoard());
                Iterable<Board> it = current.board.neighbors();
                for (Board b : it) {
                    if (current.prev == null || !b.equals(current.prev.board)) {
                    	System.out.println(b);
                        pq.insert(new Node(b, current.moves + 1, current));
                    }
                }
            } else {
                break;
            }
        }
    }
 
    private Node root(Node node) {
        Node current = node;
        while (current.prev != null) {
            current = current.prev;
        }
        return current;
    }
    public boolean isSolvable() {
    // is the initial board solvable?
        return solved;
    }
 
    public int moves() {
    // min number of moves to solve initial board; -1 if unsolvable
        return minMoves;
    }
 
    public Iterable<Board> solution() {
    // sequence of boards in a shortest solution; null if unsolvable
        if (isSolvable()) {
            Stack<Board> sol = new Stack<Board>();
            Node current = bestNode;
            while (current != null) {
                sol.push(current.board);
                current = current.prev;
            }
            return sol;
        }
        return null;
    }
 
    public static void main(String[] args) {

    	int N=3;
    	int[][] blocks = new int[N][N];
    	blocks[0][0]= 0;
    	blocks[0][1]= 1;
    	blocks[0][2]= 3;
    	
    	blocks[1][0]= 4;
    	blocks[1][1]= 2;
    	blocks[1][2]= 5;
    	
    	blocks[2][0]= 7;
    	blocks[2][1]= 8;
    	blocks[2][2]= 6;
    	
    	Board board = new Board(blocks);
    	 
        Solver solver = new Solver(board); 
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board b : solver.solution())
                StdOut.println(b);
        }
    	
    // solve a slider puzzle (given below)
    // create initial board from file
     /*   In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
 
        // solve the puzzle
        Solver solver = new Solver(initial);
        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }*/
    }
}