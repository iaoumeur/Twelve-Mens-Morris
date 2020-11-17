package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
	private JLabel[] whitePieces;
	private JLabel[] blackPieces;
	
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
		ImageIcon backgroundImage = new ImageIcon(this.getClass().getResource("/woodBackground.jpg"));
		JLabel backgroundLabel = new JLabel(backgroundImage);
		backgroundPanel.add(backgroundLabel);
		
		JPanel boardPanel = new JPanel();
		ImageIcon boardImage = new ImageIcon(this.getClass().getResource("/tmmDiagonalBoardColor.png"));
		board = new BoardLabel(boardImage, this);
		boardPanel.setBounds(250, 100, boardImage.getIconWidth(), boardImage.getIconHeight());
		boardPanel.add(board, BorderLayout.CENTER);
		boardPanel.setOpaque(false);
		
		ImageIcon whitePieceImage = new ImageIcon(this.getClass().getResource("/WhitePiece.png"));
		whitePieces = new JLabel[numberOfPieces];
		JPanel whitePiecePanel = new JPanel();
		for(int i=0; i<numberOfPieces; i++) {
			whitePieces[i] = new JLabel(whitePieceImage);
			whitePiecePanel.add(whitePieces[i]);
		}
		whitePiecePanel.setBounds(165, 120, 70, 300);
		whitePiecePanel.setOpaque(false);
		
		ImageIcon blackPieceImage = new ImageIcon(this.getClass().getResource("/BlackPiece.png"));
		blackPieces = new JLabel[numberOfPieces];
		JPanel blackPiecePanel = new JPanel();
		for(int i=0; i<numberOfPieces; i++) {
			blackPieces[i] = new JLabel(blackPieceImage);
			blackPiecePanel.add(blackPieces[i]);
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
}
