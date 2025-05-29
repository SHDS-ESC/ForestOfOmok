package dao;
import java.util.List;

import model.History;
import model.Profile;
import model.Rank;
public interface UserDAO2 {
	Profile getProfile(String id);
	List<Rank> getRankList();
	List<History> getHistoryList(String ownerId);
}
