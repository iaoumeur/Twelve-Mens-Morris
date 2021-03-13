package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import player.Computer;
import player.Minimax;
import player.MonteCarloTreeSearch;
import state.GameState;

public class Game {
	
	static final int frameWidth = 1000;
	static final int frameHeight = 750;
	private JFrame frame;
	private JLayeredPane lpane;
	
	private GameState state;
	private Board board;
	//private Minimax computer;
	//private Minimax otherComputer;
	private Computer computer;
	private Computer otherComputer;
	private ArrayList<JLabel> whitePieces;
	private ArrayList<JLabel> blackPieces;
	private JPanel whitePiecePanel;
	private JPanel blackPiecePanel;
	private JLabel turnLabel;
	private JLabel msgLabel;
	private JLabel thinkingLabel;
	
	ImageIcon whitePieceImage; 
	ImageIcon blackPieceImage;
	
	private boolean stopPainting = false;

	private String[] gameMessages = {"A Mill is formed! Select a piece not in a mill to remove", "Invalid Piece Removal", 
			"A mill was formed, you may select a piece inside a mill to remove", "The winner is White", "The winner is Black", "The game is a draw", 
			"There have been 50 moves without a mill being made, the game is a draw."};
	private String[] turnMessages = {"<html><span style=\"font-size:23px;color:rgb(211,211,211);font-weight: bold;"
			+ "\">Turn:   </span><span style=\"color:white;font-size:23px;\">White</span></html>", 
			"<html><span style=\"font-size:23px;color:rgb(211,211,211);font-weight: bold;"
					+ "\">Turn:   </span><span style=\"color:black;font-size:23px;\">Black</span></html>"};
	
	public Game(String p1Name, String p2Name, String gameType, String computerType) {
		
		state = new GameState();
		if(gameType=="pvAI") {
			if(computerType=="MCTS") {
				computer = new MonteCarloTreeSearch(this, state);
			}
			else {
				computer = new Minimax(this, state);
			}
		}
		if(gameType=="AIvAI") {
			computer = new Minimax(this, state);
			otherComputer = new MonteCarloTreeSearch(this, state);
		}
		
		frame = new JFrame("Twelve Men's Morris");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(0, 0, frameWidth, frameHeight);
		frame.setLocationRelativeTo(null);
		lpane = new JLayeredPane();
		frame.add(lpane, BorderLayout.CENTER);
		frame.setResizable(false);
		frame.setVisible(true);

		lpane.setBounds(0, 0, frameWidth, frameHeight);
		
		JPanel backgroundPanel = new JPanel();
		backgroundPanel.setBounds(0, 0, frameWidth, frameHeight);
		ImageIcon backgroundImage = new ImageIcon(this.getClass().getResource("/GameBackground.jpg"));
		JLabel backgroundLabel = new JLabel(backgroundImage);
		backgroundPanel.add(backgroundLabel);
		
		JPanel boardPanel = new JPanel();
		ImageIcon boardImage = new ImageIcon(this.getClass().getResource("/BoardInvisible.png"));
		board = new Board(boardImage, this);
		boardPanel.setBounds(250, 100, boardImage.getIconWidth(), boardImage.getIconHeight());
		boardPanel.add(board, BorderLayout.CENTER);
		boardPanel.setOpaque(false);
		
		whitePieceImage = new ImageIcon(this.getClass().getResource("/WhitePiece.png"));
		whitePieces = new ArrayList<JLabel>();
		whitePiecePanel = new JPanel();
		for(int i=0; i<state.numberOfPieces; i++) {
			whitePieces.add(new JLabel(whitePieceImage));
			whitePiecePanel.add(whitePieces.get(i));
		}
		whitePiecePanel.setBounds(165, 120, 70, 300);
		whitePiecePanel.setOpaque(false);
		
		blackPieceImage = new ImageIcon(this.getClass().getResource("/BlackPiece.png"));
		blackPieces = new ArrayList<JLabel>();
		blackPiecePanel = new JPanel();
		for(int i=0; i<state.numberOfPieces; i++) {
			blackPieces.add(new JLabel(blackPieceImage));
			blackPiecePanel.add(blackPieces.get(i));
		}
		blackPiecePanel.setBounds(730, 345, 70, 300);
		blackPiecePanel.setOpaque(false);
		
		JPanel p1Panel = new JPanel();
		String p1Text = "<html><span style=\"font-family:Arial;font-size:32px;color:white;font-weight: bold;"
				+ "\">Player 1</span><br><span style=\"color:white;font-size:16px;font-style:italic\">" + p1Name +
				"</span></html>";
		JLabel p1Label = new JLabel(p1Text);
		p1Panel.setBounds(270, 10, 200, 300);
		p1Panel.add(p1Label, BorderLayout.CENTER);
		p1Panel.setOpaque(false);
		
		JPanel p2Panel = new JPanel();
		String p2Text = "<html><span style=\"font-family:Arial;font-size:32px;color:black;font-weight: bold;"
				+ "\">Player 2</span><br><span style=\"color:black;font-size:16px;font-style:italic\">" + p2Name + 
				"</span></html>";
		JLabel p2Label = new JLabel(p2Text);
		p2Panel.setBounds(500, 10, 200, 300);
		p2Panel.add(p2Label, BorderLayout.CENTER);
		p2Panel.setOpaque(false);
		
		JPanel msgPanel = new JPanel();
		msgLabel= new JLabel();
		msgPanel.setBounds(30, 650, 650, 100);
		msgPanel.add(msgLabel, BorderLayout.CENTER);
		msgPanel.setOpaque(false);
		
		JPanel turnPanel = new JPanel();
		turnLabel = new JLabel(turnMessages[0]);
		turnLabel.setForeground(Color.WHITE);
		turnPanel.setBounds(700, 650, 300, 100);
		turnPanel.add(turnLabel, BorderLayout.CENTER);
		turnPanel.setOpaque(false);
		
		JPanel thinkingPanel = new JPanel();
		thinkingLabel = new JLabel("Computer Thinking...");
		thinkingLabel.setForeground(Color.BLACK);
		thinkingPanel.setBounds(330, 320, 300, 100);
		thinkingPanel.add(thinkingLabel, BorderLayout.CENTER);
		thinkingPanel.setOpaque(false);
		thinkingPanel.setVisible(false);
		
		
		/*JPanel exitPanel = new JPanel();
		ImageIcon exitImage = new ImageIcon(this.getClass().getResource("/ExitButton.png"));
		exitPanel.setBounds(frameWidth - exitImage.getIconWidth()-30, exitImage.getIconHeight()-40, exitImage.getIconWidth(), exitImage.getIconHeight()+10);
		JButton exitButton = new JButton(exitImage);
		exitPanel.add(exitButton);
		exitPanel.setOpaque(false);
		exitButton.setBorderPainted( false );*/
		
		
		
		lpane.add(backgroundPanel, 0, 0);
		lpane.add(boardPanel, 1, 0);
		lpane.add(whitePiecePanel, 1, 0);
		lpane.add(blackPiecePanel, 1, 0);
		lpane.add(p1Panel, 1, 0);
		lpane.add(p2Panel, 1, 0);
		lpane.add(msgPanel, 1, 0);
		lpane.add(turnPanel,1, 0);
		lpane.add(thinkingPanel, 1, 0);
		//lpane.add(exitPanel, 1, 0);
		
	}
	
