package player;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

import gui.Game;
import state.GameState;

public class MonteCarloTreeSearch extends Computer{

	final double explorationParameter = Math.sqrt(2);
	public MonteCarloTreeSearch(Game game, GameState state) {
		super(game, state);
	}
	
	public MoveScore monteCarloTreeSearch(String player, int iterations) {
		
		Node root = new Node(copyState.saveGameState(), null);
		long startTime = System.currentTimeMillis();
		
		//MAIN MONTE CARLO SEARCH LOOP
		//while((System.currentTimeMillis() - startTime) < timeForSearch) {
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
			
			int rolloutResult = rollout(exploreNode, player);
			backpropogate(exploreNode, rolloutResult);
			
		}
		
		//printTree(root, 0);
		Node finalNode = root.getBestChild();
		return finalNode.getAction();	
		
	}

	private Node selection(Node root) {
		
		Node node = root;
		int bestChildIndex = 0;
		
		while(!node.getChildren().isEmpty()) {
			double bestScore = Integer.MIN_VALUE;
			for(int i=0; i<node.getChildren().size(); i++) {
				double UCBScore = calculateUCBScore(node.getChildren().get(i));
				if(UCBScore> bestScore) {
					bestScore = UCBScore;
					bestChildIndex = i;
				}
			} 
			node = node.getChildren().get(bestChildIndex);
			
		}
		
		return node;
	
	}
	
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
	
	private int rollout(Node exploreNode, String player) {
		
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
				if(copyState.getTurn()==otherPlayer) {
					//System.out.println("No valid moves - White won this simulation");
					return -1;
				}
				else {
					//System.out.println("No valid moves - Black won this simulation");
					return 1;
				}
			}
			
			Move move = validMoves.get(rand.nextInt(validMoves.size()));
			simulateMove(move, copyState.getTurn());
		}
		
		//int evaluationScorePlayer = copyState.evaluateState(copyState, false);
		String endgame = copyState.hasGameEnded();
		copyState = tempState.saveGameState();
		
		if(endgame==otherPlayer) {
			//System.out.println("White won this simulation");
			return -1;
		}
		else if(endgame==copyState.getTurn()) {
			//System.out.println("Black won this simulation");
			return 1;
		}
		
		//System.out.println("Draw");
		return 0;
	}
	
	
	private void backpropogate(Node exploreNode, int rolloutResult) {
		
		Node tempNode = exploreNode;
		while(tempNode!=null) {
			tempNode.incrementVisits();
			tempNode.setTotalScore(tempNode.getTotalScore()+rolloutResult);
			tempNode = tempNode.getParent();
		}
		
	}

	private double calculateUCBScore(Node node) {
		
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
			System.out.println("Child " + total + ". Number of visits: " + n.getVisits() + ". Total score: " + n.getTotalScore() + ". UCB: " + calculateUCBScore(n));
			for(int i=0; i<n.getChildren().size(); i++) {
				queue.add(n.getChildren().get(i));
			}
			total++;
		}
		
	}
	
	
}
