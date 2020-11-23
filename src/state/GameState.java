package state;

public class GameState {

	public final int numberOfPieces = 12;
	public final int totalNumberOfPieces = 24;
	
	private String[] boardPieces;
	private int[][] millLocations = {{0,1,2}, {3,4,5}, {6,7,8}, {9,10,11}, {12,13,14}, {15,16,17}, {18,19,20}, {21,22, 23},
			{0,9,21}, {3,10,18}, {6,11,15}, {1,4,7}, {16,19,22}, {8,12,17}, {5,13,20}, {2,14,23}, {0,3,6}, {2,5,8}, {21,18,15},
			{23,20,17}
	};
	private String[] millsFound = new String[millLocations.length];
	
	//1 = phase 1 - placing stage
	//2 = phase 2 - movement stage
	//3 = phase 3 - "flying" movement stage
	//4 = mill created - remove opponent's piece
	//5 = game end - draw, or player wins
	private int gameStage = 1; 
	
	private String turn = "white";
	
	public GameState() {
		boardPieces = new String[totalNumberOfPieces];
		for(int i=0; i<totalNumberOfPieces; i++) {
			boardPieces[i] = null;
		}
	}
	
	public int[][] getMillLocations() {
		return millLocations;
	}
	
	public String[] getMillsFound() {
		return millsFound;
	}
	
	public int getGameStage() {
		return gameStage;
	}
	
	public void setGameStage(int stage) {
		gameStage = stage;
	}
	
	public String getTurn() {
		return turn;
	}
	
	public void setTurn(String newTurn) {
		turn = newTurn;
	}
	
	public String[] getBoardPieces() {
		return boardPieces;
	}

	public void setBoardPieces(String[] pieces) {
		boardPieces = pieces;
	}
	public void setBoardPiece(int index, String value) {
		boardPieces[index] = value;
	}
	
	public boolean checkForMill() {
		for(int i=0; i<millLocations.length; i++) {
			if(boardPieces[millLocations[i][0]]=="white" && boardPieces[millLocations[i][1]]=="white" && boardPieces[millLocations[i][2]]=="white") {
				if(millsFound[i]==null) {
					//new mill
					millsFound[i] = "white";
					return true;
				}
				else {
					//already existing mill
				}
			}
			if(boardPieces[millLocations[i][0]]=="black" && boardPieces[millLocations[i][1]]=="black" && boardPieces[millLocations[i][2]]=="black") {
				if(millsFound[i]==null) {
					//new mill
					millsFound[i] = "black";
					return true;
				}
				else {
					//already existing mill
				}
			}
		}
		
		return false;
		
	}
	
	public boolean inMill(int piecePosition) {
		for(int i=0; i<millLocations.length; i++) {
			for(int j=0; j<millLocations[i].length; j++) {
				if(piecePosition==millLocations[i][j]) {
					if(millsFound[i]!=null) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
}
