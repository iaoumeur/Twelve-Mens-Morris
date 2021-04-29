package player;

import java.util.ArrayList;
import java.util.Stack;

import gui.Game;
import state.GameState;

//contains methods for Minimax and MCTS to use
// -> allow for simulating and making moves using the AI.

public abstract class Computer {

	public Game game;
	public GameState state;
	public GameState copyState;
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
	
	//makes a move on the copyState, so that the actual game is not affected.
	public void simulateMove(Move move, String player) {
		
		if(move.getGameStage()==1) {
			copyState.setBoardPiece(move.getPiecePosition(), player);
			alterGame(1, player, copyState);
		}
		else if(move.getGameStage()==2) {
			copyState.setBoardPiece(move.getPiecePosition(), null);
			copyState.setBoardPiece(move.getTo(), player);
			alterGame(2, player, copyState);
		}
		else if(move.getGameStage()==4) {
			copyState.setBoardPiece(move.getPiecePosition(), null);
			alterGame(4, player, copyState);
		}
		
	}

	//any logic that occurs after a move is created at this point
	public void alterGame(int gameStage, String player, GameState gameState) {
		
		if(gameStage==1) {
			if(player=="white") {
				gameState.whitePiecesPlaced++;
			}
			else {
				gameState.blackPiecesPlaced++;
			}
			gameState.piecesPlaced++;
			if(gameState.checkForMill(true)) {
			}
			else {
				copyState.switchTurn();
				if(gameState.piecesPlaced>=gameState.totalNumberOfPieces){
					String endgame = gameState.hasGameEnded();
					if(endgame!=null) {
						return;
					}
					gameState.setGameStage(2);
					if(player=="white") {
						gameState.whitePhase = 2;
					}
					else {
						gameState.blackPhase = 2;
					}
				}	
			}
		}
		else if(gameStage==2) {
			if(gameState.checkForMill(true)) {
			}
			else {
				gameState.incrementMovesWihtoutMill();
				copyState.switchTurn();
				gameState.setGameStage(2);
			}
			
    		gameState.resetMovablePositions();
    		String endgame = gameState.hasGameEnded();
    		if(endgame!=null) {
    			return;
    		}
		}
		else if(gameStage==4) {
			gameState.checkForMill(true);
			if(gameState.piecesPlaced<gameState.totalNumberOfPieces) {
				gameState.setGameStage(1);
			}
			else {
				gameState.setGameStage(2);
			}
			String endgame = gameState.hasGameEnded();
			if(endgame!=null) {
				return;
			}
			copyState.switchTurn();
			gameState.resetMovablePositions();
		}
		
		
	}
	
	//makes a move on the actual state of the game, and updating any game logic.
	public void makeMove(MoveScore move, String player) {
		nodesEvaluated = 0;
		if(state.getGameStage()==1) {
			state.setBoardPiece(move.index, player);
			alterGame(1, player, state);
			if(player=="white") {
				game.removeWhitePieceFromPanel();
			}
			else {
				game.removeBlackPieceFromPanel();
			}
			if(state.getGameStage()==4) {
				millCreated = true;
				return;
			}
		}
		else if(state.getGameStage()==2) {
			state.setBoardPiece(move.index, null);
			state.setBoardPiece(move.to, player);
			alterGame(2, player, state);
			if(state.getGameStage()==4) {
				millCreated = true;
				return;
			}
		}
		else if(state.getGameStage()==4) {
			state.setBoardPiece(move.index, null);
			alterGame(4, player, state);
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
	
	//returns an ArrayList of all the possible moves the AI player can make
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
					validMoves.add(new Move(4, i, -2));
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
