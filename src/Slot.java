import javafx.scene.layout.Pane;

public class Slot extends Pane
{
	public boolean playerSlot;
	public int row;
	public int col;
	
	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}
	
	public boolean isPlayer()
	{
		return playerSlot;
	}
	
	public void setPlayer(boolean status)
	{
		playerSlot = status;
	}

}
