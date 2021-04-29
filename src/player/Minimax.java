package player;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

import gui.Game;
import state.GameState;

//Minimax implementation - extends Computer to use its methods.
// -> has only a constructor and the minimax() method.

public class Minimax extends Computer{
	
	public Minimax(Game game, GameState state) {
		super(game, state);
	}
	
	public MoveScore minimax(String player, int depth, int alpha, int beta, long timeForSearch) {
	
		//find the list of valid moves Minimax can take.
		ArrayList<Move> validMoves = findValidMoves(player); 
		
		//if there are no valid moves, this means in this state the game would end.
		if(validMoves.isEmpty()) {
			copyState.switchTurn();
			String winner = copyState.hasGameEnded();
			if(winner=="draw") {
				return new MoveScore(-1, -1, 0);
			}
			else if(winner=="black") {
				return new MoveScore(-1, -1, Integer.MAX_VALUE);
			}
			else if(winner=="white") {
				return new MoveScore(-1, -1, Integer.MIN_VALUE);
			}
			copyState.switchTurn();
		}
		
		
		// an array to collect all the objects
		int score = 0;
		ArrayList<MoveScore> moves = new ArrayList<MoveScore>();
			
		for (int i=0; i<validMoves.size(); i++){
			
			Move move = validMoves.get(i);
			MoveScore result = new MoveScore(move.getPiecePosition(), move.getTo(), 0);
			MoveScore moveToPush = new MoveScore(move.getPiecePosition(), move.getTo(), 0);
			
			simulateMove(move, player);
			
			score = copyState.evaluateState(player, false);
			states.push(copyState.saveGameState());
			nodesEvaluated++;
			
			if(depth==0) {
				moveToPush.score = score;
			}
			//if the turn is now black (max) after making a move, then it was previously white's (min) turn, so the next recursive call is for max
			else if(copyState.getTurn()=="black" && depth !=0){
				result = minimax("black", depth-1, alpha, beta, timeForSearch);
				moveToPush.score = result.score;
				
				//alpha-beta pruning
				if(beta>result.score) {
					beta = result.score;
				}
				if(beta<= alpha) {
					i = validMoves.size();
				}
				
			}
			//if the turn is now white (min) after making a move, then it was previously black's (max) turn, so the next recursive call is for min
			else if (copyState.getTurn()=="white" && depth !=0){
				result = minimax("white", depth-1, alpha, beta, timeForSearch);
				moveToPush.score = result.score;

				//alpha-beta pruning
				if(alpha<result.score) {
					alpha = result.score;
				}
				if(beta<= alpha) {
					i = validMoves.size();
				}
				
			}
			moves.add(moveToPush);
			
			// restores the game state to be the previous state
			states.pop();
			copyState = states.lastElement().saveGameState();
			
			
		}
		
		//find the best move out of the considered states.
		int bestMoveIndex = 0;
		int bestScore = 0;
		boolean allSameScore = true;
		int tempScore;
		if(moves.isEmpty()) {
			tempScore = 0;
		}
		else {
			tempScore = moves.get(0).score;
		}
		
		if(player == "black"){
			bestScore = -100000;
			for(int i=0; i<moves.size(); i++) {
				if(moves.get(i).score != tempScore) {
					allSameScore = false;
				}
				else {
					tempScore = moves.get(i).score;
				}
				if(moves.get(i).score > bestScore){
					bestScore = moves.get(i).score;
					bestMoveIndex = i;
				}
			}
		}else{
			bestScore = 100000;
			for(int i=0; i<moves.size(); i++){
				if(moves.get(i).score != tempScore) {
					allSameScore = false;
				}
				else {
					tempScore = moves.get(i).score;
				}
				if(moves.get(i).score < bestScore){
					bestScore = moves.get(i).score;
					bestMoveIndex = i;
				}
			}
		}
		
		if(allSameScore) {
			Random rand = new Random();
			bestMoveIndex = rand.nextInt(moves.size());
		}
		
		return moves.get(bestMoveIndex);
		
	}
	
	
	
	

}
