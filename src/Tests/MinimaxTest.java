package Tests;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Random;

import org.junit.Test;

import gui.Game;
import player.Minimax;
import player.MoveScore;

public class MinimaxTest {

	Random rand = new Random();
	Game game = new Game("name1", "name2", "pvAI", "Minimax", "");
	Minimax computer = new Minimax(game, game.getState());
			
	@Test
	public void testMinimaxConstructor() {
		assertNotNull(computer.game);
		assertNotNull(computer.state);
	}

	@Test
	public void testMinimax() {
		int depth = rand.nextInt(6);
		computer.setCopyState(game.getState().saveGameState());
		MoveScore bestMove = computer.minimax("black", depth, -1000000, 1000000);
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
		game.getBoard().repaintPieces();
		try {
		       Thread.sleep(2000);
		    } catch(InterruptedException e) {
		    }
		
		game.getState().setGameStage(2);
		game.getState().setTurn("black");
		computer.setCopyState(game.getState().saveGameState());
		bestMove = computer.minimax("black", depth, -1000000, 1000000);
		computer.makeMove(bestMove, "black");
		assertEquals(null, game.getState().getBoardPiece(bestMove.index));
		assertEquals("black", game.getState().getBoardPiece(bestMove.to));
		game.getBoard().repaintPieces();
		try {
		       Thread.sleep(2000);
		    } catch(InterruptedException e) {
		    }
		
		randomPiece = rand.nextInt(24);
		while(game.getState().getBoardPiece(randomPiece)!=null) {
			randomPiece = rand.nextInt(24);
		}
		game.getState().setBoardPiece(randomPiece, "white");
		game.getBoard().repaintPieces();
		try {
		       Thread.sleep(2000);
		    } catch(InterruptedException e) {
		    }
		game.getState().setGameStage(4);
		game.getState().setTurn("black");
		computer.setCopyState(game.getState().saveGameState());
		bestMove = computer.minimax("black", depth, -1000000, 1000000);
		computer.makeMove(bestMove, "black");
		assertEquals(null, game.getState().getBoardPiece(bestMove.index));
		game.getBoard().repaintPieces();
		try {
		       Thread.sleep(2000);
		    } catch(InterruptedException e) {
		    }
		
		
		
	}

}
