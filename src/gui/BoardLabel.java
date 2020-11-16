package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class BoardLabel extends JLabel {

	int[][] piecePositionsX = {{10,30}, {55,85}, {110,140}, {215, 245}, {325,355}, {380, 410}, {435,460}}; 
	int[][] piecePositionsY = {{10,30}, {55,85}, {110,140}, {215, 245}, {325,355}, {380, 410}, {435,460}};  
	int[][] positionsOnBoard = {{10,10}, {223,10}, {445,10}, {60,60}, {223, 60}, {385,60}, {112,115}, {223, 115}, {330, 115},
			{15, 220}, {65, 220}, {115, 220}, {335, 220}, {390, 220}, {445, 220}, {112, 330}, {223, 330}, {330, 330}, {60, 385},
			{223, 385}, {385, 385}, {10, 440}, {223, 440}, {445, 440}};
	
	public BoardLabel(ImageIcon img) {
		super(img);
		addMouseMotionListener(new TMMMouseAdapter());
	}
	
	public void paintComponent (Graphics g, int point)
    { 
		super.paintComponent(g);
		g.setColor(Color.LIGHT_GRAY);  
		g.fillOval(positionsOnBoard[point][0]-6, positionsOnBoard[point][1]-6, 30, 30);

    }
	
	private class TMMMouseAdapter extends MouseAdapter {
		
		
		//int[][] piecePositionsY = {{0,20}, {45,75}, {205, 235}, {425,450}}; 
        @Override
        public void mouseMoved(MouseEvent e) {
             checkMouseBoundaries(e.getX(), e.getY());	

        }

		private void checkMouseBoundaries(int x, int y) {
			System.out.println("" + x + ", " + y);
			//if((x>piecePositionsX[0][0] && x<piecePositionsX[0][1])
			
			if(inPosition(x, piecePositionsX[0], y, piecePositionsY[0])) {
				//System.out.println("outer top left");
				BoardLabel.this.paintComponent(getGraphics(), 0);
			}
			else if(inPosition(x, piecePositionsX[3], y, piecePositionsY[0])) {
				//System.out.println("outer top middle");
				BoardLabel.this.paintComponent(getGraphics(), 1);
			}
			else if(inPosition(x, piecePositionsX[6], y, piecePositionsY[0])) {
				//System.out.println("outer top right");
				BoardLabel.this.paintComponent(getGraphics(), 2);
			}
			else if(inPosition(x, piecePositionsX[1], y, piecePositionsY[1])) {
				//System.out.println("middle top left");
				BoardLabel.this.paintComponent(getGraphics(), 3);
			}
			else if(inPosition(x, piecePositionsX[3], y, piecePositionsY[1])) {
				//System.out.println("middle top middle");
				BoardLabel.this.paintComponent(getGraphics(), 4);
			}
			else if(inPosition(x, piecePositionsX[5], y, piecePositionsY[1])) {
				//System.out.println("middle top right");
				BoardLabel.this.paintComponent(getGraphics(), 5);
			}
			else if(inPosition(x, piecePositionsX[2], y, piecePositionsY[2])) {
				//System.out.println("inner top left");
				BoardLabel.this.paintComponent(getGraphics(), 6);
			}
			else if(inPosition(x, piecePositionsX[3], y, piecePositionsY[2])) {
				//System.out.println("inner top middle");
				BoardLabel.this.paintComponent(getGraphics(), 7);
			}
			else if(inPosition(x, piecePositionsX[4], y, piecePositionsY[2])) {
				//System.out.println("inner top right");
				BoardLabel.this.paintComponent(getGraphics(), 8);
			}
			else if(inPosition(x, piecePositionsX[0], y, piecePositionsY[3])) {
				//System.out.println("outer centre left");
				BoardLabel.this.paintComponent(getGraphics(), 9);
			}
			else if(inPosition(x, piecePositionsX[1], y, piecePositionsY[3])) {
				//System.out.println("middle centre left");
				BoardLabel.this.paintComponent(getGraphics(), 10);
			}
			else if(inPosition(x, piecePositionsX[2], y, piecePositionsY[3])) {
				//System.out.println("inner centre left");
				BoardLabel.this.paintComponent(getGraphics(), 11);
			}
			else if(inPosition(x, piecePositionsX[4], y, piecePositionsY[3])) {
				//System.out.println("inner centre right");
				BoardLabel.this.paintComponent(getGraphics(), 12);
			}
			else if(inPosition(x, piecePositionsX[5], y, piecePositionsY[3])) {
				//System.out.println("middle centre right");
				BoardLabel.this.paintComponent(getGraphics(), 13);
			}
			else if(inPosition(x, piecePositionsX[6], y, piecePositionsY[3])) {
				//System.out.println("outer centre right");
				BoardLabel.this.paintComponent(getGraphics(), 14);
			}
			else if(inPosition(x, piecePositionsX[2], y, piecePositionsY[4])) {
				//System.out.println("inner bottom left");
				BoardLabel.this.paintComponent(getGraphics(), 15);
			}
			else if(inPosition(x, piecePositionsX[3], y, piecePositionsY[4])) {
				//System.out.println("inner bottom middle");
				BoardLabel.this.paintComponent(getGraphics(), 16);
			}
			else if(inPosition(x, piecePositionsX[4], y, piecePositionsY[4])) {
				//System.out.println("inner bottom right");
				BoardLabel.this.paintComponent(getGraphics(), 17);
			}
			else if(inPosition(x, piecePositionsX[1], y, piecePositionsY[5])) {
				//System.out.println("middle bottom left");
				BoardLabel.this.paintComponent(getGraphics(), 18);
			}
			else if(inPosition(x, piecePositionsX[3], y, piecePositionsY[5])) {
				//System.out.println("middle bottom middle");
				BoardLabel.this.paintComponent(getGraphics(), 19);
			}
			else if(inPosition(x, piecePositionsX[5], y, piecePositionsY[5])) {
				//System.out.println("middle bottom right");
				BoardLabel.this.paintComponent(getGraphics(), 20);
			}
			else if(inPosition(x, piecePositionsX[0], y, piecePositionsY[6])) {
				//System.out.println("outer bottom left");
				BoardLabel.this.paintComponent(getGraphics(), 21);
			}
			else if(inPosition(x, piecePositionsX[3], y, piecePositionsY[6])) {
				//System.out.println("outer bottom middle");
				BoardLabel.this.paintComponent(getGraphics(), 22);
			}
			else if(inPosition(x, piecePositionsX[6], y, piecePositionsY[6])) {
				//System.out.println("outer bottom right");
				BoardLabel.this.paintComponent(getGraphics(), 23);
			}
			else {
				BoardLabel.this.repaint();
			}
			
		}

		private boolean inPosition(int x, int[] positionsX, int y, int[] positionsY) {
			if((x>positionsX[0] && x<positionsX[1]) && (y>positionsY[0] && y<positionsY[1])) 
				return true;				
			else
				return false;
		}
		
    }
}
