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
		
		// loop through available spots
		for (int i=0; i<validMoves.size(); i++){
			//create an object for each and store the index of that spot 
			Move move = validMoves.get(i);
			
			// set the empty spot to the current player
			if(move.getGameStage()==1) {
				//String action = copyState.boardMouseClick(move.getPiecePosition());
				copyState.setBoardPiece(move.getPiecePosition(), player);
				states.push(copyState.saveGameState());
				alterGame(1, player);
				score = copyState.evaluateState(player);
				copyState.printBoardPieces();
				System.out.println("Trying valid move " + i + " gives a score of " + score);
				copyState.switchTurn();
			}
			else if(move.getGameStage()==2) {
			
				for(int j=0; j<validMoves.get(i).getAvailablePositionsToMove().size(); j++) {
					copyState.setBoardPiece(validMoves.get(i).getPiecePosition(), null);
					copyState.setBoardPiece(validMoves.get(i).getAvailablePositionsToMove().get(j), player);
				}
				score = copyState.evaluateState(player);
				copyState.printBoardPieces();
				System.out.println("Trying valid move " + i + " gives a score of " + score);
				copyState.switchTurn();
			}
			
			/*collect the score resulted from calling minimax 
		      on the opponent of the current player*/
			
			
			MoveScore result = new MoveScore(0,0);
			System.out.println("Saving this game state and pushing to stack");
			
			if(depth==0){
				System.out.println("Maximum depth is reached, setting the result to be the latest score");
				result = new MoveScore(move.getPiecePosition(), score);
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
	
		
		//return 0;
		
		
		
	}
	
	public void alterGame(int gameStage, String player) {
		
		if(gameStage==1) {
			if(player=="white") {
				copyState.whitePiecesPlaced++;
			}
			else {
				copyState.blackPiecesPlaced++;
			}
			copyState.piecesPlaced++;
			if(copyState.checkForMill()) {
				
			}
			else if(copyState.piecesPlaced>=copyState.totalNumberOfPieces){
				copyState.setGameStage(2);
				copyState.phase = 2;
			}
		}
		else if(gameStage==2) {
			
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
