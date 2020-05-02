import java.util.ArrayList;
import java.util.List;

public class Node {

    public static final int SIZE = 8;
    private int[][] board;
    private int currentMark;
    private String boardOutput = null;

    public Node( boolean playerTurn ){
        board = new int[SIZE][SIZE];
        this.currentMark = playerTurn ? 2 : 1;
    }

    private Node(int[][] newGrid, int mark) {
        if ( newGrid.length != SIZE || newGrid[0].length != SIZE ){
            throw new RuntimeException("Invalid grid size.");
        }
        currentMark = mark;
        board = newGrid;
    }

    public Node mark( String coordinate ){
        if ( coordinate.length() != 2 || !Character.isAlphabetic(coordinate.charAt(0))
                || !Character.isDigit(coordinate.charAt(1)) ){
            return null;
        }
        else {
            return mark( Character.toUpperCase(coordinate.charAt(0))-65, Integer.parseInt("" + coordinate.charAt(1))-1);
        }
    }

    private Node mark( int row, int col ){
        if ( board[row][col] != 0 ){
            return null;
        }
        else {
            int[][] newGrid = board.clone();
            for ( int i = 0; i < SIZE; i++){
                newGrid[i] = board[i].clone();
            }
            newGrid[row][col] = currentMark;
            int newMark = currentMark%2+1;
            return new Node(newGrid, newMark);
        }
    }

    public List<Node> getSuccessors(){
        List<Node> successors = new ArrayList<Node>();
        for ( int row = 0; row < SIZE; row++ ){
            for ( int col = 0; col < SIZE; col++ ){
                if ( board[row][col] == 0 ){
                    successors.add(this.mark(row, col));
                }
            }
        }
        return successors;
    }

    public boolean terminalTest(){
        for ( int row = 0; row < SIZE; row++){
            int counter = 1;
            int mark = board[row][0];
            for ( int col = 1; col < SIZE; col++ ){
                if ( board[row][col] == mark ){
                    counter++;
                } else {
                    mark = board[row][col];
                    counter = 1;
                }
                if ( mark != 0 && counter >= 4 ){
                    return true;
                }
            }
        }

        for ( int col = 0; col < SIZE; col++ ){
            int counter = 1;
            int mark = board[0][col];
            for ( int row = 1; row < SIZE; row++ ){
                if ( board[row][col] == mark ){
                    counter++;
                } else {
                    mark = board[row][col];
                    counter = 1;
                }
                if ( mark != 0 && counter >= 4 ){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String toString(){
        if ( boardOutput == null ){
            StringBuilder sb = new StringBuilder();
            sb.append("   ");
            for ( int i = 1; i <= SIZE; i++){
                sb.append(i + "  ");
            }
            sb.append("\n");
            for (int letter = 65, i = 0; i < SIZE; i++,letter++){
                sb.append((char)letter + " ");
                for ( int j = 0; j < SIZE; j++ ){
                    if ( board[i][j] == 0 ){
                        sb.append(" - ");
                    } else if ( board[i][j] == 1 ){
                        sb.append(" X " );
                    } else if ( board[i][j] == 2 ){
                        sb.append(" O " );
                    }
                }
                sb.append("\n");
            }
            boardOutput = sb.toString();
        }
        return boardOutput;
    }

    public int[][] getState() {
        return board;
    }

}
