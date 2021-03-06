package player;

import java.util.ArrayList;
import java.util.Date;
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
	
	public MonteCarloTreeSearch(Game game, GameState state) {
		this.game = game;
		this.state = state;
	}
	
	public void setCopyState(GameState newState) {
		this.copyState = newState;
		states.push(state);
	}
	
	public MoveScore monteCarloTreeSearch(String player, long timeForSearch) {
		
		Node root = new Node(copyState, null);
		long startTime = System.currentTimeMillis();
		
		while((System.currentTimeMillis() - startTime) < timeForSearch) {
			
			Node bestNode = selection(root);
			Node exploreNode;
			
			if(bestNode.getGameState().hasGameEnded()!=null) {
				expansion(bestNode);
			}
			if(bestNode.getChildren().size()>0) {
				exploreNode = bestNode.getRandomChild();
			}
			
			
		}
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
		
		
		return null;
		
	}
	
	
	private void expansion(Node bestNode) {
		
		ArrayList<Move> validMoves = findValidMoves(bestNode.getGameState().getTurn()); 
		
		for (int i=0; i<validMoves.size(); i++){
			
			tempState = copyState.saveGameState();
			
			Move move = validMoves.get(i);
			simulateMove(move, bestNode.getGameState().getTurn());
			
			Node newNode = new Node(copyState.saveGameState(), bestNode);
			bestNode.addChild(newNode);
			
			copyState = tempState.saveGameState();
			
			
		}
		
		/*let possibleStates = node.state.getAllPossibleStates();
		  possibleStates.forEach(state => {
		    let newNode = new Node(state);
		    newNode.parent = node;
		    newNode.state.playNo = node.state.getOpponent();
		    node.childArray.push(newNode);
		  });*/
	}

	private Node selection(Node node) {
		
		Node n = node;
		if(n.getChildren().isEmpty()) {
			return n;
		}
		
		int bestChildIndex = 0;
		double bestScore = 0;

		for(int i=0; i<n.getChildren().size(); i++) {
			double UCBScore = calculateUCBScore(n.getChildren().get(i));
			if(UCBScore> bestScore) {
				bestScore = UCBScore;
				bestChildIndex = i;
			}
		}
		  
		return n.getChildren().get(bestChildIndex);
	}

	private double calculateUCBScore(Node node) {
		
		double V = node.getTotalScore() / node.getVisits();
		int N = node.getParent().getVisits();
		int n = node.getVisits();
		
		return V + 2*(Math.sqrt(Math.log(N)/n));
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
	
}
