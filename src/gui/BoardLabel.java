package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class BoardLabel extends JLabel {

	
	private static final int totalNumberOfPieces = 24;
	private Game game;
	private BufferedImage img;
	private TMMMouseAdapter tmmMouseAdapter;
	
	
	private int[][] piecePositionsX = {{10,30}, {55,85}, {110,140}, {215, 245}, {325,355}, {380, 410}, {435,460}}; 
	private int[][] piecePositionsY = {{10,30}, {55,85}, {110,140}, {215, 245}, {325,355}, {380, 410}, {435,460}};  
	private int[][] positionsOnBoard = {{10,10}, {223,10}, {445,10}, {60,60}, {223, 60}, {385,60}, {112,115}, {223, 115}, {330, 115},
			{15, 220}, {65, 220}, {115, 220}, {335, 220}, {390, 220}, {445, 220}, {112, 330}, {223, 330}, {330, 330}, {60, 385},
			{223, 385}, {385, 385}, {10, 440}, {223, 440}, {445, 440}};
	private String[] boardPieces;
	
	int numWhite = 0;
	int numBlack = 0;
	
	public BoardLabel(ImageIcon img, Game game) {
		super(img);
		this.game = game;
		tmmMouseAdapter = new TMMMouseAdapter();
		this.addMouseMotionListener(tmmMouseAdapter);
		this.addMouseListener(tmmMouseAdapter);
		boardPieces = new String[totalNumberOfPieces];
		for(int i=0; i<totalNumberOfPieces; i++) {
			boardPieces[i] = null;
		}
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
		if(boardPieces[point]==null) {
			g.setColor(Color.LIGHT_GRAY);  
			g.fillOval(positionsOnBoard[point][0]-6, positionsOnBoard[point][1]-6, 30, 30);			
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
		}
		else {
			gbuffer.setColor(Color.BLACK);  
		}
		gbuffer.fillOval(positionsOnBoard[point][0]-6, positionsOnBoard[point][1]-6, 30, 30);
		
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
        public void mouseClicked(MouseEvent e ) {
        	int piecePosition = checkMouseBoundaries(e.getX(), e.getY());
        	if(game.isPieceRemovalTurn()) {
        		if(game.getTurn()=="white" && boardPieces[piecePosition]=="black" && game.inMill(piecePosition)==false)  {
        			System.out.println("Remove black piece successful");
        			boardPieces[piecePosition]=null;
        			repaintPieces();
        			game.togglePieceRemovalTurn();
        			game.switchTurn();
        		}
        		if(game.getTurn()=="black" && boardPieces[piecePosition]=="white" && game.inMill(piecePosition)==false)  {
        			System.out.println("Remove white piece successful");
        			boardPieces[piecePosition]=null;
        			repaintPieces();
        			game.togglePieceRemovalTurn();
        			game.switchTurn();
        		}
        	}
        	else if(piecePosition!=-1 && boardPieces[piecePosition]==null) {
            	boardPieces[piecePosition] = game.getTurn();
            	repaintPieces();
                if(game.getTurn()=="white") {
                	game.removeWhitePieceFromPanel();
                }
                else if(game.getTurn()=="black") {
                	game.removeBlackPieceFromPanel();
                }
                if(game.checkForMill(boardPieces)) {
                	 game.togglePieceRemovalTurn();
                }
                else {
                	game.switchTurn();                	
                }
            }
            

        }

		private int checkMouseBoundaries(int x, int y) {
			//System.out.println("" + x + ", " + y);
			
			if(inPosition(x, piecePositionsX[0], y, piecePositionsY[0])) {
				//System.out.println("outer top left");
				BoardLabel.this.paintComponent(getGraphics(), 0);
				return 0;
			}
			else if(inPosition(x, piecePositionsX[3], y, piecePositionsY[0])) {
				//System.out.println("outer top middle");
				BoardLabel.this.paintComponent(getGraphics(), 1);
				return 1;
			}
			else if(inPosition(x, piecePositionsX[6], y, piecePositionsY[0])) {
				//System.out.println("outer top right");
				BoardLabel.this.paintComponent(getGraphics(), 2);
				return 2;
			}
			else if(inPosition(x, piecePositionsX[1], y, piecePositionsY[1])) {
				//System.out.println("middle top left");
				BoardLabel.this.paintComponent(getGraphics(), 3);
				return 3;
			}
			else if(inPosition(x, piecePositionsX[3], y, piecePositionsY[1])) {
				//System.out.println("middle top middle");
				BoardLabel.this.paintComponent(getGraphics(), 4);
				return 4; 
			}
			else if(inPosition(x, piecePositionsX[5], y, piecePositionsY[1])) {
				//System.out.println("middle top right");
				BoardLabel.this.paintComponent(getGraphics(), 5);
				return 5;
			}
			else if(inPosition(x, piecePositionsX[2], y, piecePositionsY[2])) {
				//System.out.println("inner top left");
				BoardLabel.this.paintComponent(getGraphics(), 6);
				return 6;
			}
			else if(inPosition(x, piecePositionsX[3], y, piecePositionsY[2])) {
				//System.out.println("inner top middle");
				BoardLabel.this.paintComponent(getGraphics(), 7);
				return 7;
			}
			else if(inPosition(x, piecePositionsX[4], y, piecePositionsY[2])) {
				//System.out.println("inner top right");
				BoardLabel.this.paintComponent(getGraphics(), 8);
				return 8;
			}
			else if(inPosition(x, piecePositionsX[0], y, piecePositionsY[3])) {
				//System.out.println("outer centre left");
				BoardLabel.this.paintComponent(getGraphics(), 9);
				return 9;
			}
			else if(inPosition(x, piecePositionsX[1], y, piecePositionsY[3])) {
				//System.out.println("middle centre left");
				BoardLabel.this.paintComponent(getGraphics(), 10);
				return 10;
			}
			else if(inPosition(x, piecePositionsX[2], y, piecePositionsY[3])) {
				//System.out.println("inner centre left");
				BoardLabel.this.paintComponent(getGraphics(), 11);
				return 11;
			}
			else if(inPosition(x, piecePositionsX[4], y, piecePositionsY[3])) {
				//System.out.println("inner centre right");
				BoardLabel.this.paintComponent(getGraphics(), 12);
				return 12;
			}
			else if(inPosition(x, piecePositionsX[5], y, piecePositionsY[3])) {
				//System.out.println("middle centre right");
				BoardLabel.this.paintComponent(getGraphics(), 13);
				return 13;
			}
			else if(inPosition(x, piecePositionsX[6], y, piecePositionsY[3])) {
				//System.out.println("outer centre right");
				BoardLabel.this.paintComponent(getGraphics(), 14);
				return 14;
			}
			else if(inPosition(x, piecePositionsX[2], y, piecePositionsY[4])) {
				//System.out.println("inner bottom left");
				BoardLabel.this.paintComponent(getGraphics(), 15);
				return 15;
			}
			else if(inPosition(x, piecePositionsX[3], y, piecePositionsY[4])) {
				//System.out.println("inner bottom middle");
				BoardLabel.this.paintComponent(getGraphics(), 16);
				return 16;
			}
			else if(inPosition(x, piecePositionsX[4], y, piecePositionsY[4])) {
				//System.out.println("inner bottom right");
				BoardLabel.this.paintComponent(getGraphics(), 17);
				return 17;
			}
			else if(inPosition(x, piecePositionsX[1], y, piecePositionsY[5])) {
				//System.out.println("middle bottom left");
				BoardLabel.this.paintComponent(getGraphics(), 18);
				return 18;
			}
			else if(inPosition(x, piecePositionsX[3], y, piecePositionsY[5])) {
				//System.out.println("middle bottom middle");
				BoardLabel.this.paintComponent(getGraphics(), 19);
				return 19;
			}
			else if(inPosition(x, piecePositionsX[5], y, piecePositionsY[5])) {
				//System.out.println("middle bottom right");
				BoardLabel.this.paintComponent(getGraphics(), 20);
				return 20;
			}
			else if(inPosition(x, piecePositionsX[0], y, piecePositionsY[6])) {
				//System.out.println("outer bottom left");
				BoardLabel.this.paintComponent(getGraphics(), 21);
				return 21;
			}
			else if(inPosition(x, piecePositionsX[3], y, piecePositionsY[6])) {
				//System.out.println("outer bottom middle");
				BoardLabel.this.paintComponent(getGraphics(), 22);
				return 22;
			}
			else if(inPosition(x, piecePositionsX[6], y, piecePositionsY[6])) {
				//System.out.println("outer bottom right");
				BoardLabel.this.paintComponent(getGraphics(), 23);
				return 23;
			}
			else {
				BoardLabel.this.repaint();
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
		for(int i=0; i<boardPieces.length; i++) {
			if(boardPieces[i]=="white") {
				numWhite++;
				this.paintComponent(getGraphics(), i, "white");
			}
			else if(boardPieces[i]=="black") {
				numBlack++;
				this.paintComponent(getGraphics(), i, "black");
			}
		}
	}
	
	
}
