package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class UserDAO {
	
	private PreparedStatement pstmt;
	private Statement stmt;
	private Connection con;
	private String driver = "oracle.jdbc.OracleDriver";
	private String url = "jdbc:oracle:thin:@192.168.0.93:1521/xe";
	private String user = "omok";
	private String pwd = "test1234";
}
