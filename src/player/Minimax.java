package player;

import java.util.ArrayList;

import gui.Game;
import state.GameState;

public class Minimax {
	
	Game game;
	GameState state;
	GameState copyState;
	
	public Minimax(Game game, GameState state) {
		this.game = game;
		this.state = state;
	}
	
	public void setCopyState(GameState newState) {
		this.copyState = newState;
	}
	
	public void makeMove() {
		
		int bestScore = -10000;
		int bestMoveIndex = 0;
		
		ArrayList<Move> validMoves = findValidMoves("black"); 
		
		String temp = null;
		String[] tempBoardPieces = new String[state.getBoardPieces().length];
		for(int i=0; i<state.getBoardPieces().length; i++) {
			tempBoardPieces[i] = state.getBoardPiece(i);
		}
		
		for(int i=0; i<validMoves.size(); i++) {
			Move move = validMoves.get(i);
			if(move.getGameStage()==1) {
				//temp = state.getBoardPiece(move.getPiecePosition());
				state.setBoardPiece(move.getPiecePosition(), "black");
				int score = state.evaluateState("black");
				System.out.println("Valid move: " + i + ", Score: " + score);
				if(score>bestScore) {
					bestMoveIndex = i;
					bestScore = score;
				}
			}
			//state.setBoardPiece(move.getPiecePosition(), temp);
			state.setBoardPieces(tempBoardPieces);
	
			
		}
		
		System.out.println("Best move index: " + bestMoveIndex);
		Move bestMove = validMoves.get(bestMoveIndex);
		if(bestMove.getGameStage()==1) {
			state.boardMouseClick(bestMove.getPiecePosition());
			game.getBoard().repaintPieces();
			game.switchTurn();
		}
		
		
		
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
					System.out.print("Stage: 2, Piece Position: " + i + " Valid positions: ");
					state.showAvailablePositionsToMove(i);
					if(!state.getMovablePositions().isEmpty()) {
						validMoves.add(new Move(2, i, state.getMovablePositions()));	
						for(int j=0; j<state.getMovablePositions().size(); j++) {
							System.out.print(state.getMovablePositions().get(j) + ", ");
						}
						state.resetMovablePositions();
					}
					System.out.println();
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
