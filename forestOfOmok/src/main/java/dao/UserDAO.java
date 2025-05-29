package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import model.User;

public class UserDAO {
	
	private PreparedStatement pstmt = null;
	private Statement stmt;
	private Connection con;
	private DataSource dataFactory;
	
	public UserDAO(){
		try {
			Context ctx = new InitialContext();
			Context envContext = (Context) ctx.lookup("java:/comp/env");
			dataFactory = (DataSource) envContext.lookup("jdbc/oracle");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//1.회원가입
	public int insertUser(User user) {
		
		try {
			con = dataFactory.getConnection();
			//win,lose 0으로 처리
			String query = "INSERT INTO users (user_id, img_id, pwd, name, email, win, lose) VALUES (USER_SEQ.NEXTVAL, ?, ?, ?, ?, 0, 0)";
			pstmt = con.prepareStatement(query);
			
			
			pstmt.setString(1, user.getImgId());
			pstmt.setString(2, user.getPwd());
			pstmt.setString(3, user.getName());
			pstmt.setString(4, user.getEmail());
			
			pstmt.executeUpdate(); // INSERT 실행
			pstmt.close();
			con.close();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return 0;
				
	}
	
	//아이디 중복체크
	public boolean isUserIdExists(String name) {
	    boolean exists = false;
	    try {
	        con = dataFactory.getConnection();
	        String query = "SELECT name FROM users WHERE name = ?";
	        pstmt = con.prepareStatement(query);
	        pstmt.setString(1, name);
	        ResultSet rs = pstmt.executeQuery();
	        exists = rs.next(); // 있으면 true
	        System.out.println(exists+ "result값");
	        rs.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        try { if (pstmt != null) pstmt.close(); if (con != null) con.close(); } catch (Exception e) {}
	    }
	    return exists;
	}

	
	// 2. 로그인 (아이디 + 비밀번호 확인)
    public User login(String name, String pwd) {
        User user = null;
        try {
            con = dataFactory.getConnection();
            String query = "SELECT user_id FROM users WHERE name = ? AND pwd = ?";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, name);
            pstmt.setString(2, pwd);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                user = new User();
                user.setUserId(rs.getString("user_id"));
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (pstmt != null) pstmt.close(); if (con != null) con.close(); } catch (Exception e) {}
        }
        return user;
    }
    
 

    
    // 4. 아이디 찾기 (이메일 기반)
    public String findNameByEmail(String email) {
        String name = null;
        try {
            con = dataFactory.getConnection();
            String query = "SELECT name FROM users WHERE email = ?";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                name = rs.getString("name");
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (pstmt != null) pstmt.close(); if (con != null) con.close(); } catch (Exception e) {}
        }
        return name;
    }

    
    // 5. 비밀번호 찾기 (아이디 + 이름 + 이메일 기반)
    public String findPassword(String name, String email) {
        String password = null;
        try {
            con = dataFactory.getConnection();
            String query = "SELECT pwd FROM users WHERE name = ? AND email = ?";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                password = rs.getString("pwd");
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (pstmt != null) pstmt.close(); if (con != null) con.close(); } catch (Exception e) {}
        }
        return password;
    }


}
