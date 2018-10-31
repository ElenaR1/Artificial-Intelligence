import java.util.ArrayList;

 
public class Board {
    private int N;
    private int[][] blocks;
 
    public Board(int[][] blocks) {
    // construct a board from an N-by-N array of blocks
    // (where blocks[i][j] = block in row i, column j)
        N = blocks[0].length;
        this.blocks = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                this.blocks[i][j] = blocks[i][j];
            }
        }
    }
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
        
        public int getManhattan() {
        	return this.dist;
        }
        
        @Override
        public int compareTo(Node that) {
            return this.moves + this.dist - that.moves - that.dist;
        }
    }
    
    public int[] placeOfZero(){
    	int[] arr=new int[2];
    	int x=0;
    	int y=0;
    	for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if(this.blocks[i][j] ==0) {
                	x=i;
                	y= j;
                }
            }
        }
    	arr[0]=x;
    	arr[1]=y;
    	return arr;
    }
    public int dimension() {
    // board dimension N
        return N;
    }
 
    public int hamming() {
    // number of blocks out of place
        int error = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (blocks[i][j] != i*N + j + 1 && blocks[i][j] != 0) {
                    error += 1;
                }
            }
        }
        return error;
    }
 
    public int manhattan() {
    // sum of Manhattan distances between blocks and goal
        int error = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (blocks[i][j] != 0) {
                    int rowSteps = (blocks[i][j] - 1)/N;
                    int colSteps = (blocks[i][j] - 1) % N;
                    error += Math.abs(rowSteps - i) + Math.abs(colSteps - j);
                }
            }
        }
        return error;
    }
 
    public boolean isGoal() {
    // is this board the goal board?
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (blocks[i][j] != i*N + j + 1 && (i != N - 1 || j != N - 1)) //proverqva da ne e na poziciq 2,2
                {
                    return false;
                }
            }
        }
        return true;
    }
 
    public Board twin() {
    // a board that is obtained by exchanging two adjacent blocks in the same row
        Board newBoard = new Board(blocks);
        for (int i = N - 1; i >= 0; i--) {
            for (int j = N - 2; j >= 0; j--) {
                if (newBoard.blocks[i][j] != 0 && newBoard.blocks[i][j + 1] != 0) {
                    newBoard.exch(i, j, i, j + 1);
                    return newBoard;
                }
            }
        }
        return null;
    }
 
    public boolean equals(Object y) {
    // does this board equal y?
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        if (that.blocks.length != N || that.blocks[0].length != N) {
            return false;
        }
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (this.blocks[i][j] != that.blocks[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
 
    public Iterable<Board> neighbors() {
    // all neighboring boards
        if (N < 2) {
            return null;
        }
        ArrayList<Board> boards = new ArrayList<Board>();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (blocks[i][j] == 0) {
                    if (j > 0) {
                        boards.add(getNeighbor(i, j, i, j - 1));
                    }
                    if (j < N - 1) {
                        boards.add(getNeighbor(i, j, i, j + 1));
                    }
                    if (i > 0) {
                        boards.add(getNeighbor(i, j, i - 1, j));
                    }
                    if (i < N - 1) {
                        boards.add(getNeighbor(i, j, i + 1, j));
                    }
                    return boards;
                }
            }
        }
        return null;
    }
 
    private void exch(int i0, int j0, int i, int j) {
        int temp = blocks[i0][j0];
        blocks[i0][j0] = blocks[i][j];
        blocks[i][j] = temp;
    }
 
    private Board getNeighbor(int i0, int j0, int i, int j) {
        Board newBoard = new Board(blocks);
        newBoard.exch(i0, j0, i, j);
        return newBoard;
    }
 
    public String toString() {
    // string representation of this board (in the output format specified below)
        String s = "" + N + "\n";
        for (int i = 0; i < N; i++) {
            s += " ";
            for (int j = 0; j < N; j++) {
                s += blocks[i][j] + "  ";
            }
            s += "\n";
        }
        return s;
    }
    private static Node root(Node node) {
        Node current = node;
        while (current.prev != null) {
            current = current.prev;
        }
        return current;
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
    	Node n=board.new Node(board,0,null);
    /*	int[][] blocks2 = new int[N][N];
    	blocks2[0][0]= 8;
    	blocks2[0][1]= 1;
    	blocks2[0][2]= 3;
    	
    	blocks2[1][0]= 4;
    	blocks2[1][1]= 0;
    	blocks2[1][2]= 2;
    	
    	blocks2[2][0]= 7;
    	blocks2[2][1]= 6;
    	blocks2[2][2]= 5;
    	
    	int[][] blocks3 = new int[N][N];
    	blocks3[0][0]= 1;
    	blocks3[0][1]= 8;
    	blocks3[0][2]= 3;
    	
    	blocks3[1][0]= 4;
    	blocks3[1][1]= 2;
    	blocks3[1][2]= 0;
    	
    	blocks3[2][0]= 7;
    	blocks3[2][1]= 6;
    	blocks3[2][2]= 5;
    	
    	
    	
    	Board board2 = new Board(blocks2);
    	Board board3 = new Board(blocks3);
    	System.out.println(board.manhattan());//4
    	System.out.println(board2.manhattan());//10
    	System.out.println(board3.manhattan());//7
    	
    	Node n2=board2.new Node(board2,0,null);
    	Node n3=board3.new Node(board3,0,null);
    	System.out.println(n.compareTo(n2));//10-10 =0 
    	
    	 MinPQ<Node> pq = new MinPQ<Node>();
    	 pq.insert(n2);
    	 pq.insert(n3);
    	 pq.insert(n);
    	Node curr=pq.delMin();
    	System.out.println("MINIMAL BOARD: ");
    	System.out.println(curr.getBoard());//vrushta tazi s nai-maluk distance t.e manhattan
    	System.out.println(pq.min().getBoard());//tazi s dist=7
    	*/
    	System.out.println("-----------------------------------------");
    	System.out.println("TWIN: ");
    	Board board4=board.twin();
    	System.out.println(board4);
    	MinPQ<Node> p = new MinPQ<Node>();
    	Node n4=board.new Node(board4,0,null);
    	p.insert(n);
    	p.insert(n4);
    	System.out.printf("MANHATTAN OF THE TWIN: %d",board4.manhattan());//6 znachi twin-a e s po-golqm manhattan ot originalniq board  
    	Node current=p.delMin();
    	System.out.println("MIN: ");
    	System.out.println();
    	System.out.println(current.getBoard());
    	Node root=root(current);
    	//System.out.println("ROOT: ");
    	//System.out.println(root.getBoard());
    	System.out.println("ITERATION");
    	 Iterable<Board> it = current.board.neighbors();
    	 for (Board b : it) {
             if (current.prev == null || !b.equals(current.prev.board)) {
            	 System.out.println(b);           	
                 p.insert(board.new Node(b, current.moves + 1, current));
             }
    	 }
    	 System.out.println("----------------");
    	Node current2=p.delMin();
    	System.out.println(current2.getManhattan());
    	System.out.println(current2.getBoard()); 
    	int[] arr=current2.getBoard().placeOfZero();
    	System.out.printf("x= %d, y=%d", arr[0],arr[1]);
    	System.out.println();
    	Node root2=root(current2);
    	System.out.print("ROOT: ");
    	System.out.println(root.getBoard());
    	
    	System.out.println("ITERATION");
	   	 Iterable<Board> it2 = current2.board.neighbors();
	   	 for (Board b : it2) {
	            if (current.prev == null || !b.equals(current.prev.board)) {
	           	 System.out.println(b);           	
	                p.insert(board.new Node(b, current.moves + 1, current));
	            }
	   	 }
    	
    // unit tests (not graded)
       /* int N = StdIn.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                blocks[i][j] = StdIn.readInt();
            }
        }
        Board board = new Board(blocks);
        System.out.println(board.dimension());
        System.out.println(board.hamming());
        System.out.println(board.manhattan());
        System.out.println(board.isGoal());
        System.out.println(board);
        System.out.println("Neighbors:");
        Iterable<Board> it = board.neighbors();
        for (Board b : it) {
            System.out.println(b);
        }
        System.out.println("Twin:");
        System.out.println(board.twin());
        // System.out.println(board);*/
    }
}