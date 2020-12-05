package state;

import java.util.ArrayList;

public class GameState {

	public final int numberOfPieces = 12;
	public final int totalNumberOfPieces = 24;
	private int piecesPlaced = 0;
	
	private String[] boardPieces;
	private int[][] millLocations = {{0,1,2}, {3,4,5}, {6,7,8}, {9,10,11}, {12,13,14}, {15,16,17}, {18,19,20}, {21,22, 23},
			{0,9,21}, {3,10,18}, {6,11,15}, {1,4,7}, {16,19,22}, {8,12,17}, {5,13,20}, {2,14,23}, {0,3,6}, {2,5,8}, {21,18,15},
			{23,20,17}
	};
	private int[][] adjacentPositions = {{1,3,9}, {0,2,4}, {1,5,14}, {0,4,6,10}, {1,3,5,7}, {2,4,8,13}, {3,7,11}, {4,6,8}, {5,7,12}, 
			{0,10,21}, {3,9,11,18}, {6,10,15}, {8,13,17}, {5,12,14,20}, {2,13,23}, {11,16,18}, {15,17,19}, {12,16,20}, {10,15,19,21}, 
			{16,18,20,22}, {13,17,19,23}, {9,18,22}, {19,21,23}, {14,20,22}
	};
	private ArrayList<Integer> movablePositions = new ArrayList<Integer>();
	private ArrayList<Integer> emptySpaces = new ArrayList<Integer>();
	private String[] millsFound = new String[millLocations.length];
	
	//1 = phase 1 - placing stage
	//2 = phase 2 - movement stage
	//3 = valid piece selected to move adjacently 
	//4 = mill created - remove opponent's piece
	//5 = game end - draw, or player wins
	private int gameStage = 1; 
	private String turn = "white";
	private int selectedPiece = -1;
	private boolean flyingWhite = false;
	private boolean flyingBlack = false;
	private String endgame;
	
	public GameState() {
		boardPieces = new String[totalNumberOfPieces];
		for(int i=0; i<totalNumberOfPieces; i++) {
			boardPieces[i] = null;
		}
	}
	
	public boolean getFlyingWhite() {
		return flyingWhite;
	}
	
	public boolean getFlyingBlack() {
		return flyingBlack;
	}
	
	public int[][] getMillLocations() {
		return millLocations;
	}
	
	public int[][] getAdjacentPositions() {
		return adjacentPositions;
	}
	
	public ArrayList<Integer> getMovablePositions() {
		return movablePositions;
	}
	
	public void appendMovablePositions(int position) {
		movablePositions.add(position);
	}
	
