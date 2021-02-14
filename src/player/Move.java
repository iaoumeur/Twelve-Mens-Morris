package player;

import java.util.ArrayList;

public class Move {

	int gameStage = 0;
	int boardPiece = 0;
	ArrayList<Integer> availablePositions = new ArrayList<Integer>();
	
	public Move(int gameStage, int boardPiece, ArrayList<Integer> availablePositions) {
		this.gameStage=gameStage;
		this.boardPiece = boardPiece;
		this.availablePositions = availablePositions;
	}
}
