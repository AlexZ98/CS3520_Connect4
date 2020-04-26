import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class Node {	
	public final int BOARD_SIZE = 8;
	public char[][] state = new char[BOARD_SIZE][BOARD_SIZE];
	public int evaluation;
	public Queue<int[]> openPositions = new LinkedList<>();
	public Stack<int[]> movesPlayed = new Stack<>();
	public Node currentGame;
	
	public Node(char[][] game) {
		for (int i = 0; i < BOARD_SIZE; ++i) {
			for (int j = 0; j < BOARD_SIZE; ++j) {
				state[i][j] = game[i][j];
				if (game[i][j] == '-') {
					openPositions.add(new int[] {i, j});
				}
			}
		}
		this.evaluation = evaluation(game);
	}
	
	public int add(char player, int[] move) {
		if (move.length != 2) {
			return (Integer) null;
		}
		movesPlayed.push(move);
//		System.out.println("Stack push test");
		state[move[0]][move[1]] = player;
		int eval = evaluation(state);
		return eval;
	}
	
	public int remove(char player, int[] move) {
		if (move.length != 2) {
			return (Integer) null;
		}
		state[move[0]][move[1]] = player;
		int eval = evaluation(state);
//		int[] toReset = movesPlayed.pop();
//		state[toReset[0]][toReset[1]] = '-';
		movesPlayed.push(move);
		
		return eval;
	}
	
	public int evaluation(char[][] board) {
		int eval = 0;
		for (int i = 0; i < BOARD_SIZE - 3; ++i) {
			for (int j = 0; j < BOARD_SIZE - 3; ++j) {
				if (board[i][j] != '-') {
					if (board[i][j] == board[i][j+1] && board[i][j] == board[i][j+2] && board[i][j]== board[i][j+3]) {
						if (board[i][j] == 'X') {
							return Integer.MAX_VALUE;
						}
						else {
							return Integer.MIN_VALUE;
						}
					}
					else if (board[i][j] == board[i][j+1] && board[i][j] == board[i][j+2]) {
						if (board[i][j] == 'X') {
							eval += 9;
						}
						else {
							eval -= 9;
						}
						j += 2;
					}
					else if (board[i][j] == board[i][j+1]) {
						if (board[i][j] == 'X') {
							eval += 4;
						}
						else {
							eval -= 4;
						}
						j += 1;
					}
					else {
						if (board[i][j] == 'X') {
							eval += 1;
						}
						else {
							eval -= 1;
						}
					}
					if (board[i][j] == board[i+1][j] && board[i][j] == board[i+2][j] && board[i][j] == board[i+3][j]) {
						if (board[i][j] == 'X') {
							return Integer.MAX_VALUE;
						}
						else {
							return Integer.MIN_VALUE;
						}
					}
					else if (board[i][j] == board[i+1][j] && board[i][j] == board[i+2][j]) {
						if (board[i][j] == 'X') {
							eval += 9;
						}
						else {
							eval -= 9;
						}
						i += 2;
					}
					else if (board[i][j] == board[i+1][j]) {
						if (board[i][j] == 'X') {
							eval += 4;
						}
						else {
							eval -= 4;
						}
						i += 1;
					}
					else {
						if (board[i][j] == 'X') {
							eval += 1;
						}
						else {
							eval -= 1;
						}
					}
				}
			}
		}
		return eval;
	}
	
	private boolean compareArrays(int[] arr1, int[] arr2) {
		if (arr1.length == arr2.length) {
			for (int i = 0; i < arr1.length; ++i) {
				if (arr1[i] != arr2[i]) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
}
