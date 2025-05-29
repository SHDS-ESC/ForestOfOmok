package model;

import lombok.Data;

@Data
public class OmokGame {
	
	OmokGameDTO game;
	OmokBoard board;
	
	public OmokGame(OmokGameDTO game, OmokBoard board) {
		this.game = game;
		this.board = board;
	}
	
	
}
