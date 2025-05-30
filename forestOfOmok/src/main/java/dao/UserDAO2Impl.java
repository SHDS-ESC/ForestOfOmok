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
import javax.sql.DataSource;
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
}