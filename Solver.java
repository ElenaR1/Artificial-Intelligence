import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;
import java.util.Vector;

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
	            dist = board.manhattan2();
	        }
	        
	        public Board getBoard() {
	        	return this.board;
	        }
	        
	        @Override
	        public int compareTo(Node that) {
	            return this.moves + this.dist - that.moves - that.dist;
	        }
	    }
	    public Solver(Board start) {
	    	pq.insert(new Node(start, 0, null)); 
	    	while(!pq.isEmpty()) {
	    		Node curr=pq.delMin();
	    		if(curr.board.isGoal()) {
	    			Node root=root(curr);
	    			 if (!root.board.equals(start)) {
	                     break;
	                 }
	    			solved=true;
	    			//minMoves has not been changed or there is another path where the num of moves is smaller
	    			if(minMoves==-1 || curr.moves <minMoves) {
	    				minMoves=curr.moves;
	    				bestNode=curr;
	    			}
	    		}
	    		//if we still haven't found a solution or the other path is less than minMoves(we've found)
	            if (minMoves == -1 || curr.moves + curr.dist < minMoves) {
	            	 ArrayList<Board> neighbors = new ArrayList<Board>();
	            	 neighbors=curr.board.neighbors();
	            	 for (int i = 0; i <neighbors.size(); i++) {
	            		 if (curr.prev == null || !neighbors.get(i).equals(curr.prev.board)) {
	            			 pq.insert(new Node(neighbors.get(i),curr.moves+1,curr));
	            		 }
	         		}
	            }
	            else {
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
	            return solved;
	        }
	     
	        public int moves() {
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
	        
	        public  Stack<Board> solution() {
	        // sequence of boards that lead to the solution
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
		// TODO Auto-generated method stub
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
         	 Stack<Board> s=new Stack<Board>();
         	 s=solver.solution();
             for (int i=0;i<s.size();i++) {
             	s.get(i).printBoard();
             }
                 
         }
         
         Vector<String> steps=new Vector<String>();
         steps=solver.steps();
         solver.printVector(steps);
		
		
		
		
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
            System.out.println("No solution possible");
        else {
        	 System.out.println("Minimum number of moves = " + solver.moves());
        	 Stack<Board> s=new Stack<Board>();
        	 s=solver.solution();
            for (int i=0;i<s.size();i++) {
            	s.get(i).printBoard();
            }
                
        }
        Vector<String> steps=new Vector<String>();
        steps=solver.steps();
        solver.printVector(steps);
        */
		
		//4x4
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
         	 Stack<Board> s=new Stack<Board>();
         	 s=solver.solution();
             for (int i=0;i<s.size();i++) {
             	s.get(i).printBoard();
             }
                 
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
             System.out.println("No solution possible");
         else {
         	 System.out.println("Minimum number of moves = " + solver.moves());
         	 Stack<Board> s=new Stack<Board>();
         	 s=solver.solution();
             for (int i=0;i<s.size();i++) {
             	s.get(i).printBoard();
             }
                 
         }
         Vector<String> steps=new Vector<String>();
         steps=solver.steps();
         solver.printVector(steps);
         */
		/*int N=3;
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
            System.out.println("No solution possible");
        else {
        	 System.out.println("Minimum number of moves = " + solver.moves());
        	 Stack<Board> s=new Stack<Board>();
        	 s=solver.solution();
            for (int i=0;i<s.size();i++) {
            	s.get(i).printBoard();
            }
                
        }
        Vector<String> steps=new Vector<String>();
        steps=solver.steps();
        solver.printVector(steps);
        */
	}
	
}
