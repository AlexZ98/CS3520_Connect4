import java.util.Optional;
import java.util.Random;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
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
	private static AlphaBetaPruning ai;
	
	private static int maxTime;
	private static boolean onGoing;
	
	private static VBox initialPlayerVBox;
	private static Label intialPlayerLabel;
	private static RadioButton computerFirst;
	private static RadioButton playerFirst;
	
	private static VBox mainMenu;
	private static Button start;
	
	private static GridPane gameRegion;
	
	private static Slot[][] slots;
	
	private static Scene menuScene;
	private static Scene gameScene;
	
	private static boolean playerTurn = true;
	private static int playerRow;
	private static int playerCol;
	private static boolean selected;
	
	private static int aiRow;
	private static int aiCol;
	CustomNode currentState;

	
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
		
		initialPlayerVBox = new VBox(5, intialPlayerLabel, computerFirst, playerFirst);
		initialPlayerVBox.setAlignment(Pos.CENTER);
		
		start = new Button("Start Game");
		
		start.setOnAction(event ->{
			if(playerFirst.isSelected())
				playerTurn = true;
			else
				playerTurn = false;
			
			
			if(!computerFirst.isSelected() && !playerFirst.isSelected())
				playerTurn = false;
				
			primaryStage.setScene(gameScene);
			
			currentState = new CustomNode(playerTurn);
			if(!playerTurn)
			{
				CustomNode aiNode = ai.makeMove(currentState, 10);
				aiCol = aiNode.getCol();
				aiRow = aiNode.getRow();
				currentState = aiNode;
				slots[aiCol][aiRow].getChildren().get(0).setStyle("-fx-fill: red");
				slots[aiCol][aiRow].getChildren().get(0).setVisible(true);
				System.out.println(currentState);
			}
			maxTime = 3;
			onGoing = true;
		});
		
		mainMenu = new VBox(50, initialPlayerVBox, start);
		mainMenu.setAlignment(Pos.CENTER);
		
		menuScene = new Scene(mainMenu, 500, 500);
		menuScene.getStylesheets().add("menustyle.css");
		
		//Fill the scene with regions to represent each possible spot
		gameRegion = new GridPane();
		
		slots = new Slot[8][8];
		ai = new AlphaBetaPruning();
		
		for(int i = 0; i<8; i++)
		{
			for(int j=0; j<8; j++)
			{
				Slot region = new Slot();
				region.setMinSize(60, 60);
				region.setStyle("-fx-border-color: #000000");
				Circle circle = new Circle(30, 30, 20);
				circle.setStyle("-fx-fill: blue");
				region.getChildren().add(circle);
				circle.setVisible(false);
				
				region.setOnMouseClicked(event -> {
					if(!region.getChildren().get(0).isVisible())
					{
						selected = true;
						region.getChildren().get(0).setVisible(true);
						region.setPlayer(true);
						playerRow = region.getRow();
						playerCol = region.getCol();
						currentState = playerMove(currentState);
						System.out.println(currentState);
					}
					if(!currentState.terminalTest())
					{
						CustomNode aiNode = ai.makeMove(currentState, 10);
						aiCol = aiNode.getCol();
						aiRow = aiNode.getRow();
						currentState = aiNode;
						slots[aiCol][aiRow].getChildren().get(0).setStyle("-fx-fill: red");
						slots[aiCol][aiRow].getChildren().get(0).setVisible(true);
						System.out.println(currentState);
					}
					if(currentState.terminalTest())
					{
						Alert gameWin = new Alert(AlertType.INFORMATION);
						gameWin.setContentText("Game is over!");
						gameWin.showAndWait();
						primaryStage.close();
					}
					
				});
				region.setCol(i);
				region.setRow(j);
				gameRegion.add(region, i, j);
				slots[i][j] = region;
			}
		}
		gameRegion.setPadding(new Insets(10));
		
		gameScene = new Scene(gameRegion, 500, 500);
		primaryStage.setTitle("Connect4");
        primaryStage.setScene(menuScene);
        primaryStage.show();
	}
	
	private Node getNode(int col, int row) 
	{
	    for (Node node : gameRegion.getChildren()) 
	    {
	        if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) 
	        {
	            return node;
	        }
	    }
	    return null;
	}
	
	private boolean winCondition()
	{
		for(int row = 0; row<8; row++)
		{
			int counter = 1;
			boolean mark = slots[row][0].isPlayer();
			for(int col = 1; col<8; col++)
			{
				if(slots[row][col].isPlayer() == mark)
					counter++;
				else 
				{
					mark = slots[row][col].isPlayer();
					counter = 1;
				}
				if(!mark && counter >= 4)
					return true;
			}
		}
		
		for(int col = 0; col<8; col++)
		{
			int counter = 1;
			boolean mark = slots[0][col].isPlayer();
			for(int row =1; row<8; row++)
			{
				if(slots[row][col].isPlayer() == mark)
					counter++;
				else
				{
					mark = slots[row][col].isPlayer();
					counter = 1;
				}
				if(mark && counter >= 4)
					return true;
			}
		}
		return false;
	}
	
	private static CustomNode playerMove(CustomNode state) {
        CustomNode newState = null;
        while ( newState == null )
        {
        	if(selected)
        	{
        		newState = state.mark(playerRow, playerCol);
        	}
        }
        return newState;
    }
}
