package dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import model.Game;
import model.Profile;
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
	
	// 1. 프로필 조회
	@Override
	public Profile getProfile(String id) {
		System.out.println("profile 접근");
		Profile profile = new Profile();
		try {
			con = datafactory.getConnection();
			System.out.println(con);
			// TODO *** 세션으로 아이디 받아야함
			// user_id = 1 → 전달받은 id로 변경
	        String query = "SELECT u.NAME, u.WIN, u.LOSE, i.img_path " +
	                       "FROM users u JOIN image i ON u.img_id = i.img_id " +
	                       "WHERE u.user_id = ?";
	        pstmt = con.prepareStatement(query);
	        pstmt.setString(1, id);  // ★ 세션에서 전달된 아이디 세팅

	        ResultSet rs = pstmt.executeQuery();
	        if (rs.next()) {
	            profile.setName(rs.getString("name"));
	            profile.setWinCnt(rs.getString("win"));
	            profile.setLoseCnt(rs.getString("lose"));
	            profile.setImgPath(rs.getString("img_path"));
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
	
	// 2. 방 만들기
	@Override
	public Game createRoom(Game game) {
	    try {
	        con = datafactory.getConnection();

	        // 시퀀스로 gameId 생성
	        String seqSql = "SELECT game_seq.NEXTVAL FROM dual";
	        pstmt = con.prepareStatement(seqSql);
	        ResultSet rs = pstmt.executeQuery();
	        int generatedGameId = 0;
	        if (rs.next()) {
	            generatedGameId = rs.getInt(1);
	        }
	        rs.close();
	        pstmt.close();

	        // insert
	        String insertSql = "INSERT INTO Game (game_id, owner_id, guest_id, title) VALUES (?, ?, ?, ?)";
	        pstmt = con.prepareStatement(insertSql);
	        pstmt.setInt(1, generatedGameId);
	        pstmt.setInt(2, game.getOwnerId());
	        pstmt.setInt(3, 1); // guestId 하드코딩
	        pstmt.setString(4, game.getTitle());
	        pstmt.executeUpdate();

	        // 객체에 반영
	        game.setGameId(generatedGameId);
	        game.setGuestId(1);

	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        try { if (pstmt != null) pstmt.close(); } catch (Exception e) {}
	        try { if (con != null) con.close(); } catch (Exception e) {}
	    }
	    return game;
	}
	
	// 3. 방 리스트 조회
	@Override
	public List<Game> getGameList() {
		List<Game> gameList = new ArrayList<>();
		
		try {
	        con = datafactory.getConnection();
	        String query = "SELECT * FROM Game";
	        pstmt = con.prepareStatement(query);
	        ResultSet rs = pstmt.executeQuery();
	        
	        while(rs.next()) {
	        	Game game = new Game();
	        	game.setGameId(rs.getInt("game_id"));
	        	game.setOwnerId(rs.getInt("owner_id"));
	        	game.setGuestId(rs.getInt("guest_id"));
	        	game.setTitle(rs.getString("title"));
	        	gameList.add(game);
	        }
			rs.close();
			pstmt.close();
			con.close();
		} catch (Exception e) {
			// TODO: handle exception

		    System.out.println("db연결 오류");
		    e.printStackTrace(); // ← 이걸 반드시 추가하세요!
		}
		
		
		
		return gameList;
	}

}