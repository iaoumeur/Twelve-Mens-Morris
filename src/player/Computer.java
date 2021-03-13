package player;

import java.util.ArrayList;
import java.util.Stack;

import gui.Game;
import state.GameState;

public abstract class Computer {

	Game game;
	GameState state;
	GameState copyState;
	GameState tempState;
	Stack<GameState> states = new Stack<GameState>();
	
	int nodesEvaluated = 0;
	boolean millCreated = false;
	
	public Computer(Game game, GameState state) {
		this.game = game;
		this.state = state;
	}
	
	public void setCopyState(GameState newState) {
		this.copyState = newState;
		states.push(state);
	}
	
	public void simulateMove(Move move, String player) {
		
		if(move.getGameStage()==1) {
			copyState.setBoardPiece(move.getPiecePosition(), player);
			//copyState.printBoardPieces();
			alterGame(1, player, copyState);
		}
		else if(move.getGameStage()==2) {
			copyState.setBoardPiece(move.getPiecePosition(), null);
			copyState.setBoardPiece(move.getTo(), player);
			//copyState.printBoardPieces();
			alterGame(2, player, copyState);
		}
		else if(move.getGameStage()==4) {
			copyState.setBoardPiece(move.getPiecePosition(), null);
			//copyState.printBoardPieces();
			alterGame(4, player, copyState);
		}
		
	}

	public void alterGame(int gameStage, String player, GameState gameState) {
		
		if(gameStage==1) {
			if(player=="white") {
				gameState.whitePiecesPlaced++;
			}
			else {
				gameState.blackPiecesPlaced++;
			}
			gameState.piecesPlaced++;
			if(gameState.checkForMill()) {
				//System.out.println("***** THIS MOVE MADE A MILL *****");
			}
			else {
				copyState.switchTurn();
				if(gameState.piecesPlaced>=gameState.totalNumberOfPieces){
					gameState.setGameStage(2);
					gameState.phase = 2;
				}	
			}
		}
		else if(gameStage==2) {
			if(gameState.checkForMill()) {
			}
			else {
				gameState.incrementMovesWihtoutMill();
				copyState.switchTurn();
				gameState.setGameStage(2);
			}
			
    		gameState.resetMovablePositions();
		}
		else if(gameStage==4) {
			gameState.checkForMill();
			if(gameState.piecesPlaced<gameState.totalNumberOfPieces) {
				gameState.setGameStage(1);
			}
			else {
				gameState.setGameStage(2);
			}
			copyState.switchTurn();
			gameState.resetMovablePositions();
		}
		
		
	}
	
	public void makeMove(MoveScore move, String player) {
		state.evaluateState(player, true);
		nodesEvaluated = 0;
		if(state.getGameStage()==1) {
			state.setBoardPiece(move.index, player);
			alterGame(1, player, state);
			state.evaluateState(player, true);
			if(player=="white") {
				game.removeWhitePieceFromPanel();
			}
			else {
				game.removeBlackPieceFromPanel();
			}
			if(state.getGameStage()==4) {
				//System.out.println("COMPUTER MADE A MILL");
				millCreated = true;
				return;
			}
		}
		else if(state.getGameStage()==2) {
			state.setBoardPiece(move.index, null);
			state.setBoardPiece(move.to, player);
			alterGame(2, player, state);
			state.evaluateState(player, true);
			if(state.getGameStage()==4) {
				millCreated = true;
				return;
			}
		}
		else if(state.getGameStage()==4) {
			state.setBoardPiece(move.index, null);
			alterGame(4, player, state);
			state.evaluateState(player, true);
		}
		
		String endgame = state.hasGameEnded();
		if(endgame=="white") {
    		game.displayMessage(3);
    		game.getState().setGameStage(5);
    	}
    	else if(endgame=="black") {
    		game.displayMessage(4);
    		game.getState().setGameStage(5);
    	}
    	else if(endgame=="draw") {
    		game.displayMessage(5);
    		game.getState().setGameStage(5);
    	}
		millCreated = false;
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
		
		if(copyState.getGameStage()==1) {
			
			for(int i=0; i<copyState.getBoardPieces().length; i++) {
				if(copyState.getBoardPieces()[i]==null) {
					validMoves.add(new Move(1, i, -1));
				}
			}
		}
		else if(copyState.getGameStage()==2) {
			
			for(int i=0; i<copyState.getBoardPieces().length; i++) {
				if(copyState.getTurn()==player && copyState.getBoardPieces()[i]==player) {
					copyState.showAvailablePositionsToMove(i);
					if(!copyState.getMovablePositions().isEmpty()) {
						for(int j=0; j<copyState.getMovablePositions().size(); j++) {
							validMoves.add(new Move(2, i, copyState.getMovablePositions().get(j)));
						}
						copyState.resetMovablePositions();
					}
				}
			}	
			
		}
		else if(copyState.getGameStage()==4) {
			
			for(int i=0; i<copyState.getBoardPieces().length; i++) {
				
				if(copyState.inMill(i) && copyState.canPieceBeRemoved()) {
	    			continue;
	    		}
				if((copyState.getTurn()==player && copyState.getBoardPieces()[i]==otherTurn))  {
					validMoves.add(new Move(4, i, -1));
				}
				
			}
		}
		
		return validMoves;
	}
	
	public boolean getMillCreated() {
		return millCreated;
	}
	
	public void resetMillCreated() {
		millCreated = false;
	}
	
	public int getNodesEvaluated() {
		return nodesEvaluated;
	}
}
