package player;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Stack;

import gui.Game;
import state.GameState;

public class MonteCarloTreeSearch extends Computer{

	public MonteCarloTreeSearch(Game game, GameState state) {
		super(game, state);
		// TODO Auto-generated constructor stub
	}
	
	public MoveScore monteCarloTreeSearch(String player, long timeForSearch) {
		
		Node root = new Node(copyState.saveGameState(), null);
		long startTime = System.currentTimeMillis();
		
		while((System.currentTimeMillis() - startTime) < timeForSearch) {
		//for(int j=0; j<iterations; j++) {
			
			//System.out.println("*** PERFORMING SELECTION ***");
			Node bestNode = selection(root);
			//System.out.println("Best Node selected. Board state for this node is: ");
			//bestNode.getGameState().printBoardPieces();
			//System.out.println("*** SELECTION COMPLETE ***");
			
			Node exploreNode;
			if(bestNode.getGameState().hasGameEnded()==null) {
				//System.out.println("*** PERFORMING EXPANSION ***");
				expansion(bestNode);
				//System.out.println("Best node now has " + bestNode.getChildren().size() + " children.");
				for(int i=0; i<bestNode.getChildren().size(); i++) {
					//System.out.println("Child " + i + ": ");
					//bestNode.getChildren().get(i).getGameState().printBoardPieces();
				}
				//System.out.println("*** EXPANSION COMPLETE***");
			}
			exploreNode = bestNode;
			if(bestNode.getChildren().size()>0) {
				exploreNode = bestNode.getRandomChild();
				//System.out.println("Picking random child for explore node: ");
				//exploreNode.getGameState().printBoardPieces();
			}
			//System.out.println("*** PERFORMING ROLLOUT ***");
			int rolloutResult = rollout(exploreNode);
			//System.out.println("*** PERFORMING BACKPROPAGATION ***");
			backpropogate(exploreNode, rolloutResult);
			
			//System.out.println("End of iteration tree:");
			
		}
		
		//printTree(root, 0);
		Node finalNode = root.getBestChild();
		return finalNode.getAction();
		
		/*let startTime = Date.now();
		  while ((Date.now() - startTime) < 1000) {
		    let promisingNode = selectPromisingNode(rootNode);
		    
		    if (promisingNode.state.board.checkStatus() === IN_PROGRESS) {
		      expandNode(promisingNode);
		    }
		    let nodeToExplore = promisingNode;
		    if (nodeToExplore.childArray.length > 0) {
		      nodeToExplore = promisingNode.getRandomChild();
		    }
		    let playoutResult = simRanPlayout(nodeToExplore, opponent)
		    backpropagation(nodeToExplore, playoutResult);
		  }
		  let winnerNode = rootNode.getChildWithMaxScore();
		  return winnerNode.state.board;*/
	
		
		
	}
	

	private void printTree(Node node, int depth) {
		if(depth==0) {
			System.out.println("ROOT: Number of visits: " + node.getVisits() + ". Total score: " + node.getTotalScore());
		}
		else {
			System.out.println("Child at depth " + depth + ". Number of visits: " + node.getVisits() + ". Total score: " + node.getTotalScore() + ". UCB: " + calculateUCBScore(node));
		}
		for(int i=0; i<node.getChildren().size(); i++) {
	        printTree(node.getChildren().get(i), depth+1);
	    }
		
	}

	private Node selection(Node root) {
		
		Node node = root;
		int bestChildIndex = 0;
		int depth = 0;
		
		while(!node.getChildren().isEmpty()) {
			double bestScore = -10000000;
			for(int i=0; i<node.getChildren().size(); i++) {
				//System.out.println("There are " + node.getChildren().size() + " children");
				double UCBScore = calculateUCBScore(node.getChildren().get(i));
				//System.out.println("Child " + i + " at depth " + depth + " UCBScore: " + UCBScore);
				if(UCBScore> bestScore) {
					bestScore = UCBScore;
					bestChildIndex = i;
				}
			} 
			node = node.getChildren().get(bestChildIndex);
			depth++;
			
		}
		
		//System.out.println("Best child is " + bestChildIndex + " selected at depth " + depth + ", score: " + node.getTotalScore() + " visits: " + node.getVisits());
		return node;
		/*if(n.getChildren().isEmpty()) {
			return n;
		}
		
		

		for(int i=0; i<n.getChildren().size(); i++) {
			double UCBScore = calculateUCBScore(n.getChildren().get(i));
			if(UCBScore> bestScore) {
				bestScore = UCBScore;
				bestChildIndex = i;
			}
		}
		  
		return n.getChildren().get(bestChildIndex);*/
		
		/*selectPromisingNode(rootNode) {
			  let node = rootNode;
			  while (node.childArray.length !== 0) {
			    node = UCB.findBestNodeWithUCB(node);
			  }
			  return node;
			}*/
	}
	
