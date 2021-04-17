package Tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.*;

import gui.Game;
import player.Computer;
import player.Minimax;
import player.Move;
import player.MoveScore;

public class ComputerTest {
	
	Random rand = new Random();

	Game game = new Game("name1", "name2", "pvAI", "Minimax", "");
	Computer computer = new Minimax(game, game.getState());
	
	@Test
	public void testConstructor() {
		assertNotNull(computer.game);
		assertNotNull(computer.state);
	}

	@Test
	public void testMakeMove() {
		computer.setCopyState(computer.state.saveGameState());
		ArrayList<Move> validMoves = computer.findValidMoves("black");
		Move randomMove = validMoves.get(rand.nextInt(validMoves.size()));
		MoveScore moveScore = randomMove.toMoveScore();
		computer.makeMove(moveScore, "black");
		assertEquals("black", computer.state.getBoardPiece(moveScore.index));
		
		computer.state.setGameStage(2);
		computer.state.setTurn("black");
		computer.copyState = computer.state.saveGameState();
		validMoves = computer.findValidMoves("black");
		randomMove = validMoves.get(rand.nextInt(validMoves.size()));
		moveScore = randomMove.toMoveScore();
		computer.makeMove(moveScore, "black");
		assertEquals(null, computer.state.getBoardPiece(moveScore.index));
		assertEquals("black", computer.state.getBoardPiece(moveScore.to));
		
		computer.state.setGameStage(1);
		computer.state.setTurn("white");
		computer.copyState = computer.state.saveGameState();
		validMoves = computer.findValidMoves("white");
		Move randomMoveWhite = validMoves.get(rand.nextInt(validMoves.size()));
		MoveScore whiteMoveScore = randomMoveWhite.toMoveScore();
		computer.makeMove(whiteMoveScore, "white");
		computer.state.setGameStage(4);
		computer.state.setTurn("black");
		computer.copyState = computer.state.saveGameState();
		validMoves = computer.findValidMoves("black");
		randomMove = validMoves.get(rand.nextInt(validMoves.size()));
		moveScore = randomMove.toMoveScore();
		computer.makeMove(moveScore, "black");
		assertEquals(null, computer.state.getBoardPiece(whiteMoveScore.index));
		
	}

	@Test
	public void testFindValidMoves() {
		computer.setCopyState(computer.state.saveGameState());
		computer.copyState.setTurn("black");
		ArrayList<Move> validMoves = computer.findValidMoves("black");
		
		//test if there are 24 possible moves for first move
		assertEquals(24, validMoves.size());
		
		int randomPiece = rand.nextInt(24);
		computer.copyState.setBoardPiece(randomPiece, "black");
		computer.copyState.setTurn("white");
	    validMoves = computer.findValidMoves("white");
	    
	    //after placing piece, check if there are 23 moves
	    assertEquals(23, validMoves.size());
	    
	    computer.copyState.setGameStage(2);
	    computer.copyState.setTurn("black");
		validMoves = computer.findValidMoves("black");
		ArrayList<Integer> fourAdjacent = new ArrayList<Integer>(List.of(3,4,5,10,13,18,19,20));
		
		//checks if movement move contains 3 or 4 valid moves depending on position
		if(fourAdjacent.contains(randomPiece)) {
			assertEquals(4, validMoves.size());
		}
		else {
			assertEquals(3, validMoves.size());
		}
		
		computer.copyState.setGameStage(1);
		computer.copyState.setTurn("white");
		randomPiece = rand.nextInt(24);
		while(computer.copyState.getBoardPiece(randomPiece)!=null) {
			randomPiece = rand.nextInt(24);
		}
		computer.copyState.setBoardPiece(randomPiece, "white");
		computer.copyState.setGameStage(4);
		computer.copyState.setTurn("black");
		validMoves = computer.findValidMoves("black");
		//check if there is only one piece to remove if black made a mill
		assertEquals(1, validMoves.size());
		assertEquals(randomPiece, validMoves.get(0).getPiecePosition());
		
		
	}

	//uses findValidMoves after tests
	@Test
	public void testSimulateMove() {
		computer.setCopyState(computer.state.saveGameState());
		ArrayList<Move> validMoves = computer.findValidMoves("black");
		
		Move placementMove = validMoves.get(rand.nextInt(validMoves.size()));
		computer.simulateMove(placementMove, "black");
		assertEquals("black", computer.copyState.getBoardPiece(placementMove.getPiecePosition()));
		
		computer.copyState.setGameStage(2);
		validMoves = computer.findValidMoves("black");
		Move movementMove = validMoves.get(rand.nextInt(validMoves.size()));
		computer.simulateMove(movementMove, "black");
		assertEquals(null, computer.copyState.getBoardPiece(movementMove.getPiecePosition()));
		assertEquals("black", computer.copyState.getBoardPiece(movementMove.getTo()));
		
		computer.copyState.setGameStage(1);
		validMoves = computer.findValidMoves("white");
		Move whiteMove = validMoves.get(rand.nextInt(validMoves.size()));
		computer.simulateMove(whiteMove, "white");
		assertEquals("white", computer.copyState.getBoardPiece(whiteMove.getPiecePosition()));
		computer.copyState.setGameStage(4);
		validMoves = computer.findValidMoves("black");
		Move removalWhiteMove = validMoves.get(rand.nextInt(validMoves.size()));
		computer.simulateMove(removalWhiteMove, "black");
		assertEquals(null, computer.copyState.getBoardPiece(removalWhiteMove.getPiecePosition()));
		
	}
}
