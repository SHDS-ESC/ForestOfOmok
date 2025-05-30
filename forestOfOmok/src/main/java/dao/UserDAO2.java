
package dao;
import java.util.List;

import model.Game;
import model.History;
import model.Profile;
import model.Rank;
public interface UserDAO2 {
	Profile getProfile(String id);
	List<Rank> getRankList();
	List<History> getHistoryList(String ownerId);
                               	// 방만들기 
	Game createRoom(Game game); // String → Game으로 수정
	
	// 방 리스트 조회
	List<Game> getGameList();
}
