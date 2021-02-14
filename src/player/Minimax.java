package player;

import java.util.ArrayList;

import state.GameState;

public class Minimax {
	
	GameState state;
	
	public Minimax(GameState state) {
		this.state = state;
	}
	
	public void makeMove() {
		
	}
	
	public ArrayList<Move> findValidMoves(String player) {
		
		String otherTurn;
		if(player=="black") {
			otherTurn = "white";
		}
		else {
			otherTurn = "black";
		}
		ArrayList<Move> validMoves = new ArrayList<Move>();
		
		if(state.getGameStage()==1) {
			
			for(int i=0; i<state.getBoardPieces().length; i++) {
				if(state.getBoardPieces()[i]==null) {
					validMoves.add(new Move(1, i, null));
				}
			}
		}
		else if(state.getGameStage()==2) {
			
			for(int i=0; i<state.getBoardPieces().length; i++) {
				if(state.getTurn()==player && state.getBoardPieces()[i]==player) {
					state.showAvailablePositionsToMove(i);
					if(!state.getMovablePositions().isEmpty()) {
						validMoves.add(new Move(2, i, state.getMovablePositions()));						
						state.resetMovablePositions();
					}
				}
			}	
			
		}
		else if(state.getGameStage()==4) {
			
			for(int i=0; i<state.getBoardPieces().length; i++) {
				
				if(state.inMill(i) && state.canPieceBeRemoved()) {
	    			continue;
	    		}
				if((state.getTurn()==player && state.getBoardPieces()[i]==otherTurn))  {
					validMoves.add(new Move(4, i, null));
				}
				
				
			}
		}
		
		return validMoves;
	}

}
