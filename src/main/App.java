package main;

import gui.Game;
import gui.MainMenu;
import player.Minimax;
import player.MonteCarloTreeSearch;
import player.Move;
import player.MoveScore;

public class App {

	public static void main(String[] args) {
		//create Main Menu
		MainMenu menu = new MainMenu();

		//wait for user to select a game type
		while(menu.isReady() == false){
		    try {
		       Thread.sleep(200);
		       System.out.println("waiting");
		    } catch(InterruptedException e) {
		    }
		}
		
		//create a new game instance and set difficulty if applicable
		Game game = new Game(menu.getP1Name(), menu.getP2Name(), menu.getGameType(), menu.getComputerType());
		int depth = setDifficulty(menu.getDifficulty());
		int movesWithoutMill = 0;
		
		//randomly start with black for white 
		if(Math.random() < 0.5) {
			game.switchTurn();
		}
		
		//player vs. player logic
		if(menu.getGameType()=="pvp") {
			while(game.getState().getGameStage()!=5) {
				while(game.getState().getTurn()=="white") {
					try {
					       Thread.sleep(1000);
					    } catch(InterruptedException e) {
					    }
				}
				checkForDrawRule(game);
				while(game.getState().getTurn()=="black") {
					try {
					       Thread.sleep(1000);
					    } catch(InterruptedException e) {
					    }
				}
				checkForDrawRule(game);
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
				checkForDrawRule(game);
				if(game.getState().getTurn()=="black") {
					game.setStopPaining(true);
					game.getComputer().setCopyState(game.getState().saveGameState());
					game.showThinking();
					//MoveScore bestMove = game.getComputer().minimax("black", depth, -1000000, 1000000);
					MoveScore bestMove = new MoveScore(0, 0, 0);
					if(game.getComputer() instanceof MonteCarloTreeSearch) {
						bestMove = ((MonteCarloTreeSearch)game.getComputer()).monteCarloTreeSearch("black", 10000);
					}
					else if (game.getComputer() instanceof Minimax){
						bestMove = ((Minimax)game.getComputer()).minimax("black", depth, -1000000, 1000000);
					}
					game.hideThinking();
					
					game.getComputer().makeMove(bestMove, "black");
					
					if(!game.getComputer().getMillCreated()) {
						game.switchTurn();
						game.getComputer().resetMillCreated();
					}
					game.setStopPaining(false);
					
					showComputerMove(bestMove, game);
					checkForDrawRule(game);
				}
			}
			System.out.print("Game ended");
		}
		else if(menu.getGameType()=="AIvAI") {
			while(game.getState().getGameStage()!=5) {
				if(game.getState().getTurn()=="white") {
					game.setStopPaining(true);
					game.getComputer().setCopyState(game.getState().saveGameState());
					game.showThinking();
					MoveScore bestMove = new MoveScore(0, 0, 0);
					if(game.getComputer() instanceof MonteCarloTreeSearch) {
						bestMove = ((MonteCarloTreeSearch)game.getComputer()).monteCarloTreeSearch("white", 10000);
					}
					else if (game.getComputer() instanceof Minimax){
						bestMove = ((Minimax)game.getComputer()).minimax("white", depth, -1000000, 1000000);
					}
					game.hideThinking();
					
					game.getComputer().makeMove(bestMove, "white");
					
					if(!game.getComputer().getMillCreated()) {
						game.switchTurn();
						game.getComputer().resetMillCreated();
					}
					game.setStopPaining(false);
					
					showComputerMove(bestMove, game);
					checkForDrawRule(game);
				}
				else if(game.getState().getTurn()=="black") {
					game.setStopPaining(true);
					game.getOtherComputer().setCopyState(game.getState().saveGameState());
					game.showThinking();
					//MoveScore bestMove = game.getComputer().minimax("black", depth, -1000000, 1000000);
					MoveScore bestMove = new MoveScore(0, 0, 0);
					if(game.getOtherComputer() instanceof MonteCarloTreeSearch) {
						bestMove = ((MonteCarloTreeSearch)game.getOtherComputer()).monteCarloTreeSearch("black", 10000);
					}
					else if (game.getOtherComputer() instanceof Minimax){
						bestMove = ((Minimax)game.getOtherComputer()).minimax("black", depth, -1000000, 1000000);
					}
					game.hideThinking();
					
					game.getOtherComputer().makeMove(bestMove, "black");
					
					if(!game.getOtherComputer().getMillCreated()) {
						game.switchTurn();
						game.getOtherComputer().resetMillCreated();
					}
					game.setStopPaining(false);
					
					showComputerMove(bestMove, game);
					checkForDrawRule(game);
				}
				
				
			}
			System.out.print("Game ended");
		}
		
	}

	private static void showComputerMove(MoveScore bestMove, Game game) {
		game.getBoard().repaintPieces();
		if(bestMove.to==-1 && game.getState().getGameStage()!=4) {
			game.getBoard().paintComponent(game.getBoard().getGraphics(), bestMove.index, "red");	
		}
		else if(game.getState().getGameStage()!=4){
			game.getBoard().paintComponent(game.getBoard().getGraphics(), bestMove.index, "smallred");	
			game.getBoard().paintComponent(game.getBoard().getGraphics(), bestMove.to, "red");	
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
	
	private static void checkForDrawRule(Game game) {
		System.out.println("Moves without mill: " + game.getState().getMovesWithoutMill());
		if(game.getState().getMovesWithoutMill()>=50) {
			game.getState().setTurn(null);
			game.getState().setGameStage(5);
			game.displayMessage(6);
		}
	}
	

}
