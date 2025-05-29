package dao;
import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import model.History;
import model.Profile;
import model.Rank;
import java.util.ArrayList;


public class UserDAO2Impl implements UserDAO2 {
	private PreparedStatement pstmt;
	private Connection con;
	private DataSource datafactory;
	// 생성자
	public UserDAO2Impl() {
		try {
			Context ctx = new InitialContext();
			Context envContext = (Context)ctx.lookup("java:/comp/env");
			datafactory = (DataSource)envContext.lookup("jdbc/oracle");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	@Override
	public Profile getProfile(String id) {
		System.out.println("profile 접근");
		Profile profile = new Profile();
		try {
			con = datafactory.getConnection();
			System.out.println(con);
			// TODO *** 세션으로 아이디 받아야함
			String query = "SELECT u.NAME, u.WIN, LOSE, i.img_path FROM users u, image i WHERE user_id = 1 and u.img_id = i.img_id";
			pstmt = con.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();
			
			System.out.println("rs"+rs);
			if (rs.next()) {
				profile.setName(rs.getString("name"));
				profile.setWinCnt(rs.getString("win"));
				profile.setLoseCnt(rs.getString("lose"));
				profile.setImgPath(rs.getString("img_path"));
				// 랭크 추가로 가져와야함
			}
			rs.close();
			pstmt.close();
			con.close();
		} catch (Exception e) {
		    System.out.println("db연결 오류");
		    e.printStackTrace(); // ← 이걸 반드시 추가하세요!
		}
		return profile;
	}
	
	@Override
	public List<Rank> getRankList() {
		System.out.println("profile 접근");
		List<Rank> rankList = new ArrayList<>();
		try {
			con = datafactory.getConnection();
			System.out.println(con);
			String query = "SELECT ROWNUM RANK, cnt, name, profile "
					+ "FROM ("
					+ "SELECT i.IMG_PATH profile , u.NAME name ,u.WIN-u.LOSE cnt "
					+ "FROM USERS u, IMAGE i "
					+ "WHERE u.IMG_ID = i.IMG_ID "
					+ "ORDER BY CNT DESC) RK "
					+ "WHERE ROWNUM <= 10";
			pstmt = con.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next()) {
				Rank rank = new Rank();
				rank.setPoint(rs.getString("cnt"));
				rank.setName(rs.getString("name"));
				rank.setRank(rs.getString("rank"));
				rank.setProfile(rs.getString("profile"));
				rankList.add(rank);
				// 랭크 추가로 가져와야함
			}
			rs.close();
			pstmt.close();
			con.close();
		} catch (Exception e) {
		    System.out.println("db연결 오류");
		    e.printStackTrace(); // ← 이걸 반드시 추가하세요!
		}
		return rankList;
	}
	@Override
	public List<History> getHistoryList(String ownerId) {
		System.out.println("profile 접근");
		List<History> historyList = new ArrayList<>();
		try {
			con = datafactory.getConnection();
			System.out.println(con);
			String query = "SELECT i.IMG_PATH, h.WINNER_COLOR, h.TIME, h.TURN_COUNT, h.DATA, " +
		               "winner.NAME AS WINNER_NAME, loser.NAME AS LOSER_NAME " +
		               "FROM HISTORY h " +
		               "JOIN USERS winner ON winner.USER_ID = h.WINNER_ID " +
		               "JOIN USERS loser ON loser.USER_ID = h.LOSER_ID " +
		               "JOIN IMAGE i ON winner.IMG_ID = i.IMG_ID " +
		               "WHERE h.WINNER_ID = ? OR h.LOSER_ID = ?";
			
			pstmt = con.prepareStatement(query);
			pstmt.setInt(1, Integer.parseInt(ownerId)); // 이후에 아이디 넣어주어야 함
			pstmt.setInt(2, Integer.parseInt(ownerId)); // 이후에 아이디 넣어주어야 함
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next()) {
				History history = new History();
				history.setImgPath(rs.getString("img_path"));
				history.setWinnerColor(rs.getString("winner_color"));
				history.setTime(rs.getString("time"));
				history.setTurnCount(rs.getString("turn_count"));
				history.setData(rs.getString("data"));
				history.setWinnerName(rs.getString("winner_name"));
				history.setLoserName(rs.getString("loser_name"));
				// 랭크 추가로 가져와야함
				historyList.add(history);
			}
			rs.close();
			pstmt.close();
			con.close();
		} catch (Exception e) {
		    System.out.println("db연결 오류");
		    e.printStackTrace(); // ← 이걸 반드시 추가하세요!
		}
		return historyList;
	}
}