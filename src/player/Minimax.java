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
		states.push(state);
	}
	
	public MoveScore minimax(String player, int depth) {
		
		ArrayList<Move> validMoves = findValidMoves(player); 

		System.out.println("--------MINIMAX FOR " + player + " AT DEPTH: " + depth+ "-------");
		System.out.println("There are " + validMoves.size() + " valid moves");
		
		// an array to collect all the objects
		ArrayList<MoveScore> moves = new ArrayList<MoveScore>();
		int score = 0;
		
		for (int i=0; i<validMoves.size(); i++){
			
			Move move = validMoves.get(i);
			MoveScore result = new MoveScore(-1, -1, 0);
			
			// set the empty spot to the current player
			if(move.getGameStage()==1) {
				copyState.setBoardPiece(move.getPiecePosition(), player);
				copyState.printBoardPieces();
				states.push(copyState.saveGameState());
				System.out.println("Saving this game state and pushing to stack");
				alterGame(1, player, copyState);
				score = copyState.evaluateState(player);
				System.out.println("Trying valid move " + i + " gives a score of " + score);
				
				if(depth==0){
					System.out.println("Maximum depth is reached, setting the result to be the latest score");
					result = new MoveScore(move.getPiecePosition(), -1, score);
				}
				else if(copyState.getTurn()=="white" && depth !=0){
					result = minimax("black", depth-1);
					System.out.println("RECURSIVE RESULT: " + result.index + ", " + result.score);
				}
				else if (copyState.getTurn()=="black" && depth !=0){
					result = minimax("white", depth-1);
					System.out.println("RECURSIVE RESULT: " + result.index + ", " + result.score);
				}
				
				// reset the spot to empty
				states.pop();
				System.out.println("Setting the state to be the last one on the stack");
				copyState = states.lastElement().saveGameState();
				moves.add(result);
				System.out.println("Adding the result " + result.index + ", " + result.score + " to the moves array");
			}
			else if(move.getGameStage()==2) {
			
				for(int j=0; j<move.getAvailablePositionsToMove().size(); j++) {
					copyState.setBoardPiece(move.getPiecePosition(), null);
					copyState.setBoardPiece(move.getAvailablePositionsToMove().get(j), player);
					System.out.println("Saving this game state and pushing to stack");
					states.push(copyState.saveGameState());
					score = copyState.evaluateState(player);
					alterGame(2, player, copyState);
					System.out.println("Moving piece " + move.getPiecePosition() + " to " + move.getAvailablePositionsToMove().get(j) + " gives a score of " + score);
					
					if(depth==0){
						System.out.println("Maximum depth is reached, setting the result to be the latest score");
						result = new MoveScore(move.getPiecePosition(), move.getAvailablePositionsToMove().get(j), score);
					}
					else if(player=="white" && depth !=0){
						result = minimax("black", depth-1);
						System.out.println("RECURSIVE RESULT: " + result.index + ", " + result.score);
					}
					else if (player =="black" && depth !=0){
						result = minimax("white", depth-1);
						System.out.println("RECURSIVE RESULT: " + result.index + ", " + result.score);
					}
					
					// reset the spot to empty
					states.pop();
					copyState = states.lastElement().saveGameState();
					
					System.out.println("Setting the state to be the last one on the stack");
					// push the object to the array
					moves.add(result);
					System.out.println("Adding the result " + result.index + ", " + result.score + " to the moves array");
				}

			}
			else if(move.getGameStage()==4) {
				copyState.setBoardPiece(move.getPiecePosition(), null);
				copyState.printBoardPieces();
				states.push(copyState.saveGameState());
				System.out.println("Saving this game state and pushing to stack");
				alterGame(4, player, copyState);
				score = copyState.evaluateState(player);
				System.out.println("Trying valid move " + i + " gives a score of " + score);
				
				if(depth==0){
					System.out.println("Maximum depth is reached, setting the result to be the latest score");
					result = new MoveScore(move.getPiecePosition(), -1, score);
				}
				else if(copyState.getTurn()=="white" && depth !=0){
					result = minimax("black", depth-1);
					System.out.println("RECURSIVE RESULT: " + result.index + ", " + result.score);
				}
				else if (copyState.getTurn() =="black" && depth !=0){
					result = minimax("white", depth-1);
					System.out.println("RECURSIVE RESULT: " + result.index + ", " + result.score);
				}
				
				// reset the spot to empty
				states.pop();
				System.out.println("Setting the state to be the last one on the stack");
				copyState = states.lastElement().saveGameState();
				moves.add(result);
				System.out.println("Adding the result " + result.index + ", " + result.score + " to the moves array");
			}
			
			/*collect the score resulted from calling minimax 
		      on the opponent of the current player*/
			
			
			/*MoveScore result = new MoveScore(0,0);
			System.out.println("Saving this game state and pushing to stack");
			
			if(depth==0){
				System.out.println("Maximum depth is reached, setting the result to be the latest score");
				result = new MoveScore(move.getPiecePosition(), -1, score);
			}
			else if(player=="white" && depth !=0){
				result = minimax("black", depth-1);
				System.out.println("RECURSIVE RESULT: " + result.index + ", " + result.score);
			}
			else if (player =="black" && depth !=0){
				result = minimax("white", depth-1);
				System.out.println("RECURSIVE RESULT: " + result.index + ", " + result.score);
			}
			
			// reset the spot to empty
			states.pop();
			copyState = states.lastElement().saveGameState();
			
			System.out.println("Setting the state to be the last one on the stack");
			// push the object to the array
			moves.add(result);
			System.out.println("Adding the result " + result.index + ", " + result.score + " to the moves array");*/

		}
		
		//copyState = state.saveGameState();
		
		System.out.println("Now going through moves, attempting to find best move...");
		String pieces = "MOVES: [";
		for(int i=0; i<moves.size(); i++) {
			pieces += moves.get(i).index + ", ";
		}
		System.out.println(pieces + "]");
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
				System.out.println("***** THIS MOVE MADE A MILL *****");
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
		if(state.getGameStage()==1) {
			state.setBoardPiece(move.index, player);
			alterGame(1, player, state);
			if(player=="white") {
				game.removeWhitePieceFromPanel();
			}
			else {
				game.removeBlackPieceFromPanel();
			}
		}
		else if(state.getGameStage()==2) {
			state.setBoardPiece(move.index, null);
			state.setBoardPiece(move.to, player);
		}
		else if(state.getGameStage()==4) {
			state.setBoardPiece(move.index, null);
		}
		game.getBoard().repaintPieces();
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
					copyState.showAvailablePositionsToMove(i);
					if(!copyState.getMovablePositions().isEmpty()) {
						validMoves.add(new Move(2, i, copyState.getMovablePositions()));	
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
					validMoves.add(new Move(4, i, null));
				}
				
				
			}
		}
		
		return validMoves;
	}

}
