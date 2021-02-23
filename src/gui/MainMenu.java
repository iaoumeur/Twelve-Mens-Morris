package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class MainMenu {

	static final int frameWidth = 1000;
	static final int frameHeight = 750;
	private JFrame frame;
	private JLayeredPane lpane;
	private Color boardColor = new Color(185, 122, 87);
	private boolean difficultiesVisible = false;
	
	private boolean ready = false;
	private String p1Name = "NoName";
	private String p2Name = "NoName";
	private String gameType = "pvp";

	public MainMenu() {

		frame = new JFrame("Main Menu");
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
		ImageIcon boardImage = new ImageIcon(this.getClass().getResource("/12 Men's Morris.png"));
		JLabel boardLabel = new JLabel(boardImage);
		boardPanel.setBounds(420, 180, boardImage.getIconWidth(), boardImage.getIconHeight());
		boardPanel.add(boardLabel, BorderLayout.EAST);
		boardPanel.setOpaque(false);
		
		JPanel titlePanel = new JPanel();
		ImageIcon titleImage = new ImageIcon(this.getClass().getResource("/Title.png"));
		JLabel titleLabel = new JLabel(titleImage);
		titlePanel.setBounds(110, 50, titleImage.getIconWidth(), titleImage.getIconHeight());
		titlePanel.add(titleLabel, BorderLayout.NORTH);
		titlePanel.setOpaque(false);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBounds(120, 200, 200, 150);
		buttonPanel.setOpaque(false);
		
		JPanel buttonPanel2 = new JPanel();
		buttonPanel2.setBounds(120, 300, 200, 150);
		buttonPanel2.setOpaque(false);
		
		JPanel buttonPanel3 = new JPanel();
		buttonPanel3.setBounds(70, 400, 300, 150);
		buttonPanel3.setOpaque(false);
		buttonPanel3.setVisible(false);
		
		JPanel buttonPanel4 = new JPanel();
		buttonPanel4.setBounds(120, 400, 200, 150);
		buttonPanel4.setOpaque(false);
		
		
		JButton pvpButton = new JButton("Player vs. Player");
		pvpButton = setupButton(pvpButton);
		JButton pvAIButton = new JButton("Player vs. AI");
		pvAIButton = setupButton(pvAIButton);
		JButton easyButton = new JButton("Easy");
		easyButton.setBackground(Color.GREEN);
		easyButton.setPreferredSize(new Dimension(90, 30));
		JButton mediumButton = new JButton("Medium");
		mediumButton.setBackground(Color.YELLOW);
		mediumButton.setPreferredSize(new Dimension(90, 30));
		JButton hardButton = new JButton("Hard");
		hardButton.setBackground(Color.RED);
		hardButton.setPreferredSize(new Dimension(90, 30));
		JButton aivaiButton = new JButton("AI vs AI");
		aivaiButton = setupButton(aivaiButton);
		
		buttonPanel.add(pvpButton, BorderLayout.NORTH);
		buttonPanel2.add(pvAIButton, BorderLayout.NORTH);
		buttonPanel3.add(easyButton, BorderLayout.WEST);
		buttonPanel3.add(mediumButton, BorderLayout.WEST);
		buttonPanel3.add(hardButton, BorderLayout.WEST);
		buttonPanel4.add(aivaiButton, BorderLayout.NORTH);
		
		lpane.add(backgroundPanel, 0, 0);
		lpane.add(boardPanel, 1, 0);
		lpane.add(titlePanel, 1, 0);
		lpane.add(buttonPanel, 1, 0);
		lpane.add(buttonPanel2, 1, 0);
		lpane.add(buttonPanel3, 1, 0);
		lpane.add(buttonPanel4, 1, 0);
		
		
		pvpButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				gameType = "pvp";
				enterNamesAndStart();
			}

		});
		
		pvAIButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				difficultiesVisible = !difficultiesVisible;
				if(difficultiesVisible) {
					buttonPanel4.setBounds(120, 450, 200, 150);
					buttonPanel3.setVisible(true);
				}
				else {
					buttonPanel3.setVisible(false);
					buttonPanel4.setBounds(120, 400, 200, 150);
				}
			}

		});
		
		aivaiButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				gameType = "AIvAI";
				enterNamesAndStart();
			}

		});
		
		easyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				gameType = "pvAI";
				enterNamesAndStart();
			}

		});

	}
	

	public JButton setupButton(JButton btn) {
		btn.setBackground(boardColor);
		btn.setPreferredSize(new Dimension(175, 75));
		btn.setBorder(new LineBorder(Color.BLACK, 3));
		return btn;
	}
	
	protected void enterNamesAndStart() {
		
		 /*p1Name = JOptionPane.showInputDialog("Enter Player 1's Name:");
		 if(gameType=="pvp") {
			 p2Name = JOptionPane.showInputDialog("Enter Player 2's Name:");
		 }
		 else {
			 p2Name = ("AI");
		 }*/
		 p1Name = "Ishaq";
		 p2Name = "Rival";
		 ready = true;
		 frame.dispose();
		
	}
	
	public boolean isReady() {
		return ready;
	}
	
	public String getP1Name() {
		return p1Name;
	}
	
	public String getP2Name() {
		return p2Name;
	}
	
	public String getGameType() {
		return gameType;
	}
	

}
