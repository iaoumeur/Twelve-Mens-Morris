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
		int depth = setDifficulty(menu.getDifficulty());
		
		if(Math.random() < 0.5) {
			game.switchTurn();
		}
		
		if(menu.getGameType()=="pvp") {
			while(game.getState().getGameStage()!=5) {
				while(game.getState().getTurn()=="white") {
					try {
					       Thread.sleep(1000);
					    } catch(InterruptedException e) {
					    }
				}
				while(game.getState().getTurn()=="black") {
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
				game.setStopPaining(true);
				game.getComputer().setCopyState(game.getState().saveGameState());
				MoveScore bestMove = game.getComputer().minimax("black", depth, -1000000, 1000000);
				System.out.println("Nodes evaluated: " + game.getComputer().getNodesEvaluated());
				
				game.setStopPaining(false);
				//System.out.println("****** BEST MOVE FOUND IS: " + bestMove.index + " WITH A SCORE OF " + bestMove.score + " ******");
				if(!game.getComputer().makeMove(bestMove, "black")) {
					game.switchTurn();					
				}
				game.getBoard().repaintPieces();
				if(bestMove.to==-1 && game.getState().getGameStage()!=4) {
					game.getBoard().paintComponent(game.getBoard().getGraphics(), bestMove.index, "red");	
				}
				else if(game.getState().getGameStage()!=4){
					game.getBoard().paintComponent(game.getBoard().getGraphics(), bestMove.index, "smallred");	
					game.getBoard().paintComponent(game.getBoard().getGraphics(), bestMove.to, "red");	
				}
				
			}
			System.out.print("Game ended");
		}
		else if(menu.getGameType()=="AIvAI") {
			while(game.getState().getGameStage()!=5) {
				if(game.getState().getTurn()=="white") {
					game.getState().hasGameEnded();
					game.getComputer().setCopyState(game.getState().saveGameState());
					MoveScore bestMove = game.getComputer().minimax("white", 4, -1000000, 1000000);
					//System.out.println("****** BEST MOVE FOUND IS: " + bestMove.index + " WITH A SCORE OF " + bestMove.score + " ******");
					if(!game.getComputer().makeMove(bestMove, "white")) {
						game.switchTurn();					
					}
					game.getBoard().repaintPieces();
					if(bestMove.to==-1 && game.getState().getGameStage()!=4) {
						game.getBoard().paintComponent(game.getBoard().getGraphics(), bestMove.index, "red");	
					}
					else if(game.getState().getGameStage()!=4){
						game.getBoard().paintComponent(game.getBoard().getGraphics(), bestMove.index, "smallred");	
						game.getBoard().paintComponent(game.getBoard().getGraphics(), bestMove.to, "red");	
					}
				}
				else if(game.getState().getTurn()=="black") {
					game.getState().hasGameEnded();
					game.getOtherComputer().setCopyState(game.getState().saveGameState());
					MoveScore bestMove = game.getOtherComputer().minimax("black", 4, -1000000, 1000000);
					//System.out.println("****** BEST MOVE FOUND IS: " + bestMove.index + " WITH A SCORE OF " + bestMove.score + " ******");
					if(!game.getOtherComputer().makeMove(bestMove, "black")) {
						game.switchTurn();					
					}
					game.getBoard().repaintPieces();
					if(bestMove.to==-1 && game.getState().getGameStage()!=4) {
						game.getBoard().paintComponent(game.getBoard().getGraphics(), bestMove.index, "red");	
					}
					else if(game.getState().getGameStage()!=4){
						game.getBoard().paintComponent(game.getBoard().getGraphics(), bestMove.index, "smallred");	
						game.getBoard().paintComponent(game.getBoard().getGraphics(), bestMove.to, "red");	
					}
				}
				
				
			}
			System.out.print("Game ended");
		}
		
	}

	private static int setDifficulty(String difficulty) {

		if(difficulty=="easy") 
			return 4;
		else if(difficulty=="hard")
			return 8;
		else 
			return 6;
		
	}
	

}
