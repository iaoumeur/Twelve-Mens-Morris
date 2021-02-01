package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Board extends JLabel {

	
	private Game game;
	private BufferedImage img;
	private TMMMouseAdapter tmmMouseAdapter;
	
	//constant x and y values for 2d array
	
	private int[][] piecePositionsX = {{10,30}, {55,85}, {110,140}, {215, 245}, {325,355}, {380, 410}, {435,460}}; 
	private int[][] piecePositionsY = {{10,30}, {55,85}, {110,140}, {215, 245}, {325,355}, {380, 410}, {435,460}};  
	private int[][] positionsOnBoard = {{10,10}, {223,10}, {445,10}, {60,60}, {223, 60}, {385,60}, {112,115}, {223, 115}, {330, 115},
			{15, 220}, {65, 220}, {115, 220}, {335, 220}, {390, 220}, {445, 220}, {112, 330}, {223, 330}, {330, 330}, {60, 385},
			{223, 385}, {385, 385}, {10, 440}, {223, 440}, {445, 440}};
	
	int numWhite = 0;
	int numBlack = 0;
	
	public Board(ImageIcon img, Game game) {
		super(img);
		this.game = game;
		tmmMouseAdapter = new TMMMouseAdapter();
		this.addMouseMotionListener(tmmMouseAdapter);
		this.addMouseListener(tmmMouseAdapter);
	}
	
	public void paintComponent (Graphics g)
    { 
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g.create();
		if (img == null) {
	        img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
		}
		
	    g2d.drawImage(img, 0, 0, this);
	    g2d.dispose();
		
    }
	
	public void paintComponent (Graphics g, int point)
    { 
		super.paintComponent(g);
		if(game.getState().getBoardPieces()[point]==null && game.getState().getGameStage()==1) {
			g.setColor(Color.LIGHT_GRAY);  
			g.fillOval(positionsOnBoard[point][0]-6, positionsOnBoard[point][1]-6, 30, 30);			
		}
		else if(game.getState().getBoardPieces()[point]!=null && game.getState().getGameStage()==2) {
			if(game.getState().getBoardPieces()[point]=="white") {
				g.setColor(Color.WHITE);  
			}
			else {
				g.setColor(Color.BLACK);  
			}
			g.fillOval(positionsOnBoard[point][0]-11, positionsOnBoard[point][1]-11, 40, 40);			
		}

    }
	
	public void paintComponent (Graphics g, int point, String color)
    { 
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g.create();
		if (img == null) {
	        img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
		}
		
		Graphics2D gbuffer = img.createGraphics();
		
		if(color=="white") {
			gbuffer.setColor(Color.WHITE);
			gbuffer.fillOval(positionsOnBoard[point][0]-6, positionsOnBoard[point][1]-6, 30, 30);
		}
		else if(color=="black"){
			gbuffer.setColor(Color.BLACK); 
			gbuffer.fillOval(positionsOnBoard[point][0]-6, positionsOnBoard[point][1]-6, 30, 30);
		}
		else if(color=="green") {
			gbuffer.setColor(Color.GREEN); 
			gbuffer.fillOval(positionsOnBoard[point][0]-6, positionsOnBoard[point][1]-6, 30, 30);
		}
		else if(color == "smallgreen") {
			gbuffer.setColor(Color.GREEN); 
			gbuffer.fillOval(positionsOnBoard[point][0]+4, positionsOnBoard[point][1]+4, 10, 10);
		}
		
		gbuffer.dispose();
	    g2d.drawImage(img, 0, 0, this);
	    g2d.dispose();
		

    }
	
	private class TMMMouseAdapter extends MouseAdapter {
		
        @Override
        public void mouseMoved(MouseEvent e) {
             checkMouseBoundaries(e.getX(), e.getY());	

        }
        
        @Override
        public void mousePressed(MouseEvent e ) {
        	//check which piece position has been clicked 
        	int piecePosition = checkMouseBoundaries(e.getX(), e.getY());
        	
        	String action = game.getState().boardMouseClick(piecePosition);
        	game.showGameMessage();
        	
        	if(action.equals("whitePlaced")) {
        		game.removeWhitePieceFromPanel();
        		game.switchTurn();
        	}
        	else if(action.equals("whitePlacedMill")) {
        		game.removeWhitePieceFromPanel();
        	}
        	else if(action.equals("blackPlaced")) {
        		game.removeBlackPieceFromPanel();
        		game.switchTurn();
        	}
        	else if(action.equals("blackPlacedMill")) {
        		game.removeBlackPieceFromPanel();
        	}
        	else if(action.equals("whiteRemoval") || action.equals("blackRemoval")) {
        		game.switchTurn();
        	}
        	else if(action.equals("invalidRemoval")) {
        		game.displayMessage(1);
        	}
        	else if(action.equals("millNoRemoval")) {
        		game.displayMessage(2);
        	}
        	else if(action.equals("validPieceSelected")) {
        		
        	}
        	else if(action.equals("resetPieceSelected")) {
        		
        	}
        	else if(action.equals("pieceMoved")) {
        		game.switchTurn();
        	}
        	else if(action=="white") {
        		game.displayMessage(3);
        		game.getState().setGameStage(5);
        	}
        	else if(action=="black") {
        		game.displayMessage(4);
        		game.getState().setGameStage(5);
        	}
        	else if(action=="draw") {
        		game.displayMessage(5);
        		game.getState().setGameStage(5);
        	}
        	/*System.out.println();
        	String output = " Board Pieces: [";
    		for(int i=0; i<game.getState().getBoardPieces().length; i++) {
    			output += game.getState().getBoardPieces()[i]+", ";	
    		}
    		output += "]";
    		System.out.println(output);
    		output = " Mills: [";
    		for(int i=0; i<game.getState().getMillsFound().length; i++) {
    			output += game.getState().getMillsFound()[i]+", ";	
    		}
    		output += "]";
    		System.out.println(output);*/
        	repaintPieces();

        }

	
		private int checkMouseBoundaries(int x, int y) {
			//System.out.println("" + x + ", " + y);
			
			if(inPosition(x, piecePositionsX[0], y, piecePositionsY[0])) {
				//System.out.println("outer top left");
				Board.this.paintComponent(getGraphics(), 0);
				return 0;
			}
			else if(inPosition(x, piecePositionsX[3], y, piecePositionsY[0])) {
				//System.out.println("outer top middle");
				Board.this.paintComponent(getGraphics(), 1);
				return 1;
			}
			else if(inPosition(x, piecePositionsX[6], y, piecePositionsY[0])) {
				//System.out.println("outer top right");
				Board.this.paintComponent(getGraphics(), 2);
				return 2;
			}
			else if(inPosition(x, piecePositionsX[1], y, piecePositionsY[1])) {
				//System.out.println("middle top left");
				Board.this.paintComponent(getGraphics(), 3);
				return 3;
			}
			else if(inPosition(x, piecePositionsX[3], y, piecePositionsY[1])) {
				//System.out.println("middle top middle");
				Board.this.paintComponent(getGraphics(), 4);
				return 4; 
			}
			else if(inPosition(x, piecePositionsX[5], y, piecePositionsY[1])) {
				//System.out.println("middle top right");
				Board.this.paintComponent(getGraphics(), 5);
				return 5;
			}
			else if(inPosition(x, piecePositionsX[2], y, piecePositionsY[2])) {
				//System.out.println("inner top left");
				Board.this.paintComponent(getGraphics(), 6);
				return 6;
			}
			else if(inPosition(x, piecePositionsX[3], y, piecePositionsY[2])) {
				//System.out.println("inner top middle");
				Board.this.paintComponent(getGraphics(), 7);
				return 7;
			}
			else if(inPosition(x, piecePositionsX[4], y, piecePositionsY[2])) {
				//System.out.println("inner top right");
				Board.this.paintComponent(getGraphics(), 8);
				return 8;
			}
			else if(inPosition(x, piecePositionsX[0], y, piecePositionsY[3])) {
				//System.out.println("outer centre left");
				Board.this.paintComponent(getGraphics(), 9);
				return 9;
			}
			else if(inPosition(x, piecePositionsX[1], y, piecePositionsY[3])) {
				//System.out.println("middle centre left");
				Board.this.paintComponent(getGraphics(), 10);
				return 10;
			}
			else if(inPosition(x, piecePositionsX[2], y, piecePositionsY[3])) {
				//System.out.println("inner centre left");
				Board.this.paintComponent(getGraphics(), 11);
				return 11;
			}
			else if(inPosition(x, piecePositionsX[4], y, piecePositionsY[3])) {
				//System.out.println("inner centre right");
				Board.this.paintComponent(getGraphics(), 12);
				return 12;
			}
			else if(inPosition(x, piecePositionsX[5], y, piecePositionsY[3])) {
				//System.out.println("middle centre right");
				Board.this.paintComponent(getGraphics(), 13);
				return 13;
			}
			else if(inPosition(x, piecePositionsX[6], y, piecePositionsY[3])) {
				//System.out.println("outer centre right");
				Board.this.paintComponent(getGraphics(), 14);
				return 14;
			}
			else if(inPosition(x, piecePositionsX[2], y, piecePositionsY[4])) {
				//System.out.println("inner bottom left");
				Board.this.paintComponent(getGraphics(), 15);
				return 15;
			}
			else if(inPosition(x, piecePositionsX[3], y, piecePositionsY[4])) {
				//System.out.println("inner bottom middle");
				Board.this.paintComponent(getGraphics(), 16);
				return 16;
			}
			else if(inPosition(x, piecePositionsX[4], y, piecePositionsY[4])) {
				//System.out.println("inner bottom right");
				Board.this.paintComponent(getGraphics(), 17);
				return 17;
			}
			else if(inPosition(x, piecePositionsX[1], y, piecePositionsY[5])) {
				//System.out.println("middle bottom left");
				Board.this.paintComponent(getGraphics(), 18);
				return 18;
			}
			else if(inPosition(x, piecePositionsX[3], y, piecePositionsY[5])) {
				//System.out.println("middle bottom middle");
				Board.this.paintComponent(getGraphics(), 19);
				return 19;
			}
			else if(inPosition(x, piecePositionsX[5], y, piecePositionsY[5])) {
				//System.out.println("middle bottom right");
				Board.this.paintComponent(getGraphics(), 20);
				return 20;
			}
			else if(inPosition(x, piecePositionsX[0], y, piecePositionsY[6])) {
				//System.out.println("outer bottom left");
				Board.this.paintComponent(getGraphics(), 21);
				return 21;
			}
			else if(inPosition(x, piecePositionsX[3], y, piecePositionsY[6])) {
				//System.out.println("outer bottom middle");
				Board.this.paintComponent(getGraphics(), 22);
				return 22;
			}
			else if(inPosition(x, piecePositionsX[6], y, piecePositionsY[6])) {
				//System.out.println("outer bottom right");
				Board.this.paintComponent(getGraphics(), 23);
				return 23;
			}
			else {
				Board.this.repaint();
				return -1;
			}
			
		}

		private boolean inPosition(int x, int[] positionsX, int y, int[] positionsY) {
			if((x>positionsX[0] && x<positionsX[1]) && (y>positionsY[0] && y<positionsY[1])) 
				return true;				
			else
				return false;
		}
		
    }


	public void repaintPieces() {
		numWhite = 0;
		numBlack = 0;
		img = null;
		this.repaint();
		for(int i=0; i<game.getState().getBoardPieces().length; i++) {
			if(game.getState().getBoardPieces()[i]=="white") {
				numWhite++;
				this.paintComponent(getGraphics(), i, "white");
			}
			else if(game.getState().getBoardPieces()[i]=="black") {
				numBlack++;
				this.paintComponent(getGraphics(), i, "black");
			}
			if(game.getState().getGameStage()==3) {
				if(i==game.getState().getSelectedPiece()) {
					this.paintComponent(getGraphics(), i, "green");					
				}
				else if(game.getState().getBoardPieces()[i]==null && game.getState().getMovablePositions().contains(i)) {
					this.paintComponent(getGraphics(), i, "smallgreen");		
				}
			}
		}
	}
	
	
}
