package model;

import lombok.Data;

@Data
public class OmokGameDTO {
	private String gameId;
	private String ownerId;
	private String guestId;
	private String title;
}
