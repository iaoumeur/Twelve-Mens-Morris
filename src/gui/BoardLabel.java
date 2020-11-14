package gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class BoardLabel extends JLabel {

	public BoardLabel(ImageIcon img) {
		super(img);
		addMouseMotionListener(new TMMMouseAdapter());
	}
	
	private class TMMMouseAdapter extends MouseAdapter {
		
		int[][] piecePositionsX = {{0,20}, {45,75}, {100,130}, {205, 235}, {315,345}, {370, 400}, {425,450}}; 
		int[][] piecePositionsY = {{0,20}, {45,75}, {100,130}, {205, 235}, {315,345}, {370, 400}, {425,450}}; 
		//int[][] piecePositionsY = {{0,20}, {45,75}, {205, 235}, {425,450}}; 
        @Override
        public void mouseMoved(MouseEvent e) {
             checkMouseBoundaries(e.getX(), e.getY());	

        }

		private void checkMouseBoundaries(int x, int y) {
			//System.out.println("" + x + ", " + y);
			//if((x>piecePositionsX[0][0] && x<piecePositionsX[0][1])
			
			if(inPosition(x, piecePositionsX[0], y, piecePositionsY[0])) {
				System.out.println("outer top left");
			}
			else if(inPosition(x, piecePositionsX[3], y, piecePositionsY[0])) {
				System.out.println("outer top middle");
			}
			else if(inPosition(x, piecePositionsX[6], y, piecePositionsY[0])) {
				System.out.println("outer top right");
			}
			else if(inPosition(x, piecePositionsX[1], y, piecePositionsY[1])) {
				System.out.println("middle top left");
			}
			else if(inPosition(x, piecePositionsX[3], y, piecePositionsY[1])) {
				System.out.println("middle top middle");
			}
			else if(inPosition(x, piecePositionsX[5], y, piecePositionsY[1])) {
				System.out.println("middle top right");
			}
			else if(inPosition(x, piecePositionsX[2], y, piecePositionsY[2])) {
				System.out.println("inner top left");
			}
			else if(inPosition(x, piecePositionsX[3], y, piecePositionsY[2])) {
				System.out.println("inner top middle");
			}
			else if(inPosition(x, piecePositionsX[4], y, piecePositionsY[2])) {
				System.out.println("inner top right");
			}
			else if(inPosition(x, piecePositionsX[0], y, piecePositionsY[3])) {
				System.out.println("outer centre left");
			}
			else if(inPosition(x, piecePositionsX[1], y, piecePositionsY[3])) {
				System.out.println("middle centre left");
			}
			else if(inPosition(x, piecePositionsX[2], y, piecePositionsY[3])) {
				System.out.println("inner centre left");
			}
			else if(inPosition(x, piecePositionsX[4], y, piecePositionsY[3])) {
				System.out.println("inner centre right");
			}
			else if(inPosition(x, piecePositionsX[5], y, piecePositionsY[3])) {
				System.out.println("middle centre right");
			}
			else if(inPosition(x, piecePositionsX[6], y, piecePositionsY[3])) {
				System.out.println("outer centre right");
			}
			else if(inPosition(x, piecePositionsX[2], y, piecePositionsY[4])) {
				System.out.println("inner bottom left");
			}
			else if(inPosition(x, piecePositionsX[3], y, piecePositionsY[4])) {
				System.out.println("inner bottom middle");
			}
			else if(inPosition(x, piecePositionsX[4], y, piecePositionsY[4])) {
				System.out.println("inner bottom right");
			}
			else if(inPosition(x, piecePositionsX[1], y, piecePositionsY[5])) {
				System.out.println("middle bottom left");
			}
			else if(inPosition(x, piecePositionsX[3], y, piecePositionsY[5])) {
				System.out.println("middle bottom middle");
			}
			else if(inPosition(x, piecePositionsX[5], y, piecePositionsY[5])) {
				System.out.println("middle bottom right");
			}
			else if(inPosition(x, piecePositionsX[0], y, piecePositionsY[6])) {
				System.out.println("outer bottom left");
			}
			else if(inPosition(x, piecePositionsX[3], y, piecePositionsY[6])) {
				System.out.println("outer bottom middle");
			}
			else if(inPosition(x, piecePositionsX[6], y, piecePositionsY[6])) {
				System.out.println("outer bottom right");
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
