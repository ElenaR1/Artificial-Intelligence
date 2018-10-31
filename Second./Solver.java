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
                //System.out.println("ROOT:");
               // System.out.println(root.getBoard());
                if (!root.board.equals(initial)) {
                    break;
                }
                solved = true;
                if (minMoves == -1 || current.moves < minMoves) {
                	//System.out.println("aaaaaaaa");
                	//System.out.println(minMoves);
                	//System.out.println(current.getBoard());
                    minMoves = current.moves;
                    bestNode = current;
                }
            } 
            if (minMoves == -1 || current.moves + current.dist < minMoves) {
            	//System.out.println("bbbbbbbbbbbbbbbbbbb");
            	//System.out.println(minMoves);
            	//System.out.print("current board: ");
            	//System.out.println(current.getBoard());
                Iterable<Board> it = current.board.neighbors();
                for (Board b : it) {
                    if (current.prev == null || !b.equals(current.prev.board)) {
                    	//System.out.println(b);
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
    public String changeOfZero (Board curr,Board prev) {
    	String step="";
    	
    	int [] currpos=curr.placeOfZero();
    	int curx=currpos[0];
    	int cury=currpos[1];
    	
    	int [] prevpos=prev.placeOfZero();
    	int prevx=prevpos[0];
    	int prevy=prevpos[1];
    	
    	if(curx==prevx+1) {
    		step="up";
    	}
    	if(curx==prevx-1) {
    		step="down";
    	}
    	if(cury==prevy+1) {
    		step="left";
    	}
    	if(cury==prevy-1) {
    		step="right";
    	}
    	return step;
    }
    
    public void printVector(Vector<String> v) {
    	for (int i = v.size()-1; i >=0; i--) {
			System.out.println(v.elementAt(i));
		}
    }
    
    public Vector<String>steps(){
    	Vector<String> v=new Vector<String>();
    	if (isSolvable()) {
            Node current = bestNode;
            while (current.prev != null) {
               v.add(changeOfZero(current.board,current.prev.board));
                current = current.prev;
            }
            return v;
        }
        return null;
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
    	
    	System.out.print("N: ");
    	Scanner scanner = new Scanner(System.in); 
    	int N=scanner.nextInt();
    	int n=(int) Math.sqrt(N+1);
    	int[][] blocks = new int[n][n];
    	System.out.println("Choose how the table is going to look like: ");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                blocks[i][j] = scanner.nextInt();
            }
        }
    	
         Board board = new Board(blocks);
    	 
         Solver solver = new Solver(board); 
         if (!solver.isSolvable())
        	 System.out.println("No solution possible");
         else {
        	 System.out.println("Minimum number of moves = " + solver.moves());
             for (Board b : solver.solution())
            	 System.out.println(b);
         }
         
         Vector<String> steps=new Vector<String>();
         steps=solver.steps();
         solver.printVector(steps);
       
    	
    	//3X3
    	/*int N=3;
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
        
        Vector<String> steps=new Vector<String>();
        steps=solver.steps();
        solver.printVector(steps);
        */
    	
    	
    	
    	//4X4
    	/*int N=4;
    	int[][] blocks4 = new int[N][N];
    	blocks4[0][0]= 1;
    	blocks4[0][1]= 2;
    	blocks4[0][2]= 3;
    	blocks4[0][3]= 4;
    	
    	blocks4[1][0]= 0;
    	blocks4[1][1]= 5;
    	blocks4[1][2]= 6;
    	blocks4[1][3]= 7;
    	
    	blocks4[2][0]= 8;
    	blocks4[2][1]= 9;
    	blocks4[2][2]= 10;
    	blocks4[2][3]= 11;
    	
    	blocks4[3][0]= 12;
    	blocks4[3][1]= 13;
    	blocks4[3][2]= 14;
    	blocks4[3][3]= 15;
    	
    	Board board = new Board(blocks4);
    	 
        Solver solver = new Solver(board); 
        if (!solver.isSolvable())
        	System.out.println("No solution possible");
        else {
        	System.out.println("Minimum number of moves = " + solver.moves());
            for (Board b : solver.solution())
            	System.out.println(b);
        }
        
        Vector<String> steps=new Vector<String>();
        steps=solver.steps();
        solver.printVector(steps);
        
        */
    	/*int N=4;
    	int[][] blocks4 = new int[N][N];
    	blocks4[0][0]= 1;
    	blocks4[0][1]= 2;
    	blocks4[0][2]= 3;
    	blocks4[0][3]= 4;
    	
    	blocks4[1][0]= 5;
    	blocks4[1][1]= 6;
    	blocks4[1][2]= 0;
    	blocks4[1][3]= 8;
    	
    	blocks4[2][0]= 9;
    	blocks4[2][1]= 10;
    	blocks4[2][2]= 7;
    	blocks4[2][3]= 11;
    	
    	blocks4[3][0]= 13;
    	blocks4[3][1]= 14;
    	blocks4[3][2]= 15;
    	blocks4[3][3]= 12;
    	
    	Board board = new Board(blocks4);
    	 
        Solver solver = new Solver(board); 
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board b : solver.solution())
                StdOut.println(b);
        }
        
        Vector<String> steps=new Vector<String>();
        steps=solver.steps();
        solver.printVector(steps);
    	*/
        
       /* System.out.println("-----------------------------");
        
        int[][] blocks2 = new int[N][N];
        blocks2[0][0]= 4;
        blocks2[0][1]= 1;
        blocks2[0][2]= 3;
    	
        blocks2[1][0]= 0;
        blocks2[1][1]= 2;
        blocks2[1][2]= 5;
    	
        blocks2[2][0]= 7;
        blocks2[2][1]= 8;
        blocks2[2][2]= 6;
    	
    	Board board2 = new Board(blocks2);
    	 
        System.out.println(solver.changeOfZero(board2, board));*/
        
        
    /*	int N=3;
    	int[][] blocks = new int[N][N];
    	blocks[0][0]= 4;
    	blocks[0][1]= 1;
    	blocks[0][2]= 3;
    	
    	blocks[1][0]= 0;
    	blocks[1][1]= 2;
    	blocks[1][2]= 6;
    	
    	blocks[2][0]= 7;
    	blocks[2][1]= 5;
    	blocks[2][2]= 8;
    	
    	Board board = new Board(blocks);
    	 
        Solver solver = new Solver(board); 
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board b : solver.solution())
                StdOut.println(b);
        }
    	*/
    	
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