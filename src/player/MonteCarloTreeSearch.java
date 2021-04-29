package player;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

import gui.Game;
import state.GameState;

//MCTS implementation - extends Computer to use its methods.
// -> has a constructor, alongside general MCTS method and methods for selection, expansion, simulation and back-propagation.
// -> UCT calculation

public class MonteCarloTreeSearch extends Computer{

	//altered through trial and error - started at root 2.
	final double explorationParameter = 4;
	
	public MonteCarloTreeSearch(Game game, GameState state) {
		super(game, state);
	}
	
	public MoveScore monteCarloTreeSearch(String player, int iterations, long timeForSearch) {
		
		Node root = new Node(copyState.saveGameState(), null);
		long startTime = System.currentTimeMillis();
		
		for(int j=0; j<iterations; j++) {
			
			Node bestNode = selection(root);
			Node exploreNode;
			
			if(bestNode.getGameState().hasGameEnded()==null) {
				expansion(bestNode);
			}
			
			exploreNode = bestNode;
			
			if(bestNode.getChildren().size()>0) {
				exploreNode = bestNode.getRandomChild();
			}
			
			double rolloutResult = rollout(exploreNode, player);
			backpropogate(exploreNode, rolloutResult);
			
			if((System.currentTimeMillis() - startTime) > timeForSearch) {
				break;
			}
			
		}
		
		Node finalNode = root.getBestChild();
		return finalNode.getAction();	
		
	}

	//find the next node to explore
	private Node selection(Node root) {
		
		Node node = root;
		int bestChildIndex = 0;
		
		while(!node.getChildren().isEmpty()) {
			double bestScore = Integer.MIN_VALUE;
			for(int i=0; i<node.getChildren().size(); i++) {
				double UCBScore = calculateUCTScore(node.getChildren().get(i));
				if(UCBScore> bestScore) {
					bestScore = UCBScore;
					bestChildIndex = i;
				}
			} 
			node = node.getChildren().get(bestChildIndex);
			
		}
		
		return node;
	
	}
	
	//add valid moves to the tree
	private void expansion(Node bestNode) {
		
		copyState = bestNode.getGameState().saveGameState();
		ArrayList<Move> validMoves = findValidMoves(copyState.getTurn()); 
		
		for (int i=0; i<validMoves.size(); i++){
			
			tempState = copyState.saveGameState();
			
			Move move = validMoves.get(i);
			simulateMove(move, copyState.getTurn());
			
			Node newNode = new Node(copyState.saveGameState(), bestNode);
			newNode.setAction(move.toMoveScore());
			bestNode.addChild(newNode);
			
			copyState = tempState.saveGameState();
				
		}
		
	}
	
	//simulate the game outcome
	private double rollout(Node exploreNode, String player) {
		
		String otherPlayer;
		if(player=="white") {
			otherPlayer="black";
		}
		else {
			otherPlayer="white";
		}
		
		tempState = copyState.saveGameState();
		copyState = exploreNode.getGameState().saveGameState();
		
		while(copyState.hasGameEnded()==null) {
			Random rand = new Random();
			ArrayList<Move> validMoves = findValidMoves(copyState.getTurn());
			
			if(validMoves.isEmpty()) {
				//loss
				if(copyState.getTurn()==otherPlayer) {
					return 0;
				}
				//win
				else {
					return 1;
				}
			}
			
			Move move = validMoves.get(rand.nextInt(validMoves.size()));
			simulateMove(move, copyState.getTurn());
			
		}
		
		String endgame = copyState.hasGameEnded();
		copyState = tempState.saveGameState();
		
		//loss
		if(endgame==otherPlayer) {
			return 0;
		}
		//win
		else if(endgame==copyState.getTurn()) {
			return 1;
		}
		
		//draw
		return 0.5;
	}
	
	//update previous nodes with new average score
	private void backpropogate(Node exploreNode, double rolloutResult) {
		
		Node tempNode = exploreNode;
		while(tempNode!=null) {
			tempNode.incrementVisits();
			tempNode.setTotalScore(tempNode.getTotalScore()+rolloutResult);
			tempNode = tempNode.getParent();
		}
		
	}

	private double calculateUCTScore(Node node) {
		
		if(node.getVisits()==0) {
			return Integer.MAX_VALUE;
		}
		double V = node.getTotalScore() / node.getVisits();
		int N = node.getParent().getVisits();
		int n = node.getVisits();
		
		return (V + explorationParameter*(Math.sqrt((Math.log(N))/n)));
	}
	
	private void printTree(Node node, int depth) {
		
		System.out.println("ROOT: Number of visits: " + node.getVisits() + ". Total score: " + node.getTotalScore());
		Queue<Node> queue = new LinkedList<Node>();
		for(int i=0; i<node.getChildren().size(); i++) {
			queue.add(node.getChildren().get(i));
		}
		int total = 0;
		while(!queue.isEmpty()) {
			Node n = queue.remove();
			System.out.println("Child " + total + ". Number of visits: " + n.getVisits() + ". Total score: " + n.getTotalScore() + ". UCB: " + calculateUCTScore(n));
			for(int i=0; i<n.getChildren().size(); i++) {
				queue.add(n.getChildren().get(i));
			}
			total++;
		}
		
	}
	
	
}
