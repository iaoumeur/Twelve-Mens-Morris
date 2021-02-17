package player;

import java.util.ArrayList;
import java.util.Stack;

import gui.Game;
import state.GameState;

public class Minimax {
	
	Game game;
	GameState state;
	GameState copyState;
	GameState tempState;
	Stack<GameState> states = new Stack<GameState>();
	
	public Minimax(Game game, GameState state) {
		this.game = game;
		this.state = state;
	}
	
	public void setCopyState(GameState newState) {
		this.copyState = newState;
	}
	
	public MoveScore minimax(String player, int depth) {
		
		String otherPlayer;
		if(player=="black") {
			otherPlayer = "white";
		}
		else {
			otherPlayer = "black";
		}
		
		System.out.println("--------MINIMAX FOR " + player + " AT DEPTH: " + depth+ "-------");
		ArrayList<Move> validMoves = findValidMoves(player); 
		System.out.println("There are " + validMoves.size() + " valid moves");
		if (copyState.hasGameEnded()=="black"){
			System.out.println("Black has won");
			return new MoveScore(-1, 10000);
		}
		else if (copyState.hasGameEnded()=="white"){
			System.out.println("White has won");
			return new MoveScore(-1, 10000);
		}
		else if (copyState.hasGameEnded()=="draw"){
			System.out.println("Draw");
			return new MoveScore(-1, 0);
		}
		
		// an array to collect all the objects
		ArrayList<MoveScore> moves = new ArrayList<MoveScore>();
		int score = 0;
		
		// loop through available spots
		for (int i=0; i<validMoves.size(); i++){
			//create an object for each and store the index of that spot 
			Move move = validMoves.get(i);
			
			// set the empty spot to the current player
			if(move.getGameStage()==1) {
				String action = copyState.boardMouseClick(move.getPiecePosition());
				alterGame(action);
				//copyState.setBoardPiece(move.getPiecePosition(), player);
				score = copyState.evaluateState(player);
				System.out.println("Trying valid move " + i + " gives a score of " + score);
			}
			
			/*collect the score resulted from calling minimax 
		      on the opponent of the current player*/
			
			MoveScore result;
			states.push(copyState.saveGameState());
			System.out.println("Saving this game state and pushing to stack");
			
			if (copyState.getTurn() == "black" && depth !=0){
				result = minimax("white", depth-1);
			}
			else if(copyState.getTurn() == "white" && depth !=0){
				result = minimax("black", depth-1);
			}
			else {
				System.out.println("Maximum depth is reached, setting the result to be the latest score");
				result = new MoveScore(i, score);
			}
			
			// reset the spot to empty
			copyState = states.pop();
			System.out.println("Setting the state to be the last one on the stack");
			
			// push the object to the array
			moves.add(result);
			System.out.println("Adding the result " + result.index + ", " + result.score + " to the moves array");

		}
		
		//copyState = state.saveGameState();
		
		System.out.println("Now going through moves, attempting to find best move...");
		// if it is the computer's turn loop over the moves and choose the move with the highest score
		int bestMoveIndex = 0;
		int bestScore = 0;
		if(player == "black"){
			bestScore = -10000;
			for(int i=0; i<moves.size(); i++){
				if(moves.get(i).score > bestScore){
					bestScore = moves.get(i).score;
					bestMoveIndex = i;
				}
			}
		}else{
			// else loop over the moves and choose the move with the lowest score
			bestScore = 10000;
			for(int i=0; i<moves.size(); i++){
				if(moves.get(i).score < bestScore){
					bestScore = moves.get(i).score;
					bestMoveIndex = i;
				}
			}
		}
		System.out.println("Best move found for " + player + " is index " + bestMoveIndex + " with a score of " + bestScore);
		
		/*validMoves = findValidMoves(player); 
		Move bestMove = validMoves.get(bestMoveIndex);
		if(bestMove.getGameStage()==1) {
			state.setBoardPiece(bestMove.getPiecePosition(), player);
			return bestScore;
		}*/
		
		// return the chosen move (object) from the moves array
		return moves.get(bestMoveIndex);
	
		
		//return 0;
		
		
		
	}
	
	public void alterGame(String action) {
		if(action.equals("whitePlaced")) {
    		game.switchTurn();
    	}
    	else if(action.equals("blackPlaced")) {
    		game.switchTurn();
    	}
    	else if(action.equals("whiteRemoval") || action.equals("blackRemoval")) {
    		game.switchTurn();
    	}
    	else if(action.equals("pieceMoved")) {
    		game.switchTurn();
    	}
    	else if(action=="white") {
    		game.getState().setGameStage(5);
    	}
		
	}

	public void makeMove() {
		int bestScore = -10000;
		int bestMoveIndex = 0;
		
		ArrayList<Move> validMoves = findValidMoves("black"); 
		
		String temp = null;
		/*String[] tempBoardPieces = new String[state.getBoardPieces().length];
		for(int i=0; i<state.getBoardPieces().length; i++) {
			tempBoardPieces[i] = state.getBoardPiece(i);
		}*/
		
		for(int i=0; i<validMoves.size(); i++) {
			Move move = validMoves.get(i);
			if(move.getGameStage()==1) {
				copyState.setBoardPiece(move.getPiecePosition(), "black");
				int score = copyState.evaluateState("black");
				System.out.println("Valid move: " + i + ", Score: " + score);
				if(score>bestScore) {
					bestMoveIndex = i;
					bestScore = score;
				}
			}
			//state.setBoardPiece(move.getPiecePosition(), temp);
			copyState = state.saveGameState();
			//state.setBoardPieces(tempBoardPieces);
	
			
		}
		
		System.out.println("Best move index: " + bestMoveIndex);
		System.out.println("Game Stage: " + state.getGameStage());
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
		
		if(copyState.getGameStage()==1) {
			
			for(int i=0; i<copyState.getBoardPieces().length; i++) {
				if(copyState.getBoardPieces()[i]==null) {
					validMoves.add(new Move(1, i, null));
				}
			}
		}
		else if(copyState.getGameStage()==2) {
			
			for(int i=0; i<copyState.getBoardPieces().length; i++) {
				if(copyState.getTurn()==player && copyState.getBoardPieces()[i]==player) {
					System.out.print("Stage: 2, Piece Position: " + i + " Valid positions: ");
					copyState.showAvailablePositionsToMove(i);
					if(!copyState.getMovablePositions().isEmpty()) {
						validMoves.add(new Move(2, i, copyState.getMovablePositions()));	
						for(int j=0; j<copyState.getMovablePositions().size(); j++) {
							System.out.print(copyState.getMovablePositions().get(j) + ", ");
						}
						copyState.resetMovablePositions();
					}
					System.out.println();
				}
			}	
			
		}
		else if(copyState.getGameStage()==4) {
			
			for(int i=0; i<copyState.getBoardPieces().length; i++) {
				
				if(copyState.inMill(i) && copyState.canPieceBeRemoved()) {
	    			continue;
	    		}
				if((copyState.getTurn()==player && copyState.getBoardPieces()[i]==otherTurn))  {
					validMoves.add(new Move(4, i, null));
				}
				
				
			}
		}
		
		return validMoves;
	}

}
