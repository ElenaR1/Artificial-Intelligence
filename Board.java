import java.util.ArrayList;
import java.util.Arrays;

public class Board {
	private int N;
	private int[][] blocks;
	private int[][] goalBlocks;
	
	public Board(int[][] b) {
		 N = b[0].length;
	        this.blocks = new int[N][N];
	        for (int i = 0; i < N; i++) {
	            for (int j = 0; j < N; j++) {
	                this.blocks[i][j] = b[i][j];
	            }
	        }
	        setGoalBlocks();
	}
	public void setGoalBlocks() {
		int N=this.size();
		  this.goalBlocks = new int[N][N];
		for (int i = 0; i < N; i++) {
	            for (int j = 0; j < N; j++) {
	            	if(i!=N-1 || j!=N-1) {
	                this.goalBlocks[i][j] = i*N+j+1;
	            	}
	            }
	        }
	        goalBlocks[N-1][N-1]=0;
	}
	private class Node implements Comparable<Node> {
        private Board board;
        private int moves, dist;
        private Node prev;
 
        public Node(Board board, int moves, Node prev) {
            this.board = board;
            this.moves = moves;
            this.prev = prev;
            dist = board.manhattan2();
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
	public int size() {
		return N;
	}
	
	//e.g if N=4 blocks[1][1]=6=1*4+1+1
	//if N=3 blocks[1][1]=5=1*3+1+1
	public int blocksOutOfPlace() {
		int count = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (blocks[i][j] != i*N + j + 1 && blocks[i][j] != 0)//because if it is zero it won't be equal to i*N+j+1
                {
                    count += 1;
                }
            }
        }
        return count;
	}
	
	
	//it shows how far away the each element is from where it should be and then sums the distances
	 public int[] correctPlace(int el){
	    	int[] arr=new int[2];
	    	int x=0;
	    	int y=0;
	    	for (int i = 0; i < N; i++) {
	            for (int j = 0; j < N; j++) {
	                if(this.goalBlocks[i][j] ==el) {
	                	x=i;
	                	y= j;
	                }
	            }
	        }
	    	arr[0]=x;
	    	arr[1]=y;
	    	return arr;
	    }
	public int manhattan2() {
		int error = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (blocks[i][j] != 0) {
                	int[] arr=correctPlace(blocks[i][j]);
                    int rowSteps = arr[0];
                    int colSteps = arr[1];
                    error += Math.abs(rowSteps - i) + Math.abs(colSteps - j);
                }
            }
        }
        return error;
	}
	 public boolean isGoal() {
		        for (int i = 0; i < N; i++) {
		            for (int j = 0; j < N; j++) {
		                if (blocks[i][j] != i*N + j + 1 && (i != N - 1 || j != N - 1)) //it is not the element in the down right angle because there should be the 0
		                {
		                    return false;
		                }
		            }
		        }
		        return true;
		    }
	
	//checks if two boards are equal
	 public boolean equals(Board other) {
		        if (other == this)
		        	return true;
		        if (other == null) 
		        	return false;
		        if (other.blocks[0].length != N) {
		            return false;
		        }
		        for (int i = 0; i < N; i++) {
		            for (int j = 0; j < N; j++) {
		                if (this.blocks[i][j] != other.blocks[i][j]) {
		                    return false;
		                }
		            }
		        }
		        return true;
		    }
	
	 
	 public  ArrayList<Board> neighbors() {
	        if (N < 2) {
	            return null;
	        }
	        ArrayList<Board> neighbors = new ArrayList<Board>();
	        for (int i = 0; i < N; i++) {
	            for (int j = 0; j < N; j++) {
	                if (blocks[i][j] == 0) {
	                    if (j > 0) {
	                    	neighbors.add(getNeighbor(i, j, i, j - 1));//moves 0 to the left
	                    }
	                    if (j < N - 1) {
	                    	neighbors.add(getNeighbor(i, j, i, j + 1));//moves 0 to the right
	                    }
	                    if (i > 0) {
	                    	neighbors.add(getNeighbor(i, j, i - 1, j));//moves 0 downwards
	                    }
	                    if (i < N - 1) {
	                    	neighbors.add(getNeighbor(i, j, i + 1, j));//moves 0 upwards
	                    }
	                    return neighbors;
	                }
	            }
	        }
	        return null;
	    }
	 
	 public void exchange(int i0, int j0, int i, int j) {
	        int temp = blocks[i0][j0];
	        blocks[i0][j0] = blocks[i][j];
	        blocks[i][j] = temp;
	    }
	 
	 public Board getNeighbor(int i0, int j0, int i, int j) {
	        Board newBoard = new Board(blocks);
	        newBoard.exchange(i0, j0, i, j);
	        return newBoard;
	    }
	 public static Node root(Node node) {
         Node current = node;
         while (current.prev != null) {
             current = current.prev;
         }
         return current;
     }
	 public void printBoard() {
		 for (int i = 0; i < N; i++) {
	            for (int j = 0; j < N; j++) {
	                System.out.print(blocks[i][j]+" ");
	            }
	            System.out.println();
	        }
		 System.out.println();
	 }
	
	 
	public static void main(String[] args) {
		// TODO Auto-generated method stub
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
    	
    	int[][] blocks4 = new int[N][N];
    	blocks4[0][0]= 0;
    	blocks4[0][1]= 1;
    	blocks4[0][2]= 3;
    	
    	blocks4[1][0]= 4;
    	blocks4[1][1]= 2;
    	blocks4[1][2]= 5;
    	
    	blocks4[2][0]= 7;
    	blocks4[2][1]= 6;
    	blocks4[2][2]= 8;
    	
    	Board board = new Board(blocks);
    	Board board4 = new Board(blocks4);
    	Node n=board.new Node(board,0,null);
    	System.out.println("-----------------------------------------");
    	System.out.println("TWIN: ");
    	board4.printBoard();
    	MinPQ<Node> p = new MinPQ<Node>();
    	Node n4=board.new Node(board4,0,null);
    	p.insert(n);
    	p.insert(n4);
    	System.out.printf("MANHATTAN OF THE TWIN: %d",board4.manhattan2());//6 znachi twin-a e s po-golqm manhattan ot originalniq board  
    	Node current=p.delMin();
    	System.out.println("MIN: ");
    	System.out.println();
    	current.getBoard().printBoard();
    	Node root=root(current);

    	 System.out.println("ITERATION2");
    	 ArrayList<Board> neighbors = new ArrayList<Board>();
    	 neighbors=current.board.neighbors();
    	 for (int i = 0; i <neighbors.size(); i++) {
			neighbors.get(i).printBoard();
			p.insert(board.new Node(neighbors.get(i),current.moves+1,current));
		}
    	 
    	 System.out.println("----------------");
    	Node current2=p.delMin();
    	System.out.println(current2.getManhattan()); 
    	current2.getBoard().printBoard();
    	int[] arr=current2.getBoard().placeOfZero();
    	System.out.printf("x= %d, y=%d", arr[0],arr[1]);
    	System.out.println();
    	Node root2=root(current2);
    	System.out.print("ROOT: ");
    	root.getBoard().printBoard();
    	System.out.println("ITERATION");

    	
    	int[][] blocks2 = new int[N][N];
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
    	System.out.println(board.manhattan2());//4
    	System.out.println(board2.manhattan2());//10
    	System.out.println(board3.manhattan2());//7
    	
    	
	}

}
