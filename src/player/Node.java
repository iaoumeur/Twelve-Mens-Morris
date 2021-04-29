package player;

import java.util.ArrayList;
import java.util.Random;

import state.GameState;

public class Node {

	GameState state;
	double totalScore;
	int visits;
	MoveScore action;
	
	Node parent;
	ArrayList<Node> children = new ArrayList<Node>();
	
	public Node(GameState state, Node parent) {
		this.state = state;
		this.parent = parent;
		totalScore = 0;
		visits = 0;
	}
	
	public void setGameState(GameState state) {
		this.state = state;
	}
	
	public void setTotalScore(double value) {
		this.totalScore = value;
	}
	
	public void incrementVisits() {
		this.visits++;
	}
	
	public void addChild(Node node) {
		this.children.add(node);
	}
	
	public GameState getGameState() {
		return state;
	}
	
	public ArrayList<Node> getChildren() {
		return children;
	}
	
	public Node getParent() {
		return parent;
	}

	public double getTotalScore() {
		return totalScore;
	}

	public int getVisits() {
		return visits;
	}
	
	public void setAction(MoveScore move) {
		action = move;
	}
	
	public MoveScore getAction() {
		return action;
	}

	public Node getRandomChild() {
		Random rand = new Random();
		int randomIndex = rand.nextInt(children.size());
		return children.get(randomIndex);
	}

	public Node getBestChild() {
		if(children.isEmpty()) {
			return null;
		}
		int bestChildIndex = 0;
		double bestScore = -10000;
		for(int i=0; i<children.size(); i++) {
			if(children.get(i).getTotalScore()>bestScore) {
				bestChildIndex = i;
				bestScore = children.get(i).getTotalScore();
			}
		}
		return children.get(bestChildIndex);
	}
	
	
}
