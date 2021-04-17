package Tests;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Random;

import org.junit.Test;

import gui.Game;
import player.MonteCarloTreeSearch;
import player.MoveScore;

public class MonteCarloTreeSearchTest {

	Random rand = new Random();
	Game game = new Game("name1", "name2", "pvAI", "MCTS", "");
	MonteCarloTreeSearch computer = new MonteCarloTreeSearch(game, game.getState());
	
	@Test
	public void testConstructor() {
		assertNotNull(computer.game);
		assertNotNull(computer.state);
	}

	@Test
	public void testMonteCarloTreeSearch() {
		
		int iterations = rand.nextInt(10000 - 5000) + 5000;
		computer.setCopyState(game.getState().saveGameState());
		MoveScore bestMove = computer.monteCarloTreeSearch("black", iterations);
		computer.makeMove(bestMove, "black");
		assertTrue(Arrays.asList(game.getState().getBoardPieces()).contains("black"));
		game.getBoard().repaintPieces();
		try {
		       Thread.sleep(2000);
		    } catch(InterruptedException e) {
		    }
		
		int randomPiece = rand.nextInt(24);
		while(game.getState().getBoardPiece(randomPiece)!=null) {
			randomPiece = rand.nextInt(24);
		}
		game.getState().setBoardPiece(randomPiece, "white");
		randomPiece = rand.nextInt(24);
		while(game.getState().getBoardPiece(randomPiece)!=null) {
			randomPiece = rand.nextInt(24);
		}
		game.getState().setBoardPiece(randomPiece, "white");
		randomPiece = rand.nextInt(24);
		while(game.getState().getBoardPiece(randomPiece)!=null) {
			randomPiece = rand.nextInt(24);
		}
		game.getState().setBoardPiece(randomPiece, "white");
		while(game.getState().getBoardPiece(randomPiece)!=null) {
			randomPiece = rand.nextInt(24);
		}
		game.getState().setBoardPiece(randomPiece, "black");
		while(game.getState().getBoardPiece(randomPiece)!=null) {
			randomPiece = rand.nextInt(24);
		}
		game.getState().setBoardPiece(randomPiece, "black");
		game.getBoard().repaintPieces();
		try {
		       Thread.sleep(2000);
		    } catch(InterruptedException e) {
		    }
		
		game.getState().setGameStage(2);
		game.getState().setTurn("black");
		computer.setCopyState(game.getState().saveGameState());
		bestMove = computer.monteCarloTreeSearch("black", iterations);
		computer.makeMove(bestMove, "black");
		assertEquals(null, game.getState().getBoardPiece(bestMove.index));
		assertEquals("black", game.getState().getBoardPiece(bestMove.to));
		game.getBoard().repaintPieces();
		
		try {
		       Thread.sleep(2000);
		    } catch(InterruptedException e) {
		    }
		game.getState().setGameStage(4);
		game.getState().setTurn("black");
		computer.setCopyState(game.getState().saveGameState());
		bestMove = computer.monteCarloTreeSearch("black", iterations);
		computer.makeMove(bestMove, "black");
		assertEquals(null, game.getState().getBoardPiece(bestMove.index));
		game.getBoard().repaintPieces();
		try {
		       Thread.sleep(2000);
		    } catch(InterruptedException e) {
		    }
	}

}
