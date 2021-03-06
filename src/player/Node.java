package player;

import java.util.ArrayList;
import java.util.Random;

import state.GameState;

public class Node {

	GameState state;
	int totalScore;
	int visits;
	
	Node parent;
	ArrayList<Node> children;
	
	public Node(GameState state, Node parent) {
		this.state = state;
		this.parent = parent;
	}
	
	public void setGameState(GameState state) {
		this.state = state;
	}
	
	public void setTotalScore(int value) {
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

	public int getTotalScore() {
		return totalScore;
	}

	public int getVisits() {
		return visits;
	}

	public Node getRandomChild() {
		Random rand = new Random();
		int randomIndex = rand.nextInt(children.size());
		return children.get(randomIndex);
	}
	
}
