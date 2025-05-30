package model;

import lombok.Data;

@Data
public class Game {
	private int gameId;
	private int ownerId;
	private int guestId;
	private String title;
}
