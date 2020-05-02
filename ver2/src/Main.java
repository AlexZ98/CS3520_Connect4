import java.util.Scanner;

public class Main {
    private static Scanner sc;
    private static AlphaBetaPruning ai;

    public static void main(String[] args) {
        System.out.println("Connect Four Game\n");
        sc = new Scanner(System.in);
        ai = new AlphaBetaPruning();
        boolean onGoing = true;
        while ( onGoing ){
            boolean playerTurn = firstOrSecond("Would you like to go first (y/n)? ");
            Node currentState = new Node(playerTurn);
            System.out.print("Amount of time AI takes to make a move (in seconds)? ");
            int cutOffTime = Integer.parseInt(sc.nextLine());
            System.out.println(currentState);
            while ( !currentState.terminalTest() ){
                if ( playerTurn ){
                    currentState = playerMove(currentState);
                }
                else {
                    currentState = ai.makeMove(currentState, cutOffTime);
                }
                System.out.println("\n" + currentState);
                playerTurn = !playerTurn;
            }
            System.out.print("Game Over. ");
            System.out.println( playerTurn ? "AI Win!" : "You Win!");
            onGoing = firstOrSecond("Would you like to play again (y/n)? ");
        }
    }

    private static Node playerMove(Node state) {
        Node newState = null;
        while ( newState == null ){
            System.out.print("Choose your next move: ");
            String userMove = sc.nextLine();
            newState = state.mark(userMove);
        }
        return newState;
    }

    private static boolean firstOrSecond( String prompt ){
        String turn = "";
        do {
            System.out.print(prompt);
            turn = sc.nextLine();
        } while ( !turn.equalsIgnoreCase("y") && !turn.equalsIgnoreCase("n") );
        return turn.equalsIgnoreCase("y");
    }

}
