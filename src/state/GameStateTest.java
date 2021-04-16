package state;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;

public class GameStateTest {

	Random rand = new Random();
	GameState state = new GameState();
	
	
	@Test
	public void testBoardMouseClick() {
		
	}
	
	
	@Test
	public void testCheckForMill() {
		state = new GameState();
		state.setTurn("black");
		for(int i=0; i<state.getMillLocations().length; i++) {
			for(int j=0; j<state.getMillLocations()[i].length; j++) {
				state.setBoardPiece(state.getMillLocations()[i][j], "black");
			}
			assertTrue(state.checkForMill(false));
			for(int j=0; j<state.getMillLocations()[i].length; j++) {
				state.setBoardPiece(state.getMillLocations()[i][j], null);
			}
		}
		
	}


	@Test
	public void testCanPieceBeRemoved() {
		//first checks if white can remove a piece when black only has mills (should be false)
		state = new GameState();
		state.setTurn("black");
		int randomMill = rand.nextInt(state.getMillLocations().length);
		for(int i=0; i<state.getMillLocations()[randomMill].length; i++) {
			state.setBoardPiece(state.getMillLocations()[randomMill][i], "black");
		}
		state.checkForMill(true);
		state.setTurn("white");
		assertFalse(state.canPieceBeRemoved());
		//then checks if white can remove a piece when black has pieces outside of a mill (should be true)
		for(int i=0; i<state.getBoardPieces().length; i++) {
			if(state.getBoardPiece(i)!=null) {
				state.setBoardPiece(i, null);
				break;
			}
		}
		state.checkForMill(true);
		state.setTurn("white");
		assertTrue(state.canPieceBeRemoved());
		
		
	}

	@Test
	public void testCheckForWin() {
		
		assertNull(state.checkForWin());
		state.setBoardPiece(0, "white");
		state.setBoardPiece(1, "white");
		state.setBoardPiece(2, "black");
		state.setBoardPiece(3, "black");
		state.setBoardPiece(4, "black");
		state.setGameStage(2);
		state.setPiecesPlaced(24);
		assertEquals("black", state.checkForWin());
		state.setTurn("white");
		for(int i=5; i<state.getBoardPieces().length-1; i++) {
			state.setBoardPiece(i, state.getTurn());
			state.switchTurn();
		}
		state.setTurn("black");
		assertEquals("black", state.checkForWin());
		
	}

	@Test
	public void testPieceBlocked() {
		state = new GameState();
		int randomPiece = rand.nextInt(24);
		state.setBoardPiece(randomPiece, "black");
		assertFalse(state.pieceBlocked(randomPiece));
		for(int i=0; i<state.getAdjacentPositions()[randomPiece].length; i++) {
			state.setBoardPiece(state.getAdjacentPositions()[randomPiece][i], "white");
		}
		assertTrue(state.pieceBlocked(randomPiece));
	}


	@Test
	public void testCountPieces() {
		state = new GameState();
		int randomPiecesWhite = rand.nextInt(12);
		int randomPiecesBlack = rand.nextInt(12);
		
		int i=0;
		for(int j=0; j<randomPiecesWhite; j++) {
			state.setBoardPiece(i, "white");
			i++;
		}
		for(int j=0; j<randomPiecesBlack; j++) {
			state.setBoardPiece(i, "black");
			i++;
		}

		assertEquals(randomPiecesWhite, state.countPieces("white"));
		assertEquals(randomPiecesBlack, state.countPieces("black"));
		
	}

	@Test
	public void testCountTwoPieceConfigurations() {
		
		state = new GameState();
		assertEquals(0, state.countTwoPieceConfigurations("black"));
		int randomMill = rand.nextInt(state.getMillLocations().length);
		int randomPiece = rand.nextInt(2);
		int firstPiece = state.getMillLocations()[randomMill][randomPiece];
		int secondPiece = state.getMillLocations()[randomMill][rand.nextInt(2)];
		while(secondPiece==firstPiece) {
			secondPiece = state.getMillLocations()[randomMill][rand.nextInt(2)];
		}
		state.setBoardPiece(firstPiece, "black");
		state.setBoardPiece(secondPiece, "black");
		assertEquals(1, state.countTwoPieceConfigurations("black"));
		
		
	}

	@Test
	public void testCountThreePieceConfigurations() {
		state = new GameState();
		assertEquals(0, state.countThreePieceConfigurations("black"));
		int randomMill = rand.nextInt(state.getMillLocations().length);
		int randomPiece = rand.nextInt(2);
		int firstPiece = state.getMillLocations()[randomMill][randomPiece];
		int secondPiece = state.getMillLocations()[randomMill][rand.nextInt(2)];
		while(secondPiece==firstPiece) {
			secondPiece = state.getMillLocations()[randomMill][rand.nextInt(2)];
		}
		state.setBoardPiece(firstPiece, "black");
		state.setBoardPiece(secondPiece, "black");
		boolean pieceFound = false;
		for(int i=0; i<state.getMillLocations().length; i++) {
			if(i==randomMill) {
				continue;
			}
			for(int j=0; j<state.getMillLocations()[i].length; j++) {
				if(state.getMillLocations()[i][j]==firstPiece || state.getMillLocations()[i][j]==secondPiece) {
					int newMillPiece = state.getMillLocations()[i][rand.nextInt(2)];
					while(newMillPiece==state.getMillLocations()[i][j]) {
						newMillPiece = state.getMillLocations()[i][rand.nextInt(2)];
					}
					state.setBoardPiece(newMillPiece, "black");
					pieceFound = true;
					break;
				}
			}
			if(pieceFound) {
				break;
			}
		}
		assertEquals(1, state.countThreePieceConfigurations("black"));
		
	}

	@Test
	public void testCountDoubleMills() {
		fail("Not yet implemented");
	}

	@Test
	public void testSaveGameState() {
		state = new GameState();
		state.setBoardPiece(rand.nextInt(24), "black");
		state.setBlackPiecesPlaced(1);
		state.setWhitePiecesPlaced(0);
		state.setGameStage(2);
		state.setTurn("white");
		GameState testState = state.saveGameState();
		assertEquals(state.getBoardPieces(), testState.getBoardPieces());
		assertEquals(state.getWhitePiecesPlaced(), testState.getWhitePiecesPlaced());
		assertEquals(state.getBlackPiecesPlaced(), testState.getBlackPiecesPlaced());
		assertEquals(state.getGameStage(), testState.getGameStage());
		assertEquals(state.getMillsFound(), testState.getMillsFound());
		assertEquals(state.getTurn(), testState.getTurn());
		assertEquals(state.getSelectedPiece(), testState.getSelectedPiece());
		assertEquals(state.getFlyingBlack(), testState.getFlyingBlack());
		assertEquals(state.getFlyingWhite(), testState.getFlyingWhite());
		
	}

}
