package gui;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public class Game {

	static final int frameWidth = 1000;
	static final int frameHeight = 750;
	private JFrame frame;
	private JLayeredPane lpane;
	
	public Game() {
		
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
		BoardLabel boardLabel = new BoardLabel(boardImage);
		boardPanel.setBounds(250, 100, boardImage.getIconWidth(), boardImage.getIconHeight());
		boardPanel.add(boardLabel, BorderLayout.CENTER);
		boardPanel.setOpaque(false);
		
		/*subclass label and override paint component and make it call super.paintComponent() <- make sure this still works
		 * if this works, add custom draw code to draw each of the pieces
		 * check cursor (override mouse handler to store current x and y coordinates, repaint()), if the cursor moves to where a piece should be, put a "ghosted piece" to represent where a piece could be placed.
		 * 
		 * "if you click here this is where the piece appears" <- ghosted piece 
		 * 
		 * *if i want draggable, use mouseDown and mouseUp to check when mouse is being held to draw piece image below mouse.
		 */
		
		lpane.add(backgroundPanel, 0, 0);
		lpane.add(boardPanel, 1, 0);
		
	}
}
