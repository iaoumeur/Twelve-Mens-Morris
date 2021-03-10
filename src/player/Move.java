package player;

import java.util.ArrayList;

public class Move {

	int gameStage = 0;
	int piecePosition = 0;
	public int to = 0;
	
	public Move(int gameStage, int boardPiece, int to) {
		this.gameStage=gameStage;
		this.piecePosition = boardPiece;
		this.to = to;
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
	
}
