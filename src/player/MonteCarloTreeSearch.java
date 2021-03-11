package player;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Stack;

import gui.Game;
import state.GameState;

public class MonteCarloTreeSearch {

	Game game;
	GameState state;
	GameState copyState;
	GameState tempState;
	Stack<GameState> states = new Stack<GameState>();
	
	int nodesEvaluated = 0;
	boolean millCreated = false;
	
	public MonteCarloTreeSearch(Game game, GameState state) {
		this.game = game;
		this.state = state;
	}
	
	public void setCopyState(GameState newState) {
		this.copyState = newState;
		states.push(state);
	}
	
	public Move monteCarloTreeSearch(String player, int iterations) {
		
		Node root = new Node(copyState.saveGameState(), null);
		long startTime = System.currentTimeMillis();
		
		//while((System.currentTimeMillis() - startTime) < timeForSearch) {
		for(int j=0; j<iterations; j++) {
			
			System.out.println("*** PERFORMING SELECTION ***");
			Node bestNode = selection(root);
			//System.out.println("Best Node selected. Board state for this node is: ");
			//bestNode.getGameState().printBoardPieces();
			//System.out.println("*** SELECTION COMPLETE ***");
			
			Node exploreNode;
			if(bestNode.getGameState().hasGameEnded()==null) {
				System.out.println("*** PERFORMING EXPANSION ***");
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
			System.out.println("*** PERFORMING ROLLOUT ***");
			int rolloutResult = rollout(exploreNode);
			System.out.println("*** PERFORMING BACKPROPAGATION ***");
			backpropogate(exploreNode, rolloutResult);
			
		}
		
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
	

	private Node selection(Node root) {
		
		Node node = root;
		int bestChildIndex = 0;
		int depth = 0;
		
		while(!node.getChildren().isEmpty()) {
			double bestScore = Integer.MIN_VALUE;
			for(int i=0; i<node.getChildren().size(); i++) {
				//System.out.println("There are " + node.getChildren().size() + " children");
				double UCBScore = calculateUCBScore(node.getChildren().get(i));
				System.out.println("Child " + i + " UCBScore: " + UCBScore);
				if(UCBScore> bestScore) {
					bestScore = UCBScore;
					bestChildIndex = i;
				}
			} 
			node = node.getChildren().get(bestChildIndex);
			depth++;
			
		}
		
		System.out.println("Best child is " + bestChildIndex + " selected at depth " + depth + ", score: " + node.getTotalScore() + " visits: " + node.getVisits());
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
			newNode.setAction(move);
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
		
		int moves = 0;
		//System.out.println("Turn: " + copyState.getTurn());
		while(copyState.hasGameEnded()==null && moves < 100) {
			//System.out.println("Game has not ended");
			Random rand = new Random();
			//System.out.println("player: " + player);
			//System.out.println("copy state turn: " + copyState.getTurn());
			ArrayList<Move> validMoves = findValidMoves(copyState.getTurn());
			if(validMoves.isEmpty()) {
				if(copyState.getTurn()==otherPlayer) {
					//System.out.println("White won this simulation");
					return -10;
				}
				else {
					//System.out.println("Black won this simulation");
					return 10;
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
			moves++;
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
			System.out.println("Returning max value");
			return Integer.MAX_VALUE;
		}
		double V = node.getTotalScore() / node.getVisits();
		int N = node.getParent().getVisits();
		int n = node.getVisits();
		
		return V + 2*(Math.sqrt((Math.log(N))/n));
	}

	public void makeMove(Move move, String player) {
		//System.out.println("BEFORE MAKING MOVE");
		if(state.getGameStage()==1) {
			state.setBoardPiece(move.piecePosition, player);
			alterGame(1, player, state);
			//System.out.println("AFTER MAKING MOVE");
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
			state.setBoardPiece(move.piecePosition, null);
			state.setBoardPiece(move.to, player);
			alterGame(2, player, state);
			System.out.println("AFTER MAKING MOVE");
			state.evaluateState(player, true);
			if(state.getGameStage()==4) {
				millCreated = true;
				return;
			}
		}
		else if(state.getGameStage()==4) {
			state.setBoardPiece(move.piecePosition, null);
			alterGame(4, player, state);
			System.out.println("AFTER MAKING MOVE");
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
	
	private void simulateMove(Move move, String player) {
		
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
	
	public boolean getMillCreated() {
		return millCreated;
	}
	
	public void resetMillCreated() {
		millCreated = false;
	}
	
	
}
