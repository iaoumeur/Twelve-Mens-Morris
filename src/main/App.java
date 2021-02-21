package main;

import gui.Game;
import gui.MainMenu;
import player.MoveScore;

public class App {

	public static void main(String[] args) {
		MainMenu menu = new MainMenu();

		while(menu.isReady() == false){
		    try {
		       Thread.sleep(200);
		       System.out.println("waiting");
		    } catch(InterruptedException e) {
		    }
		}
		
		Game game = new Game(menu.getP1Name(), menu.getP2Name(), menu.getGameType());
		
		if(Math.random() < 0.5) {
			game.switchTurn();
		}
		
		if(menu.getGameType()=="pvp") {
			while(game.getState().getGameStage()!=5) {
				while(game.getState().getTurn()=="white") {
					game.getState().evaluateState("white");
					try {
					       Thread.sleep(1000);
					    } catch(InterruptedException e) {
					    }
				}
				while(game.getState().getTurn()=="black") {
					game.getState().evaluateState("black");
					//System.out.println("Black Evaluation: " + game.getState().evaluateState("black"));
					//System.out.println("Game Stage: " + game.getState().getGameStage());
					try {
					       Thread.sleep(1000);
					    } catch(InterruptedException e) {
					    }
				}
			}
			System.out.print("Game ended");
		}
		else if(menu.getGameType()=="pvAI") {
			while(game.getState().getGameStage()!=5) {
				while(game.getState().getTurn()=="white") {
					try {
					       Thread.sleep(1000);
					    } catch(InterruptedException e) {
					    }
				}
				game.getComputer().setCopyState(game.getState().saveGameState());
				MoveScore bestMove = game.getComputer().minimax("black", 2);
				System.out.println("****** BEST MOVE FOUND IS: " + bestMove.index + " WITH A SCORE OF " + bestMove.score + " ******");
				if(!game.getComputer().makeMove(bestMove, "black")) {
					game.switchTurn();					
				}
				game.getBoard().repaintPieces();
			}
			System.out.print("Game ended");
		}
		
		
	}
	

}
