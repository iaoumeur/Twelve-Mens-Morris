package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public class Game {

	static final int frameWidth = 1000;
	static final int frameHeight = 750;
	static final int numberOfPieces = 12;
	private JFrame frame;
	private JLayeredPane lpane;
	
	private BoardLabel board;
	private ArrayList<JLabel> whitePieces;
	private ArrayList<JLabel> blackPieces;
	private JPanel whitePiecePanel;
	private JPanel blackPiecePanel;
	
	ImageIcon whitePieceImage; 
	ImageIcon blackPieceImage;
	
	private String turn = "white";
	
	public Game(String p1Name, String p2Name) {
		
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
		board = new BoardLabel(boardImage, this);
		boardPanel.setBounds(250, 100, boardImage.getIconWidth(), boardImage.getIconHeight());
		boardPanel.add(board, BorderLayout.CENTER);
		boardPanel.setOpaque(false);
		
		whitePieceImage = new ImageIcon(this.getClass().getResource("/WhitePiece.png"));
		whitePieces = new ArrayList<JLabel>();
		whitePiecePanel = new JPanel();
		for(int i=0; i<numberOfPieces; i++) {
			whitePieces.add(new JLabel(whitePieceImage));
			whitePiecePanel.add(whitePieces.get(i));
		}
		whitePiecePanel.setBounds(165, 120, 70, 300);
		whitePiecePanel.setOpaque(false);
		
		blackPieceImage = new ImageIcon(this.getClass().getResource("/BlackPiece.png"));
		blackPieces = new ArrayList<JLabel>();
		blackPiecePanel = new JPanel();
		for(int i=0; i<numberOfPieces; i++) {
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
		//lpane.add(exitPanel, 1, 0);
		
		
	}
	
	public String getTurn() {
		return turn;
	}
	
	public void switchTurn() {
		if(turn=="white") 
			turn = "black";
		else if(turn=="black")
			turn = "white";
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

	public boolean isFinished() {
		return false;
	}

	/*public void showRemainingPieces(int numWhite, int numBlack) {
		System.out.println(numWhite);
		int whitePiecesLeft = numberOfPieces-numWhite;
		whitePieces = new JLabel[whitePiecesLeft];
		whitePiecePanel.removeAll();
		for(int i=0; i<whitePiecesLeft; i++) {
			whitePieces[i] = new JLabel(whitePieceImage);
			whitePiecePanel.add(whitePieces[i]);
		}
		whitePiecePanel.repaint();
		
		int blackPiecesLeft = numberOfPieces-numBlack;
		blackPieces = new JLabel[blackPiecesLeft];
		blackPiecePanel.removeAll();
		for(int i=0; i<blackPiecesLeft; i++) {
			blackPieces[i] = new JLabel(blackPieceImage);
			blackPiecePanel.add(blackPieces[i]);
		}
		
		
		
	}*/
}
