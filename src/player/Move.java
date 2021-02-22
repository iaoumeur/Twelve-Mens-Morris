package player;

import java.util.ArrayList;

public class Move {

	int gameStage = 0;
	int piecePosition = 0;
	int to = 0;
	ArrayList<Integer> availablePositionsToMove = new ArrayList<Integer>();
	
	public Move(int gameStage, int boardPiece, int to, ArrayList<Integer> availablePositions) {
		this.gameStage=gameStage;
		this.piecePosition = boardPiece;
		this.to = to;
		this.availablePositionsToMove = availablePositions;
	}
	
	public int getGameStage() {
		return gameStage;
	}
	
	public int getPiecePosition() {
		return piecePosition;
	}
	
	public int getTo() {
		return to;
	}
	
	public ArrayList<Integer> getAvailablePositionsToMove() {
		return availablePositionsToMove;
	}
}