	private void expansion(Node bestNode) {
		
		String otherTurn;
		if(bestNode.getGameState().getTurn()=="black") {
			otherTurn = "white";
		}
		else {
			otherTurn = "black";
		}
		
		copyState = bestNode.getGameState().saveGameState();
		ArrayList<Move> validMoves = findValidMoves(copyState.getTurn()); 
		
		for (int i=0; i<validMoves.size(); i++){
			
			//System.out.println("BEFORE: " + copyState.getTurn());
			tempState = copyState.saveGameState();
			
			Move move = validMoves.get(i);
			simulateMove(move, copyState.getTurn());
			
			/*if(copyState.hasGameEnded()!=null) {
				copyState = tempState.saveGameState();
				continue;
			}*/
			
			Node newNode = new Node(copyState.saveGameState(), bestNode);
			newNode.setAction(move.toMoveScore());
			bestNode.addChild(newNode);
			
			copyState = tempState.saveGameState();
			//System.out.println("AFTER: " + copyState.getTurn());
			
			
		}
		
		/*let possibleStates = node.state.getAllPossibleStates();
		  possibleStates.forEach(state => {
		    let newNode = new Node(state);
		    newNode.parent = node;
		    newNode.state.playNo = node.state.getOpponent();
		    node.childArray.push(newNode);
		  });*/
	}
	
	private int rollout(Node exploreNode) {
		
		String otherPlayer;
		if(copyState.getTurn()=="white") {
			otherPlayer="black";
		}
		else {
			otherPlayer="white";
		}
		
		//System.out.println("Saving copyState and creating tempState");
		tempState = copyState.saveGameState();
		copyState = exploreNode.getGameState().saveGameState();
		
		//System.out.println("Turn: " + copyState.getTurn());
		while(copyState.hasGameEnded()==null) {
			//System.out.println("Game has not ended");
			Random rand = new Random();
			//System.out.println("player: " + player);
			//System.out.println("copy state turn: " + copyState.getTurn());
			ArrayList<Move> validMoves = findValidMoves(copyState.getTurn());
			if(validMoves.isEmpty()) {
				if(copyState.getTurn()==otherPlayer) {
					//System.out.println("White won this simulation");
					return -20;
				}
				else {
					//System.out.println("Black won this simulation");
					return 20;
				}
			}
			//System.out.println("Turn: " + copyState.getTurn());
			//System.out.println("Valid Moves: " + validMoves.size());
			Move move = validMoves.get(rand.nextInt(validMoves.size()));
			//System.out.println("Simulating move for: " + copyState.getTurn());
			simulateMove(move, copyState.getTurn());
			//System.out.println("Random move creates board state: ");
			//copyState.printBoardPieces();
			//System.out.println("Move made, board state is now: ");
			//copyState.printBoardPieces();
		}
		
		//System.out.println("Game has ended");
		String endgame = copyState.hasGameEnded();
		copyState = tempState.saveGameState();
		
		if(endgame==otherPlayer) {
			//System.out.println("White won this simulation");
			return -10;
		}
		else if(endgame==copyState.getTurn()) {
			//System.out.println("Black won this simulation");
			return 10;
		}
		
		return 0;
	}
	
	
	private void backpropogate(Node exploreNode, int rolloutResult) {
		
		Node tempNode = exploreNode;
		while(tempNode!=null) {
			tempNode.incrementVisits();
			tempNode.setTotalScore(tempNode.getTotalScore()+rolloutResult);
			//System.out.println("Node score: " + tempNode.getTotalScore());
			//System.out.println("Node visits: " + tempNode.getVisits());
			tempNode = tempNode.getParent();
		}
		
		/*
		let tempNode = nodeToExplore;
		  while (tempNode !== undefined) {
		    tempNode.state.visitCount++;
		    if (tempNode.state.playNo === playoutResult) {
		      tempNode.state.addScore(10);
		    }
		    tempNode = tempNode.parent;
		  }*/
		
	}

	private double calculateUCBScore(Node node) {
		
		if(node.getVisits()==0) {
			//System.out.println("Returning max value");
			return 1000000000;
		}
		double V = node.getTotalScore() / node.getVisits();
		int N = node.getParent().getVisits();
		int n = node.getVisits();
		
		return (V + 2*(Math.sqrt((Math.log(N))/n)));
	}
	
	
}
