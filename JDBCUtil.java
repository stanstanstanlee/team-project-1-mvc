package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JDBCUtil {
	static final String driverName_MySQL="com.mysql.cj.jdbc.Driver";
	static final String url_MySQL="jdbc:mysql://localhost/kimdb";
	static final String userName="root";
	static final String passwd="12345678";

	public static Connection connect(){
		// 1. JAVA와 DB를 연결해줄 자원(resource)을 가진 클래스
		Connection conn= null;
		try {
			Class.forName(driverName_MySQL);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		// 2. DB와 연결
		try {
			conn=DriverManager.getConnection(url_MySQL, userName, passwd);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

	public static void disconnect(PreparedStatement pstmt, Connection conn) {
		// 4. DB 연결해제 ★
		try {
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void disconnect(ResultSet rs,PreparedStatement pstmt, Connection conn) {
		// 4. DB 연결해제 ★
		try {
			rs.close();
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
