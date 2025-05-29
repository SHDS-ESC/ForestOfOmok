package dao;
import java.util.List;

import model.Game;
import model.Profile;
public interface UserDAO2 {
	// 프로필 조회
	Profile getProfile(String id);
	
	// 방만들기 
	Game createRoom(Game game); // String → Game으로 수정
	
	// 방 리스트 조회
	List<Game> getGameList();
}