	public GameState getState() {
		return state;
	}
	
	public Computer getComputer() {
		return computer;
	}
	
	public Computer getOtherComputer() {
		return otherComputer;
	}
	
	public boolean getStopPaining() {
		return stopPainting;
	}
	
	public void setStopPaining(boolean painting) {
		stopPainting = painting;
	}
	
	
	public void switchTurn() {
		if(state.getTurn()=="white") { 
			state.setTurn("black");
			turnLabel.setText(turnMessages[1]);
		}
		else if(state.getTurn()=="black") {
			state.setTurn("white");
			turnLabel.setText(turnMessages[0]);
		}
	}
	
	public void removeWhitePieceFromPanel() {
		if(whitePieces.size()==0) {
			return;
		}
		whitePieces.remove(whitePieces.size()-1);
		whitePieces.trimToSize();
		whitePiecePanel.removeAll();
		for(int i=0; i<whitePieces.size(); i++) {
			whitePiecePanel.add(whitePieces.get(i));				
		}
		whitePiecePanel.repaint();
	}
	
	public void removeBlackPieceFromPanel() {
		if(blackPieces.size()==0) {
			return;
		}
		blackPieces.remove(blackPieces.size()-1);
		blackPieces.trimToSize();
		blackPiecePanel.removeAll();
		for(int i=0; i<blackPieces.size(); i++) {
			blackPiecePanel.add(blackPieces.get(i));				
		}
		blackPiecePanel.repaint();
	}


	public void showGameMessage() {
		if(state.getGameStage()==4) {
			msgLabel.setText("<html><span style=\"font-size:16px;color:white;"
					+ "\">" + gameMessages[0] + "</span></html>");
		}
		else {
			msgLabel.setText("");
		}
	}
	
	public void setGameStage(int stage) {
		state.setGameStage(stage);
	}
	
	public void setGameState(GameState newState) {
		state = newState;
	}
	
	public Board getBoard() {
		return board;
	}
	
	public void displayMessage(int messageNumber) {
		msgLabel.setText("<html><span style=\"font-size:16px;color:white;"
				+ "\">" + gameMessages[messageNumber] + "</span></html>");
	}

	public void showThinking() {
		thinkingLabel.setVisible(true);
	}
	
	public void hideThinking() {
		thinkingLabel.setVisible(false);
	}




}
