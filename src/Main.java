import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

	private static VBox initialPlayerVBox;
	private static Label intialPlayerLabel;
	private static RadioButton computerFirst;
	private static RadioButton playerFirst;
	
	private static VBox mainMenu;
	Button start;
	
	private static GridPane gameRegion;

	Scene menuScene;
	Scene gameScene;
	
	private static boolean playerTurn = true;

	
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
			if(!computerFirst.isSelected() && !playerFirst.isSelected())
				computerFirst.setSelected(true);
			
			primaryStage.setScene(gameScene);
		});
		
		mainMenu = new VBox(50, initialPlayerVBox, start);
		mainMenu.setAlignment(Pos.CENTER);
		
		menuScene = new Scene(mainMenu, 500, 500);
		menuScene.getStylesheets().add("menustyle.css");
		
		//Fill the scene with regions to represent each possible spot
		gameRegion = new GridPane();
		
		for(int i = 0; i<8; i++)
		{
			for(int j=0; j<8; j++)
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
