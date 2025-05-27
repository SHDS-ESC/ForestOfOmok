package model;

import lombok.Data;

@Data
public class OmokGame {
	
	OmokGameDTO dto;
	OmokBoard board;
	
	public OmokGame(OmokGameDTO dto, OmokBoard board) {
		this.dto = dto;
		this.board = board;
	}
	
	
}
