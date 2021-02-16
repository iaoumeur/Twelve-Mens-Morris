package player;

import java.util.ArrayList;

public class Move {

	int gameStage = 0;
	int piecePosition = 0;
	ArrayList<Integer> availablePositionsToMove = new ArrayList<Integer>();
	
	public Move(int gameStage, int boardPiece, ArrayList<Integer> availablePositions) {
		this.gameStage=gameStage;
		this.piecePosition = boardPiece;
		this.availablePositionsToMove = availablePositions;
	}
	
	public int getGameStage() {
		return gameStage;
	}
	
	public int getPiecePosition() {
		return piecePosition;
	}
	
	public ArrayList<Integer> getAvailablePositionsToMove() {
		return availablePositionsToMove;
	}
}
