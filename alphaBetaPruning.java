import java.awt.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class alphaBetaPruning {
	public final static int SIZE = 8;
	public static char[][] board = new char[SIZE][SIZE];
	public static int turnCounter;
	public static int timeLimit;
	public static int depth = 0;
	public static long start, end, total;
	public static int[] best = new int[2], currentBest = new int[2];
	public static int curBestEval = Integer.MIN_VALUE;
	public static boolean agentFirst = true;
	public static ArrayList<String> agentMoves = new ArrayList<>();
	public static ArrayList<String> opponentMoves = new ArrayList<>();
	
	public static void printMenu() {
//		printBoard(board);
		Scanner scanner = new Scanner(System.in);
		System.out.print("Enter the maximum time allowed to generate a move: ");
		timeLimit = scanner.nextInt() * 1000;
		System.out.println("Who goes first? A for agent, O for opponent");
		String firstPlayer = scanner.next();
		if (firstPlayer.equals("A") || firstPlayer.equals("a")) {
			turnCounter = 0;
		}
		else if (firstPlayer.equals("O")  || firstPlayer.equals("o")) {
			turnCounter = 1;
			agentFirst = false;
		}
		else {
			printMenu();
		}
		//scanner.close();
	}
	
	public static void initializeBoard() {
		for (char[] row: board) {
			Arrays.fill(row, '-');
		}
	}
	
	public static void printBoard(char[][] board) {
		char pos = 'A';
		for (int i = 0; i < board.length; ++i) {
			if (i == 0) {
				System.out.print("  ");
				for (int k = 0; k < board[0].length; ++k) {
					System.out.print((k + 1) + " ");
				}
				if (agentFirst) {
					System.out.print("     Agent vs. Opponent");
				}
				else {
					System.out.print("     Opponent vs. Agent");
				}
				System.out.println();
			}
			
			System.out.print(pos++ + " ");
			
			for(int j = 0; j < board[0].length; ++j) {
				System.out.print(board[i][j] + " ");
			}
			
			if (i > 0){
				if (agentFirst){
					if (i <= agentMoves.size()) {
						System.out.print("        "  + i + ". " + agentMoves.get(i-1));
					}
					if (i <= opponentMoves.size()) {
						System.out.print("  " + opponentMoves.get(i-1));
						if (isWinState(board)) {
							System.out.print(" wins." );
						}
					}
					
				}
				else {
					if (i <= opponentMoves.size()) {
						System.out.print("        "  + i + ". " + opponentMoves.get(i-1));
					}
					if (i <= agentMoves.size()) {
						System.out.print("  " + agentMoves.get(i-1));
					}
				}
				if (isWinState(board)) {
					System.out.print(" wins." );
				}
			}
			System.out.println();
		}
		
		for (int i = 7; i <= agentMoves.size() || i <= opponentMoves.size(); ++i) {
			if (agentFirst) {
				if (i < agentMoves.size()) {
					System.out.print("                          " + (i + 1) + ". " + agentMoves.get(i));
					if (isWinState(board)) {
						System.out.print(" wins." );
					}
				}
				if (i < opponentMoves.size()) {
					System.out.print("  " + opponentMoves.get(i));
					if (isWinState(board)) {
						System.out.print(" wins." );
					}
				}
			}
			else {
				if (i < opponentMoves.size()) {
					System.out.print("                          " + (i + 1) + ". " + opponentMoves.get(i));
					if (isWinState(board)) {
						System.out.print(" wins." );
					}
				}
				if (i < agentMoves.size()) {
					System.out.print("  " + agentMoves.get(i));
					if (isWinState(board)) {
						System.out.print(" wins." );
					}
				}
			}
			System.out.println();
		}
	}
	
	public static boolean isWinState(char[][] board) {
		for (int i = 0; i < SIZE - 3; ++i) {
			for (int j = 0; j < SIZE - 3; ++j) {
				if (board[i][j] == '-') {
					continue;
				}
				else {
					if (board[i][j] == board[i][j+1] && board[i][j] == board[i][j+2] && board[i][j]== board[i][j+3]) {
//						System.out.println("Game over. " + board[i][j] + " wins.");
//						System.out.println("j Test");
						return true;
					} 
					else if (board[i][j] == board[i+1][j] && board[i][j] == board[i+2][j] && board[i][j]== board[i+3][j]) {
//						System.out.println("i Test");
//						System.out.println("Game over. " + board[i][j] + " wins.");
						return true;
					}
				}
			}
		}
		return false;
	}
	
	
	public boolean isLegalMove(int i, int j, char[][] board) {
		if (i < board.length && j < board[0].length && board[i][j] == '-') {
			return true;
		}
		return false;
	} 
	
	public static boolean compareMoves(char[][] board, int[] move1, int[] move2, char player) {
		Node temp = new Node(board);
		board[move1[0]][move1[1]] = player;
		int move1Eval = temp.evaluation;
		board[move1[0]][move1[1]] = '-';
		board[move2[0]][move2[1]] = player;
		int move2Eval = temp.evaluation;
		board[move2[0]][move2[1]] = '-';
		if (move1Eval > move2Eval) {
			return true;
		}
		return false;
	}
	
	public static int[] alphaBetaSearch(char[][] game) {
		total = 0;
		depth = 0;
//		System.out.println("ABS test");
		start = System.nanoTime();
		Node board = new Node(game);
		for (int i = 1; i < Integer.MAX_VALUE && total <= timeLimit; ++i) {
			int val = maxValue(board, Integer.MIN_VALUE, Integer.MAX_VALUE, i);
			// COMPARE BEST MOVE TO CURRENT BEST AND UPDATE HERE
			if(compareMoves(board.state, currentBest, best, 'X')) {
//				System.out.println("Test");
				copyArray(best, currentBest);
			}
//			depth++;
//			System.out.println("Depth = " + depth);
		}
		
		// FIND A WAY TO KEEP TRACK OF BEST MOVE AND CURRENT BEST MOVE
		return best;
	}
	
	private static int maxValue(Node board, int alpha, int beta, int distance) {
//		System.out.println("Max Test");
		end = System.nanoTime();
		total = (end - start)/1000000;
//		System.out.println(timeLimit);
//		System.out.println(total);
		if (isWinState(board.state) || depth >= distance|| total >= timeLimit) {
//			System.out.println("Max Return Test");
			return board.evaluation;
		}
		int val = board.evaluation;
//		System.out.println("Max val = " + val);
		while(!board.openPositions.isEmpty()) {
			int[] move = board.openPositions.poll();
//			printArray(move);
			int eval = board.add('X', move);
//			printBoard(board.state);
//			System.out.println("Max eval = " + eval);
			if (eval > curBestEval) {
//				System.out.println("Test");
				copyArray(currentBest, move);
//				printArray(currentBest);
				curBestEval = eval;
			}
			val = Math.max(val, minValue(board, alpha, beta, distance));
			if (val >= beta) {
				copyArray(currentBest, move);
				end = System.nanoTime();
				total = (end - start) / 1000000;
				return val;
			}
			alpha = Math.max(alpha, val);
			end = System.nanoTime();
			total = (end - start) / 1000000;
		}
		depth++;
//		System.out.println("Depth = " + depth);
		return val;
	}
		
	private static int minValue(Node board, int alpha, int beta, int distance) {
//		System.out.println("Min Test");
		end = System.nanoTime();
		total = (end - start) / 1000000;
		if (isWinState(board.state) || depth >= distance || total >= timeLimit) {
			return board.evaluation;
		}
		int val = board.evaluation;
//		System.out.println("Min val = " + val);
		while(!board.openPositions.isEmpty()) {
			int[] move = board.openPositions.poll();
//			printArray(move);
			int eval = board.remove('O', move);
//			printBoard(board.state);
//			System.out.println("Min eval = " + eval);
			if (eval > curBestEval) {
				copyArray(currentBest, move);
//				System.out.println("Test");
//				printArray(currentBest);
				curBestEval = eval;
			}
			val = Math.min(val, maxValue(board, alpha, beta, distance));
			
			if (val <= alpha) {
//				System.out.println("Val = " + val);
				copyArray(currentBest, move);
//				printArray(currentBest);
				end = System.nanoTime();
				total = (end - start) / 1000000;
				return val;
			}
			beta = Math.min(beta, val);
			end = System.nanoTime();
			total = (end - start) / 1000000;
		}
		depth++;
//		System.out.println("Depth = " + depth);
		return val;
	}
	
	private static void copyArray(int[] copyInto, int[] copyFrom) {
		if (copyInto.length == copyFrom.length) {
			for (int i = 0; i < copyInto.length; ++i) {
				copyInto[i] = copyFrom[i];
			}
		}
	}
	
	public static void getAndEnterMove() {
		System.out.println("\nChoose opponent's move: ");
		Scanner scan = new Scanner(System.in);
		String move = scan.next();
		//System.out.println(move.charAt(1));
		int row = (move.charAt(0) - 'a');
		//System.out.println(row);
		int col = Integer.valueOf(move.charAt(1)) - '0' - 1;
		while (row > 7 || col > 7 || row < 0 || col < 0) {
			System.out.println("Illegal move.... Enter a new move");
			System.out.println("\nChoose opponent's move: ");
			move = scan.next();
			row = (move.charAt(0) - 'a');
			col = Integer.valueOf(move.charAt(1)) - '0' - 1;
		}
		while (board[row][col] != '-') {
			System.out.println("Move already taken.... Enter a new move");
			System.out.println("\nChoose opponent's move: ");
			move = scan.next();
			row = (move.charAt(0) - 'a');
			col = Integer.valueOf(move.charAt(1)) - '0' - 1;
		}
		if (board[row][col] == '-') {
			board[row][col] = 'O';
			opponentMoves.add(move);
			printBoard(board);
		}
	}
	
	public static void playMove(int[] move, char player) {
//		System.out.println("Play move test");
		if (board[move[0]][move[1]] == '-') {
			board[move[0]][move[1]] = player;
			char r = (char)(move[0] + 'a');
			String moveMade = r + String.valueOf(move[1] + 1);
//			System.out.println(moveMade);
			agentMoves.add(moveMade);
			printBoard(board);
			System.out.println("\nAgents move is: " + moveMade);
		}
		else {
			if (move[0] > 7 || move[0] < 0 || move[1] > 7 || move[1] < 0) {
				System.out.println("Illegal move.... generating another move");
				Random rand = new Random();
				int row = rand.nextInt(8);
				int col = rand.nextInt(8);
				playMove(new int[] {row, col}, player);
			}
			System.out.println("Move already taken.... generating another move");
			Random rand = new Random();
			int row = rand.nextInt(8);
			int col = rand.nextInt(8);
			playMove(new int[] {row, col}, player);
		}
	}
	
	public static void printArray(int[] arr) {
		for (int i = 0; i < arr.length; ++i) {
			System.out.print(arr[i] + " ");
		}
		System.out.println();
	}

	public static void main(String[] args) {
		initializeBoard();
		printMenu();
		while(!isWinState(board)) {
			if (turnCounter % 2 == 0) {
				if (agentFirst) {
					if (turnCounter <= 2) {
						Random rand = new Random();
						int row = rand.nextInt(8);
						int col = rand.nextInt(8);
						playMove(new int[] {row, col}, 'X');
						turnCounter++;
						continue;
					}
				}
				else {
					if (turnCounter <= 4) {
						Random rand = new Random();
						int row = rand.nextInt(8);
						int col = rand.nextInt(8);
						playMove(new int[] {row, col}, 'X');
						turnCounter++;
						continue;
					}
				}
//				System.out.println("Turn number:" + turnCounter);
				alphaBetaSearch(board);
				playMove(best, 'X');
				turnCounter++;
			}
			else {
//				System.out.println("Turn number:" + turnCounter);
				getAndEnterMove();
				turnCounter++;
			}
		}
	}
}
