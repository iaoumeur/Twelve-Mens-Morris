package player;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

import gui.Game;
import state.GameState;

//IN MINIMAX LOOP, LOOK AT TABOO SEARCH, maybe think about penalty cost for cycling.
//after making a move, undo the last move so our don't copy the state
public class Minimax extends Computer{
	
	int initialDepth = -1;
	long startTime;
	String initialPlayer = "black";
	MoveScore maxMove = new MoveScore(-1, -1, Integer.MAX_VALUE);
	MoveScore minMove = new MoveScore(-1, -1, Integer.MIN_VALUE);
	
	public Minimax(Game game, GameState state) {
		super(game, state);
	}
	
	public MoveScore minimax(String player, int depth, int alpha, int beta, long timeForSearch) {
		//System.out.println("--------MINIMAX FOR " + player + " AT DEPTH: " + depth+ "-------");
		if(initialDepth==-1) {
			startTime = System.currentTimeMillis();
			initialDepth = depth;
			initialPlayer=player;
		}
		
		if((System.currentTimeMillis() - startTime) > timeForSearch) {
			if(initialPlayer=="black") {
				return maxMove;
			}
			else {
				return minMove;
			}
		}
	
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
				return new MoveScore(-1, -1, 10000);
			}
			else if(winner=="white") {
				return new MoveScore(-1, -1, -10000);
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
			//System.out.println("Score for move " + i + " = " + score);
			//System.out.println(score);
			states.push(copyState.saveGameState());
			nodesEvaluated++;
			
			if(depth==0) {
				moveToPush.score = score;
			}
			else if(copyState.getTurn()=="black" && depth !=0){
				result = minimax("black", depth-1, alpha, beta, timeForSearch);
				moveToPush.score = result.score;
				
				if(moveToPush.score < minMove.score) {
					minMove = moveToPush;
				}
				//alpha-beta pruning
				
				if(beta>result.score) {
					beta = result.score;
				}
				if(beta<= alpha) {
					//System.out.println("Min move has been found");
					i = validMoves.size();
				}
				//System.out.println("RECURSIVE RESULT: " + result.index + ", " + result.score);
				
			}
			else if (copyState.getTurn()=="white" && depth !=0){
				result = minimax("white", depth-1, alpha, beta, timeForSearch);
				moveToPush.score = result.score;
				
				if(moveToPush.score > maxMove.score) {
					maxMove = moveToPush;
				}
				
				//alpha-beta pruning
				if(alpha<result.score) {
					alpha = result.score;
				}
				if(beta<= alpha) {
					//System.out.println("Max move has been found");
					i = validMoves.size();
				}
				//System.out.println("RECURSIVE RESULT: " + result.index + ", " + result.score);
				
			}
			moves.add(moveToPush);
			
			// restores the game state to be the previous state
			states.pop();
			copyState = states.lastElement().saveGameState();
			
			
		}
		
		//System.out.println("Now going through moves, attempting to find best move...");
		/*String pieces = "MOVES: [";
		for(int i=0; i<moves.size(); i++) {
			pieces += moves.get(i).index + ", ";
		}
		System.out.println(pieces + "]");*/
		
		
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
			for(int i=0; i<moves.size(); i++){
				//System.out.println("Move " + i + " with a score of " + moves.get(i).score);
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
		//System.out.println("Best move found for " + player + " is index " + bestMoveIndex + " with a score of " + bestScore);
		
		return moves.get(bestMoveIndex);
		
	}
	
	public void reset() {
		initialDepth = -1;
		maxMove = new MoveScore(-1, -1, Integer.MAX_VALUE);
		minMove = new MoveScore(-1, -1, Integer.MIN_VALUE);
	}
	
	
	

}
