package model;

import lombok.Data;

@Data
public class UserDTO {
	private String userId;
	private String gameId;
	private String imgId;
	private String name;
	private String email;
	private int win;
	private int lose;

}
