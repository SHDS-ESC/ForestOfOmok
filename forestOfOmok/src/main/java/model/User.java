package model;

import lombok.Data;

@Data
public class User {
	private String userId;
	private String imgId;
	private String pwd;
	private String name;
	private String email;
	private int win;
	private int lose;

}
