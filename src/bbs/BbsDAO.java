package bbs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BbsDAO {
	
	private Connection conn;
	private ResultSet rs;
	
	public BbsDAO() {   //데이터베이스 연결하기
		try {
			String dbURL = "jdbc:mysql://localhost:3306/BBS?serverTimezone=UTC";
			String dbID = "root";
			String dbPassword = "asd008154";
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public String getDate() {  //현재 시간을 가져오는 함수
		
		String SQL = "SELECT NOW()";  //현재 시간을 가져오는 mySQL 문장
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);  // 데이터베이스 간 충돌방지
			rs = pstmt.executeQuery(); //실행 결과
			
			if(rs.next()) {
				return rs.getString(1);  
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ""; // 데이터베이스 오류
	}
	
	
	public int getNext() {  //bbsID 값을 가져오기 (번호)
		
		String SQL = "SELECT bbsID FROM BBS ORDER BY bbsID DESC";  //내림차순으로 가장 마지막에 쓰인 값
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);  // 데이터베이스 간 충돌방지
			rs = pstmt.executeQuery(); //실행 결과
			
			if(rs.next()) {
				return rs.getInt(1)+1;
			}
			
			return 1;  // 첫번째 게시글인 경우
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1; // 데이터베이스 오류
	}
	
	public int write(String bbsTitle, String userID, String bbsContent) {  //게시글을 삽입
		String SQL = "INSERT INTO BBS VALUES (?, ?, ?, ?, ?, ?)"; //값 추가
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);  // 데이터베이스 간 충돌방지
			pstmt.setInt(1, getNext());
			pstmt.setString(2, bbsTitle);
			pstmt.setString(3, userID);
			pstmt.setString(4, getDate());
			pstmt.setString(5, bbsContent);
			pstmt.setInt(6, 1);   //available 여부(삭제 여부)
					
			return pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1; // 데이터베이스 오류
	}

}























