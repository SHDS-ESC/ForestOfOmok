package service;

import model.OmokBoard;
import model.OmokGame;
import model.OmokGameDTO;

public class OmokGameService {
	
	
	public static String omokTurn(String gameId, int row, int col) {
		return "success";
		
		// 1. game 객체 찾고 (소켓에서)
//		OmokGame game = socket.getOmokGame(gameId);
//		OmokGame game = new OmokGame(null, null);
		
		// 2. validation check
//		if (isValidateCoord(game, row, col)) {
			// 가능
			
//		} else {
			// 불가능
			
//		}
		
		// 3. 
		
//		return "success";
	}
	
	private static boolean isValidateCoord(OmokGame game, int row, int col) {
		
		OmokGameDTO dto = game.getDto();
		OmokBoard board = game.getBoard();
		
		int boardRow = board.getRowCount();
		int boardCol = board.getColCount();
		
//		if (row<0 || row>=boardRow || col<0 || col>=boardCol || ((int)board.getCell(row, col)) != board.EMPTY)
//		{
//			return false;
//		}
		
		// 쌍삼, 사삼 검사 함수 만들어서 호출할 것.
		if (isBannedCoord()) {
			;
		}
		
		return true;
	}
	
	private static boolean isBannedCoord() {
		boolean result = true;
		
		return result;
	}
	
	
}
