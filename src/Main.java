import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class Main extends Application
{

	private static VBox initialPlayerVBox;
	private static Label intialPlayerLabel;
	private static RadioButton computerFirst;
	private static RadioButton playerFirst;
	private static Label timeLabel;
	private static TextField time;
	private int COLUMNS = 8, ROWS = 8;

	private static VBox mainMenu;
	Button start;

	private static GridPane gameRegion;

	Scene menuScene;
	Scene gameScene;

	private static boolean playerTurn = true;

	private static alphaBetaPruning ai;


	public static void main(String args[])
	{
		launch(args);
	}

	public void start(Stage primaryStage)
	{
		// Initial menu for settings
		intialPlayerLabel = new Label("Who goes first?");
		intialPlayerLabel.setPadding(new Insets(5));
		computerFirst = new RadioButton("Computer");
		playerFirst = new RadioButton("Player");
		ToggleGroup tgFirstPlayer = new ToggleGroup();
		computerFirst.setToggleGroup(tgFirstPlayer);
		playerFirst.setToggleGroup(tgFirstPlayer);
		ai = new alphaBetaPruning();
		boolean playing = true;

		initialPlayerVBox = new VBox(5, intialPlayerLabel, computerFirst, playerFirst);
		initialPlayerVBox.setAlignment(Pos.CENTER);

		start = new Button("Start Game");


		start.setOnAction(event ->{
			if(!computerFirst.isSelected() && !playerFirst.isSelected())
				computerFirst.setSelected(true);
			int cutOffTime = 5;
			primaryStage.setScene(gameScene);
		});

		mainMenu = new VBox(50, initialPlayerVBox, start);
		mainMenu.setAlignment(Pos.CENTER);

		menuScene = new Scene(mainMenu, 500, 500);
		menuScene.getStylesheets().add("menustyle.css");

		//Fill the scene with regions to represent each possible spot
		gameRegion = new GridPane();

		for(int i = 0; i<ROWS; i++)
		{
			for(int j=0; j<COLUMNS; j++)
			{
				Pane region = new Pane();
				region.setMinSize(60, 60);
				region.setStyle("-fx-border-color: #000000");
				region.setOnMouseClicked(event -> {
					if(playerTurn)
					{
						Circle circle = new Circle(30, 30, 20);
						circle.setFill(Color.BLUE);
						region.getChildren().add(circle);

					}
				});

				gameRegion.add(region, i, j);
			}
		}
		gameRegion.setPadding(new Insets(10));

		gameScene = new Scene(gameRegion, 500, 500);
		primaryStage.setTitle("Connect4");
		primaryStage.setScene(menuScene);
		primaryStage.show();
	}
}


/*import java.util.Scanner;

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
*/