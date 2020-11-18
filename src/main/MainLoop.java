package main;

import gui.Game;
import gui.MainMenu;

public class MainLoop {

	public static void main(String[] args) {
		MainMenu menu = new MainMenu();

		while(menu.isReady() == false){
		    try {
		       Thread.sleep(200);
		       System.out.println("waiting");
		    } catch(InterruptedException e) {
		    }
		}
		
		Game game = new Game(menu.getP1Name(), menu.getP2Name());
		if(Math.random() < 0.5) {
			game.switchTurn();
		}
		while(game.isFinished()==false) {
			while(game.getTurn()=="white") {
				try {
				       Thread.sleep(200);
				       System.out.println("white turn");
				    } catch(InterruptedException e) {
				    }
			}
			while(game.getTurn()=="black") {
				try {
				       Thread.sleep(200);
				       System.out.println("black turn");
				    } catch(InterruptedException e) {
				    }
			}
		}
		
	}
	

}