	public void resetMovablePositions() {
		movablePositions.clear();
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
	
	public void switchTurn() {
		if(turn=="white") { 
			turn = "black";
		}
		else if(turn=="black") {
			turn = "white";
		}
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
	
	public int getSelectedPiece() {
		return selectedPiece;
	}
	
	public void setSelectedPiece(int piece) {
		selectedPiece = piece;
	}
	
	
	public boolean checkForMill() {
		for(int i=0; i<millLocations.length; i++) {
			if(boardPieces[millLocations[i][0]]=="white" && boardPieces[millLocations[i][1]]=="white" && boardPieces[millLocations[i][2]]=="white") {
				if(millsFound[i]==null) {
					millsFound[i] = "white";
					gameStage = 4;   
					return true;
				}
			}
			else if(boardPieces[millLocations[i][0]]=="black" && boardPieces[millLocations[i][1]]=="black" && boardPieces[millLocations[i][2]]=="black") {
				if(millsFound[i]==null) {
					millsFound[i] = "black";
					gameStage = 4;   
					return true;
				}
			}
			else {
				millsFound[i] = null;
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
	
	public String boardMouseClick(int piecePosition) {
		
		if(piecePosition==-1 && gameStage!=3) {
			return "invalidClick";
		}
		
		switch(gameStage) {
		
		case 1:
			if(boardPieces[piecePosition]==null) {
				boardPieces[piecePosition] = turn;
	    		piecesPlaced++;
				if(checkForMill()) {
					if(!canPieceBeRemoved()) {
			       		return "millNoRemoval";
			       	}    	 
					resetMovablePositions();
			        return turn + "PlacedMill";
				}
				else if(piecesPlaced>=totalNumberOfPieces){
					endgame = hasGameEnded();
					if(endgame!=null) {
						return endgame;
					}
					gameStage=2;
				}
				return turn + "Placed";
			}
			break;
			
		case 2:
			if((turn=="white" && boardPieces[piecePosition]=="white") || (turn=="black" && boardPieces[piecePosition]=="black")) {
				gameStage = 3;
				selectedPiece = piecePosition;
        		showAvailablePositionsToMove(piecePosition);
				return "validPieceSelected";
			}
			else {
				return "invalidPieceSelected";
			}
			
		case 3:
			if(piecePosition==-1) {
				gameStage = 2;
				selectedPiece = -1;
        		resetMovablePositions();
				return "resetPieceSelected";
			}
			if(movablePositions.contains(piecePosition)) {
				boardPieces[selectedPiece] = null;
				boardPieces[piecePosition] = turn;
				if(checkForMill()) {
					if(!canPieceBeRemoved()) {
			       		return "millNoRemoval";
			       	}    	 
			        return turn + "PlacedMill";
				}
				endgame = hasGameEnded();
				if(endgame!=null) {
					return endgame;
				}
				gameStage = 2;
				selectedPiece = -1;
        		resetMovablePositions();
				return "pieceMoved";
			}
			break;
			
		case 4:
			if(inMill(piecePosition) && canPieceBeRemoved()) {
    			return "invalidRemoval";
    		}
    		if((turn=="white" && boardPieces[piecePosition]=="black") || (turn=="black" && boardPieces[piecePosition]=="white"))  {
    			boardPieces[piecePosition] = null;
    			checkForMill();
    			if(piecesPlaced<totalNumberOfPieces) {
    				gameStage = 1;
    			}
    			else if(hasGameEnded()!=null) {
					return "end";
    			}
    			else {
    				gameStage = 2;
    			}
    			return turn + "Removal";
    		}		
    		break;
		}
		
		return "invalid";
	}


	private String hasGameEnded() {
		
		if(checkForDraw()) {
			return "draw";
		}
		String winner = checkForWin();
		if(winner!=null) {
			return winner;
		}
		
		return null;
		
	}
	private boolean canPieceBeRemoved() {
		
		for(int i=0; i<boardPieces.length; i++) {
			if(boardPieces[i]!=null) {
				if(!inMill(i)) {
					return true;
				}
			}
		}
		return false;
		
	}
	private boolean checkForDraw() {
		
		if(gameStage==1) {
			for(int i=0; i<boardPieces.length; i++) {
				if(boardPieces[i]==null) {
					return false;
				}
			}
			return true;
		}
		
		return false;
		
   	 
	}
	
	private String checkForWin() {
		
		int whitePieces = 0;
		int blackPieces = 0;
		
		for(int i=0; i<boardPieces.length; i++) {
			if(boardPieces[i]=="white")
				whitePieces++;
			else if(boardPieces[i]=="black") 
				blackPieces++;
		}
		if(whitePieces==3) {
			flyingWhite = true;
		}
		if(blackPieces==3) {
			flyingBlack = true;
		}
		if(whitePieces==2) 
			return "black";
		else if(blackPieces==2) 
			return "white";
		
		String otherTurn;
		if(turn=="white") {
			otherTurn="black";
			if(flyingBlack) {
				return null;
			}
		}
		else {
			otherTurn = "white";
			if(flyingWhite) {
				return null;
			}
		}
		
		for(int i=0; i<boardPieces.length; i++) {
			if(boardPieces[i]==otherTurn) {
				if(checkForValidMove(i)) 
					return null;
			}	
		}
		
		//returns the winner
		return turn;
		
	}

	private boolean checkForValidMove(int piece) {
		
		for(int i=0; i<adjacentPositions[piece].length; i++) {
			if(boardPieces[adjacentPositions[piece][i]]==null) {
				return true;
			}
		}
		return false;
		
	}
	
	private void showAvailablePositionsToMove(int piecePosition) {
		if((flyingWhite && turn=="white") || (flyingBlack && turn=="black")) {
			emptySpaces = getEmptySpaces();
			for(int i=0; i<emptySpaces.size(); i++) {
				appendMovablePositions(emptySpaces.get(i));
			}
		}
		int size = adjacentPositions[piecePosition].length;
		for(int i=0; i<size; i++) {
			if(boardPieces[adjacentPositions[piecePosition][i]]==null) {
				appendMovablePositions(adjacentPositions[piecePosition][i]);
			}
		}
		
	}
	
	public ArrayList<Integer> getEmptySpaces() {
		emptySpaces.clear();
		for(int i=0; i<boardPieces.length; i++) {
			if(boardPieces[i]==null) {
				emptySpaces.add(i);
			}
		}
		return emptySpaces;
	}


	
	
}
