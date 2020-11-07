package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public class MainMenu {

	static final int frameWidth = 1000;
	static final int frameHeight = 750;
	private JFrame frame;
	private JLayeredPane lpane = new JLayeredPane();

	public MainMenu() {

		frame = new JFrame("Main Menu");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(0, 0, frameWidth, frameHeight);
		frame.setLocationRelativeTo(null);
		frame.add(lpane, BorderLayout.CENTER);
		frame.setVisible(true);

		lpane.setBounds(0, 0, frameWidth, frameHeight);

		JPanel backgroundPanel = new JPanel();
		backgroundPanel.setBounds(0, 0, frameWidth, frameHeight);
		ImageIcon backgroundImage = new ImageIcon(this.getClass().getResource("/woodBackground.jpg"));
		JLabel backgroundLabel = new JLabel(backgroundImage);
		backgroundPanel.add(backgroundLabel);
		
		JPanel extraPanel = new JPanel();
		ImageIcon boardImage = new ImageIcon(this.getClass().getResource("/12 Men's Morris.png"));
		JLabel boardLabel = new JLabel(boardImage);
		extraPanel.setBounds(420, 180, boardImage.getIconWidth(), boardImage.getIconHeight());
		extraPanel.add(boardLabel, BorderLayout.EAST);
		extraPanel.setOpaque(false);
		
		lpane.add(backgroundPanel, 0, 0);
		lpane.add(extraPanel, 1, 0);
		
		

	}

}
